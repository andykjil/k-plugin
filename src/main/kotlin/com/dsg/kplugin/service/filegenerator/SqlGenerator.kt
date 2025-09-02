package com.dsg.kplugin.service.filegenerator

class SqlGenerator : MigrationFileGenerator {
    override fun generateContent(
        newVersion: String,
        userName: String,
        sqlFileName: String,
        rollbackFileName: String
    ): String {
        return "-- SQL миграции для версии $newVersion\n"
    }

    override fun fileName(newVersion: String, prefix: String): String {
        return "$prefix.sql"
    }
}