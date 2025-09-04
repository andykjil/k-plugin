package com.dsg.kplugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service(Service.Level.APP)
@State(
    name = "MigrationPluginSettings",
    storages = [Storage("migrationPlugin.xml")],
)
class MigrationPluginSettings : PersistentStateComponent<MigrationPluginSettings.State> {

    data class State(
        var useCustomUser: Boolean = false,
        var customUserName: String = "",
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): MigrationPluginSettings =
            ApplicationManager.getApplication()
                .getService(MigrationPluginSettings::class.java)
    }
}
