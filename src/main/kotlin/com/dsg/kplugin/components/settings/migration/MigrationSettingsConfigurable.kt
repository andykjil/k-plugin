package com.dsg.kplugin.components.settings.migration

import com.dsg.kplugin.common.constants.UI
import com.dsg.kplugin.service.versioning.GitService
import com.dsg.kplugin.settings.MigrationPluginSettings
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import javax.swing.JComponent

class MigrationSettingsConfigurable(private val project: Project) : Configurable {
    private var component: MigrationSettingsComponent? = null

    override fun getDisplayName(): @NlsContexts.ConfigurableName String =
        UI.K_PLUGIN_SETTINGS

    override fun createComponent(): JComponent {
        val initialUser = GitService().getDefaultUserNameSync(project)
        component = MigrationSettingsComponent(project, initialUser)
        return component!!.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = MigrationPluginSettings.getInstance().state
        return component?.let {
            it.isUseCustomUser() != settings.useCustomUser ||
                (it.isUseCustomUser() && it.getUserName() != settings.customUserName)
        } ?: false
    }

    override fun apply() {
        val settings = MigrationPluginSettings.getInstance().state
        component?.let {
            settings.useCustomUser = it.isUseCustomUser()
            if (settings.useCustomUser) {
                settings.customUserName = it.getUserName()
            }
        }
    }

    override fun reset() {
        val settings = MigrationPluginSettings.getInstance().state
        component?.apply {
            val initialUser = GitService().getDefaultUserNameSync(project)
            setUserName(
                if (settings.useCustomUser && settings.customUserName.isNotBlank()) {
                    settings.customUserName
                } else {
                    initialUser
                },
            )
            setCustomUserEnabled(settings.useCustomUser)
        }
    }
}
