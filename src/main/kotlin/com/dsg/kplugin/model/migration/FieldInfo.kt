package com.dsg.kplugin.model.migration

data class FieldInfo(
    val name: String,
    val type: String,
    val isId: Boolean,
    val isNullable: Boolean,
    val hasDefault: Boolean,
)
