package com.dsg.kplugin.actions.migration

import com.dsg.kplugin.common.constants.Migration
import com.dsg.kplugin.common.constants.UI
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.migration.MigrationService
import com.dsg.kplugin.service.versioning.VersionService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.StatusBar

class MigrationCommandAction(
    private val bumpType: BumpType,
    private val versionService: VersionService,
    private val migrationService: MigrationService,
) : DumbAwareAction(bumpType.text) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val changelogDir = migrationService.findChangelogDir(project)
        if (changelogDir == null || !changelogDir.isDirectory) {
            return
        }

        val lastVersion = versionService.findLastChangelog(changelogDir)
        val newVersion = versionService.bumpVersion(lastVersion, bumpType)

        WriteCommandAction.runWriteCommandAction(project) {
            try {
                migrationService.createMigrationFiles(project, changelogDir, newVersion)
                notify(project, Migration.MIGRATION_CREATED.format(newVersion))
                Messages.showInfoMessage(project, Migration.MIGRATION_CREATED.format(newVersion), UI.SUCCESS)
            } catch (ex: Exception) {
                notify(project, Migration.MIGRATION_CREATE_ERROR.format(newVersion, ex.message))
            }
        }
    }

    private fun notify(project: Project, text: String) {
        StatusBar.Info.set(text, project)
    }
}
