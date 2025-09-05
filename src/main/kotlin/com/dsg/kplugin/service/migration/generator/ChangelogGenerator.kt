package com.dsg.kplugin.service.migration.generator

import com.dsg.kplugin.common.constants.Migration

class ChangelogGenerator : MigrationFileGenerator {
    override fun generateContent(
        newVersion: String,
        userName: String,
        sqlFileName: String,
        rollbackFileName: String,
    ): String {
        return Migration.Templates.CHANGELOG_CONTENT.format(
            newVersion,
            userName,
            newVersion,
            sqlFileName,
            rollbackFileName,
        ).trimIndent()
    }

    override fun fileName(newVersion: String, prefix: String): String {
        return "changelog-$newVersion.yml"
    }
}
