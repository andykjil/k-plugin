package com.dsg.kplugin.service

import com.dsg.kplugin.common.MIGRATION_DIRECTORY
import com.dsg.kplugin.service.filegenerator.ChangelogGenerator
import com.dsg.kplugin.service.filegenerator.MigrationBuilder
import com.dsg.kplugin.service.filegenerator.MigrationFileGenerator
import com.dsg.kplugin.service.filegenerator.RollbackGenerator
import com.dsg.kplugin.service.filegenerator.SqlGenerator
import com.dsg.kplugin.service.versioning.GitService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class MigrationService {

    private val changelogGenerator: MigrationFileGenerator = ChangelogGenerator()
    private val sqlGenerator: MigrationFileGenerator = SqlGenerator()
    private val rollbackGenerator: MigrationFileGenerator = RollbackGenerator()

    fun findDbModule(project: Project): VirtualFile? =
        project.baseDir?.findChild(MIGRATION_DIRECTORY)

    fun createMigrationFiles(
        project: Project,
        changelogDir: VirtualFile,
        newVersion: String,
        userName: String,
    ) {
        val branchName = GitService().getCurrentBranch(project) ?: "feature-branch"
        val prefix = branchFilePrefix(branchName)

        val builder = MigrationBuilder(
            project = project,
            changelogDir = changelogDir,
            newVersion = newVersion,
            userName = userName,
            prefix = prefix,
            changelogGenerator = changelogGenerator,
            sqlGenerator = sqlGenerator,
            rollbackGenerator = rollbackGenerator,
        )
        builder.build()
    }

    private fun branchFilePrefix(branchName: String): String =
        branchName.substringAfterLast('/')
}
