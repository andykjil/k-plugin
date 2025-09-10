package com.dsg.kplugin.model.migration

data class TableInfo(
    val schema: String,
    val name: String,
    val fields: List<FieldInfo>,
)
