package com.dsg.kplugin.actions.settings

import com.dsg.kplugin.common.constants.K_PLUGIN_SETTINGS
import com.dsg.kplugin.common.constants.OPEN_SETTINGS
import com.dsg.kplugin.common.constants.PLUGIN_NAME
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

class OpenPluginSettingsAction : AnAction(K_PLUGIN_SETTINGS, OPEN_SETTINGS, AllIcons.General.Settings) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        ShowSettingsUtil.getInstance().showSettingsDialog(project, PLUGIN_NAME)
    }
}
