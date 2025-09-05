package com.dsg.kplugin.service.migration

import com.dsg.kplugin.common.constants.Migration
import com.dsg.kplugin.service.migration.generator.ChangelogGenerator
import com.dsg.kplugin.service.migration.generator.MigrationBuilder
import com.dsg.kplugin.service.migration.generator.MigrationFileGenerator
import com.dsg.kplugin.service.migration.generator.RollbackGenerator
import com.dsg.kplugin.service.migration.generator.SqlGenerator
import com.dsg.kplugin.service.versioning.GitService
import com.dsg.kplugin.settings.MigrationPluginSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class MigrationService {

    private val changelogGenerator: MigrationFileGenerator = ChangelogGenerator()
    private val sqlGenerator: MigrationFileGenerator = SqlGenerator()
    private val rollbackGenerator: MigrationFileGenerator = RollbackGenerator()

    fun findDbModule(project: Project): VirtualFile? =
        project.baseDir?.findChild(Migration.MIGRATION_DIRECTORY)

    fun createMigrationFiles(
        project: Project,
        changelogDir: VirtualFile,
        newVersion: String,
    ) {
        val settings = MigrationPluginSettings.Companion.getInstance().state
        val branchName = GitService().getCurrentBranch(project) ?: "feature-branch"
        val prefix = branchFilePrefix(branchName)

        val builder = MigrationBuilder(
            project = project,
            changelogDir = changelogDir,
            newVersion = newVersion,
            userName = settings.customUserName,
            prefix = prefix,
            changelogGenerator = changelogGenerator,
            sqlGenerator = sqlGenerator,
            rollbackGenerator = rollbackGenerator,
        )
        builder.build()
    }

    private fun branchFilePrefix(branchName: String): String =
        branchName.substringAfterLast('/')

    fun findChangelogDir(project: Project): VirtualFile? =
        findDbModule(project)?.findFileByRelativePath(Migration.CHANGELOG_PATH)
}
