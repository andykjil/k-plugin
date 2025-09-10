package com.dsg.kplugin.actions.migration

import com.dsg.kplugin.common.constants.Migration
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.migration.MigrationService
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class CreateMigrationGroup :
    ActionGroup(
        Migration.CREATE_MIGRATION_TITLE,
        true,
    ),
    StartupActivity.DumbAware {

    private val migrationService = MigrationService()

    override fun getChildren(e: AnActionEvent?): Array<out AnAction?> {
        return arrayOf(
            MigrationCommandAction(BumpType.MAJOR, migrationService),
            MigrationCommandAction(BumpType.MINOR, migrationService),
            MigrationCommandAction(BumpType.PATCH, migrationService),
        )
    }

    override fun runActivity(project: Project) {
    }
}
