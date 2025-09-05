package com.dsg.kplugin.components.settings.migration

import com.dsg.kplugin.actions.settings.OpenPluginSettingsAction
import com.dsg.kplugin.common.constants.TOOLBAR_LOGS_TAB_TITLE
import com.dsg.kplugin.common.constants.TOOLBAR_LOGS_TITLE
import com.dsg.kplugin.common.constants.TOOLBAR_MIGRATIONS_TAB_TITLE
import com.dsg.kplugin.service.migration.MigrationService
import com.dsg.kplugin.service.versioning.VersionService
import com.dsg.kplugin.ui.MigrationsPanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.content.ContentManagerEvent
import com.intellij.ui.content.ContentManagerListener
import javax.swing.JLabel
import javax.swing.JPanel

class MigrationToolWindowFactory : ToolWindowFactory {

    private lateinit var migrationService: MigrationService
    private lateinit var versionService: VersionService

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        initializeServices()

        val migrationsPanel = createMigrationsPanel(project)
        val migrationsContent = createContent(migrationsPanel, TOOLBAR_MIGRATIONS_TAB_TITLE)

        val logsPanel = createLogsPanel()
        val logsContent = createContent(logsPanel, TOOLBAR_LOGS_TAB_TITLE)

        toolWindow.contentManager.addContent(migrationsContent)
        toolWindow.contentManager.addContent(logsContent)

        toolWindow.setTitleActions(listOf(OpenPluginSettingsAction()))
        toolWindow.contentManager.addContentManagerListener(createContentManagerListener(toolWindow, migrationsContent, migrationsPanel))

        val connection = project.messageBus.connect()
        connection.subscribe(ToolWindowManagerListener.TOPIC, createToolWindowManagerListener(toolWindow, migrationsContent, migrationsPanel))
    }

    private fun initializeServices() {
        migrationService = MigrationService()
        versionService = VersionService()
    }

    private fun createMigrationsPanel(project: Project): MigrationsPanel {
        return MigrationsPanel(project, migrationService, versionService)
    }

    private fun createLogsPanel(): JPanel {
        return JPanel().apply {
            add(JLabel(TOOLBAR_LOGS_TITLE))
        }
    }

    private fun createContent(component: JPanel, displayName: String): Content {
        val contentFactory = ContentFactory.getInstance()
        return contentFactory.createContent(component, displayName, false)
    }

    private fun createContentManagerListener(
        toolWindow: ToolWindow,
        migrationsContent: Content,
        migrationsPanel: MigrationsPanel,
    ): ContentManagerListener {
        return object : ContentManagerListener {
            override fun selectionChanged(event: ContentManagerEvent) {
                val selectedContent = toolWindow.contentManager.selectedContent
                if (selectedContent == migrationsContent) {
                    migrationsPanel.loadLastVersion()
                }
            }
        }
    }

    private fun createToolWindowManagerListener(
        toolWindow: ToolWindow,
        migrationsContent: Content,
        migrationsPanel: MigrationsPanel,
    ): ToolWindowManagerListener {
        return object : ToolWindowManagerListener {
            override fun toolWindowShown(toolWindowShown: ToolWindow) {
                if (toolWindowShown.id == toolWindow.id) {
                    val selectedContent = toolWindowShown.contentManager.selectedContent
                    if (selectedContent == migrationsContent) {
                        migrationsPanel.loadLastVersion()
                    }
                }
            }
        }
    }
}
