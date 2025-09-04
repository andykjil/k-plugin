package com.dsg.kplugin.components

import com.dsg.kplugin.common.K_PLUGIN_SETTINGS
import com.dsg.kplugin.common.PLUGIN_NAME
import com.dsg.kplugin.common.TOOL_BAR_TOOLTIP
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class MigrationToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow,
    ) {
        val panel = JPanel()
        panel.add(JLabel(TOOL_BAR_TOOLTIP))

        val settingsButton = JButton(K_PLUGIN_SETTINGS)
        settingsButton.addActionListener {
            ShowSettingsUtil.getInstance().showSettingsDialog(
                project,
                PLUGIN_NAME,
            )
        }
        panel.add(settingsButton)

        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
