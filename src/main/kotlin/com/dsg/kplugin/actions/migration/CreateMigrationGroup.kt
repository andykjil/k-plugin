package com.dsg.kplugin.actions.migration

import com.dsg.kplugin.common.constants.CREATE_MIGRATION_TITLE
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.migration.MigrationService
import com.dsg.kplugin.service.versioning.VersionService
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class CreateMigrationGroup :
    ActionGroup(
        CREATE_MIGRATION_TITLE,
        true,
    ),
    StartupActivity.DumbAware {

    private val versionService = VersionService()
    private val migrationService = MigrationService()

    override fun getChildren(e: AnActionEvent?): Array<out AnAction?> {
        return arrayOf(
            MigrationCommandAction(BumpType.MAJOR, versionService, migrationService),
            MigrationCommandAction(BumpType.MINOR, versionService, migrationService),
            MigrationCommandAction(BumpType.PATCH, versionService, migrationService),
        )
    }

    override fun runActivity(project: Project) {
    }
}
