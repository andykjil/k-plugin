package com.dsg.kplugin.actions.settings

import com.dsg.kplugin.common.constants.PluginInfo
import com.dsg.kplugin.common.constants.UI
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

class OpenPluginSettingsAction : AnAction(UI.K_PLUGIN_SETTINGS, UI.OPEN_SETTINGS, AllIcons.General.Settings) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        ShowSettingsUtil.getInstance().showSettingsDialog(project, PluginInfo.PLUGIN_NAME)
    }
}
