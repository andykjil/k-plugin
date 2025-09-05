package com.dsg.kplugin.service.migration.generator

class RollbackGenerator : MigrationFileGenerator {
    override fun generateContent(
        newVersion: String,
        userName: String,
        sqlFileName: String,
        rollbackFileName: String,
    ): String {
        return "-- SQL отката для версии $newVersion\n"
    }

    override fun fileName(newVersion: String, prefix: String): String {
        return "$prefix-rollback.sql"
    }
}
