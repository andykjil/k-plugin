package com.dsg.kplugin.service.migration.generator

class RollbackScriptGenerator : MigrationFileGenerator {
    override fun generateContent(
        newVersion: String,
        userName: String,
        sqlFileName: String,
        rollbackFileName: String,
        sqlFileContent: String,
        rollbackFileContent: String,
    ): String {
        return rollbackFileContent
    }

    override fun fileName(newVersion: String, prefix: String): String {
        return "$prefix-rollback.sql"
    }
}
