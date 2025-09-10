package com.dsg.kplugin.service.migration.generator

class SqlScriptGenerator : MigrationFileGenerator {
    override fun generateContent(
        newVersion: String,
        userName: String,
        sqlFileName: String,
        rollbackFileName: String,
        sqlFileContent: String,
        rollbackFileContent: String,
    ): String {
        return sqlFileContent
    }

    override fun fileName(newVersion: String, prefix: String): String {
        return "$prefix.sql"
    }
}
