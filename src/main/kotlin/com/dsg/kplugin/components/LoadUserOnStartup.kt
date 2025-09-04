package com.dsg.kplugin.components

import com.dsg.kplugin.service.versioning.GitService
import com.dsg.kplugin.settings.MigrationPluginSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class LoadUserOnStartup : ProjectActivity {

    override suspend fun execute(project: Project) {
        val settings = MigrationPluginSettings.getInstance().state

        if (!settings.useCustomUser || settings.customUserName.isBlank()) {
            GitService().getGitUserName(project) { gitUser ->
                settings.customUserName = gitUser
            }
        }
    }
}
