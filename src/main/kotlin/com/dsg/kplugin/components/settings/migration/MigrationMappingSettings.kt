package com.dsg.kplugin.components.settings.migration

import com.dsg.kplugin.common.constants.MigrationMapping
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

@State(
    name = "MigrationMappingSettings",
    storages = [Storage("migrationMappingSettings.xml")],
)
@Service(Service.Level.APP)
class MigrationMappingSettings : PersistentStateComponent<MigrationMappingSettings.State> {

    data class State(var mappings: MutableList<Mapping> = mutableListOf())

    data class Mapping(var kotlinType: String = "", var postgresType: String = "")

    private var state = State()

    init {
        ensureDefaultMappings()
    }

    private fun ensureDefaultMappings() {
        if (state.mappings.isEmpty()) {
            state.mappings.addAll(MigrationMapping.DEFAULT_DATA.map { Mapping(it[0], it[1]) })
        }
    }

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
        ensureDefaultMappings()
    }

    fun getMappings(): List<Pair<String, String>> =
        state.mappings.map { it.kotlinType to it.postgresType }

    fun setMappings(mappings: List<Pair<String, String>>) {
        state.mappings = mappings.map { Mapping(it.first, it.second) }.toMutableList()
    }

    companion object {
        fun getInstance(): MigrationMappingSettings = service()
    }
}
