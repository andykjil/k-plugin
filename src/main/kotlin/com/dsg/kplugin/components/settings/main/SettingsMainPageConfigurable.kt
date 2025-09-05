package com.dsg.kplugin.components.settings.main

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class SettingsMainPageConfigurable() : Configurable {

    private val component = SettingsMainPageComponent()

    override fun getDisplayName(): String = "K-Plugin"

    override fun createComponent(): JComponent = component.getPanel()

    override fun isModified(): Boolean = false
    override fun apply() {}
}
