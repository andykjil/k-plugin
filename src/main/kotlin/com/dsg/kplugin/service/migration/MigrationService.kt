package com.dsg.kplugin.service.migration

import com.dsg.kplugin.common.constants.Migration
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.migration.generator.ChangelogGenerator
import com.dsg.kplugin.service.migration.generator.MigrationBuilder
import com.dsg.kplugin.service.migration.generator.MigrationFileGenerator
import com.dsg.kplugin.service.migration.generator.RollbackScriptGenerator
import com.dsg.kplugin.service.migration.generator.SqlScriptGenerator
import com.dsg.kplugin.service.versioning.GitService
import com.dsg.kplugin.service.versioning.VersionService
import com.dsg.kplugin.settings.MigrationPluginSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class MigrationService {
    private val changelogGenerator: MigrationFileGenerator by lazy { ChangelogGenerator() }
    private val sqlScriptGenerator: MigrationFileGenerator by lazy { SqlScriptGenerator() }
    private val rollbackScriptGenerator: MigrationFileGenerator by lazy { RollbackScriptGenerator() }
    private val versionService by lazy { VersionService() }

    fun findDbModule(project: Project): VirtualFile? =
        project.baseDir?.findChild(Migration.MIGRATION_DIRECTORY)

    fun createMigrationFiles(
        project: Project,
        bumpType: BumpType,
        sqlFileContent: String,
        rollbackFileContent: String = "",
    ) {
        val settings = MigrationPluginSettings.getInstance().state
        val changelogDir = findChangelogDir(project) ?: return
        MigrationBuilder(
            project = project,
            changelogDir = changelogDir,
            newVersion = versionService.bumpVersion(
                versionService.findLastChangelog(changelogDir),
                bumpType,
            ),
            userName = settings.customUserName,
            prefix = branchFilePrefix(GitService().getCurrentBranch(project) ?: "feature-branch"),
            changelogGenerator = changelogGenerator,
            sqlGenerator = sqlScriptGenerator,
            rollbackGenerator = rollbackScriptGenerator,
            sqlFileContent = sqlFileContent,
            rollbackFileContent = rollbackFileContent,
        ).build()
    }

    private fun branchFilePrefix(branchName: String): String =
        branchName.substringAfterLast('/')

    fun findChangelogDir(project: Project): VirtualFile? =
        findDbModule(project)?.findFileByRelativePath(Migration.CHANGELOG_PATH)
}
