package com.dsg.kplugin.service.migration.generator

interface MigrationFileGenerator {
    fun generateContent(
        newVersion: String,
        userName: String,
        sqlFileName: String = "",
        rollbackFileName: String = "",
    ): String
    fun fileName(newVersion: String, prefix: String): String
}
