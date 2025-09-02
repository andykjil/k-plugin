package com.dsg.kplugin.actions

import CHANGELOG_CONTENT
import CHANGELOG_DEFAULT_VERSION
import CHANGELOG_INCLUDE_CONTENT
import CHANGELOG_PATH
import MIGRATION_CREATED
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

class CreateMigrationAction(
    private val bumpType: String = "patch"
) : AnAction(bumpType) {

    override fun actionPerformed(p0: AnActionEvent) {
        val project = p0.project ?: return

        val dbModule = findDbModule(project) ?: return
        val changelogDir = dbModule.findFileByRelativePath(CHANGELOG_PATH)
        if (changelogDir == null || !changelogDir.isDirectory) {
            return
        }

        val lastVersion = findLastChangelog(changelogDir)
//        val newVersion = bumpVersion(lastVersion, bumpType)

//        createMigrationFiles(project, changelogDir, newVersion)
    }

//    private fun bumpVersion(lastVersion: String, bumpType: String): String {
//        val parts = lastVersion.split(".").map { it.toIntOrNull() ?: 0 }.toMutableList()
//        while (parts.size < 3) parts.add(0)
//        when (bumpType) {
//            MAJOR -> {
//                parts[0] += 1
//                parts[1] = 0
//                parts[2] = 0
//            }
//            MINOR -> {
//                parts[1] += 1
//                parts[2] = 0
//            }
//            PATCH -> {
//                parts[2] += 1
//            }
//
//            else -> throw IllegalArgumentException("Unknown bump type: $bumpType")
//        }
//        return parts.joinToString(".")
//    }

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
        return subdirs.maxOrNull() ?: CHANGELOG_DEFAULT_VERSION
    }

    private fun findDbModule(project: Project): VirtualFile? =
        project.baseDir?.let {
            it.findChild("db")
        }

}