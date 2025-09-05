package com.dsg.kplugin.components.settings.main

import com.intellij.ide.DataManager
import com.intellij.openapi.options.ex.Settings
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

class SettingsMainPageComponent {

    private val panel: JPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        val migrationsButton = JButton("Открыть настройки миграций").apply {
            addActionListener {
                navigateTo("com.dsg.kplugin.settings.migrations")
            }
        }

        val gitButton = JButton("Открыть настройки Git").apply {
            addActionListener {
                navigateTo("com.dsg.kplugin.settings.git")
            }
        }

        add(migrationsButton)
        add(gitButton)
    }

    fun getPanel(): JComponent = panel

    private fun navigateTo(id: String) {
        val ctx = DataManager.getInstance().getDataContext(panel)
        val settings = Settings.KEY.getData(ctx)
        val configurable = settings?.find(id)
        if (configurable != null) {
            settings.select(configurable) // ⚡ переключаемся без открытия нового окна
        }
    }
}
