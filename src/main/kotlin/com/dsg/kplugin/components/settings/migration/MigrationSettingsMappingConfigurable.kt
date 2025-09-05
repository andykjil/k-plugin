package com.dsg.kplugin.components.settings.migration

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.NlsContexts
import javax.swing.JComponent

class MigrationSettingsMappingConfigurable : Configurable {
    private val component = MigrationSettingsMappingComponent()

    override fun getDisplayName(): @NlsContexts.ConfigurableName String =
        "Настройки соответствия миграций"

    override fun createComponent(): JComponent = component.getPanel()

    override fun isModified(): Boolean = false

    override fun apply() {}
}
