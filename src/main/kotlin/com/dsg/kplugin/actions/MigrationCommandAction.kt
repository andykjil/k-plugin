package com.dsg.kplugin.actions

import CHANGELOG_PATH
import MIGRATION_CREATED
import MIGRATION_CREATE_ERROR
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.GitService
import com.dsg.kplugin.service.MigrationService
import com.dsg.kplugin.service.VersionService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.StatusBar

class MigrationCommandAction(
    text: String,
    private val bumpType: BumpType,
    private val gitService: GitService,
    private val versionService: VersionService,
    private val migrationService: MigrationService,
) : DumbAwareAction(text) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val dbModule = migrationService.findDbModule(project) ?: return
        val changelogDir = dbModule.findFileByRelativePath(CHANGELOG_PATH)
        if (changelogDir == null || !changelogDir.isDirectory) {
            return
        }

        val lastVersion = versionService.findLastChangelog(changelogDir)
        val newVersion = versionService.bumpVersion(lastVersion, bumpType)

        gitService.getGitUserName(project) { userName ->
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    migrationService.createMigrationFiles(project, changelogDir, newVersion, userName)
                    notify(project, MIGRATION_CREATED.format(newVersion))
                    Messages.showInfoMessage(project, MIGRATION_CREATED.format(newVersion), "Успех")
                } catch (ex: Exception) {
                    notify(project, MIGRATION_CREATE_ERROR.format(newVersion, ex.message))
                }
            }
        }
    }

    private fun notify(project: Project, text: String) {
        StatusBar.Info.set(text, project)
    }
}
