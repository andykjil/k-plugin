package com.dsg.kplugin.actions

import com.dsg.kplugin.common.MIGRATION_TITLE
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.MigrationService
import com.dsg.kplugin.service.versioning.GitService
import com.dsg.kplugin.service.versioning.VersionService
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class CreateMigrationGroup :
    ActionGroup(
        MIGRATION_TITLE,
        true,
    ),
    StartupActivity.DumbAware {

    private val gitService = GitService()
    private val versionService = VersionService()
    private val migrationService = MigrationService()

    override fun getChildren(e: AnActionEvent?): Array<out AnAction?> {
        return arrayOf(
            MigrationCommandAction("Major", BumpType.MAJOR, gitService, versionService, migrationService),
            MigrationCommandAction("Minor", BumpType.MINOR, gitService, versionService, migrationService),
            MigrationCommandAction("Patch", BumpType.PATCH, gitService, versionService, migrationService),
        )
    }

    override fun runActivity(project: Project) {
    }
}
