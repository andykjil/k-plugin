package com.dsg.kplugin.service.filegenerator

import com.dsg.kplugin.common.CHANGELOG_CONTENT

class ChangelogGenerator : MigrationFileGenerator {
    override fun generateContent(
        newVersion: String,
        userName: String,
        sqlFileName: String,
        rollbackFileName: String,
    ): String {
        return CHANGELOG_CONTENT.format(
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
