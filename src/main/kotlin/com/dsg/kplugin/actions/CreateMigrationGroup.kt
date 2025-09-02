package com.dsg.kplugin.actions

import CHANGELOG_CONTENT
import CHANGELOG_DEFAULT_VERSION
import CHANGELOG_INCLUDE_CONTENT
import CHANGELOG_PATH
import MIGRATION_CREATED
import MIGRATION_CREATE_ERROR
import MIGRATION_TITLE
import VERSION_REGEXP
import com.dsg.kplugin.model.enums.BumpType
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.WindowManager

class CreateMigrationGroup : ActionGroup(
    MIGRATION_TITLE, true
), StartupActivity.DumbAware {


    override fun getChildren(e: AnActionEvent?): Array<out AnAction?> {
        return arrayOf(
            BumpAction("Major", BumpType.MAJOR),
            BumpAction("Minor", BumpType.MINOR),
            BumpAction("Patch", BumpType.PATCH),
        )
    }

    override fun runActivity(project: Project) {
        TODO("Not yet implemented")
    }

    private inner class BumpAction(text: String, private val bumpType: BumpType) : DumbAwareAction(text) {
        override fun actionPerformed(e: AnActionEvent) {
            val project = e.project ?: return

            val dbModule = findDbModule(project) ?: return
            val changelogDir = dbModule.findFileByRelativePath(CHANGELOG_PATH)
            if (changelogDir == null || !changelogDir.isDirectory) {
                return
            }

            val lastVersion = findLastChangelog(changelogDir)

            val newVersion = bumpVersion(lastVersion, bumpType)

            try {
                createMigrationFiles(project, changelogDir, newVersion)
                notify(project, MIGRATION_CREATED.format(newVersion))
            } catch (e: Exception) {
                notify(project, MIGRATION_CREATE_ERROR.format(newVersion, e.message))
            }
        }
    }

    private fun createMigrationFiles(
        project: Project,
        changelogDir: VirtualFile,
        newVersion: String
    ) {
        WriteCommandAction.runWriteCommandAction(project) {
            val newChangelogDir = changelogDir.createChildDirectory(this, newVersion)
            val changelogFile = newChangelogDir.createChildData(this, "changelog-$newVersion.yml")
            val sqlFile = newChangelogDir.createChildData(this, "feature-branch.sql")
            val rollbackFile = newChangelogDir.createChildData(this, "feature-branch-rollback.sql")

            val sqlContent = "-- SQL миграции для версии $newVersion\n"
            val rollbackContent = "-- SQL отката для версии $newVersion\n"

            VfsUtil.saveText(
                changelogFile, CHANGELOG_CONTENT.format(
                    newVersion,
                    System.getProperty("user.name"),
                    newVersion,
                    sqlFile.name,
                    rollbackFile.name
                ).trimIndent()
            )
            VfsUtil.saveText(sqlFile, sqlContent)
            VfsUtil.saveText(rollbackFile, rollbackContent)

            val masterFile = changelogDir.findChild("db.changelog-master.yaml")
                ?: changelogDir.createChildData(this, "db.changelog-master.yaml")

            val oldMasterContent = VfsUtil.loadText(masterFile).trimEnd()
            val newMasterContent = if (oldMasterContent.contains(newVersion)) {
                oldMasterContent
            } else {
                buildString {
                    append(oldMasterContent)
                    append(CHANGELOG_INCLUDE_CONTENT.format("$newVersion/changelog-$newVersion.yml"))
                }
            }

            VfsUtil.saveText(masterFile, newMasterContent)
            Messages.showInfoMessage(project, MIGRATION_CREATED.format(newVersion), "Успех")
        }
    }

    private fun findLastChangelog(changelogDir: VirtualFile): String {
        val subdirs = changelogDir.children.filter { it.isDirectory }.map { it.name }
        val versionRegex = Regex(VERSION_REGEXP)
        val versions = subdirs.mapNotNull { name ->
            val match = versionRegex.matchEntire(name)
            if (match != null) {
                val (maj, min, pat) = match.destructured
                Triple(maj.toInt(), min.toInt(), pat.toInt()) to name
            } else {
                null
            }
        }
        val maxVersion = versions.maxWithOrNull(compareBy({ it.first.first }, { it.first.second }, { it.first.third }))
        return maxVersion?.second ?: CHANGELOG_DEFAULT_VERSION
    }

    private fun findDbModule(project: Project): VirtualFile? =
        project.baseDir?.let {
            it.findChild("db")
        }

    private fun bumpVersion(version: String, type: BumpType): String {
        val parts = version.split(".").map { it.toInt() }.toMutableList()
        while (parts.size < 3) parts.add(0)
        when (type) {
            BumpType.MAJOR -> {
                parts[0] += 1; parts[1] = 0; parts[2] = 0
            }

            BumpType.MINOR -> {
                parts[1] += 1; parts[2] = 0
            }

            BumpType.PATCH -> {
                parts[2] += 1
            }
        }
        return parts.joinToString(".")
    }

    private fun notify(project: Project, text: String) {
        // Неброское сообщение в статус-бар (вместо всплывающих диалогов)
        val frame = WindowManager.getInstance().getFrame(project) ?: return
        StatusBar.Info.set(text, project)
    }
}