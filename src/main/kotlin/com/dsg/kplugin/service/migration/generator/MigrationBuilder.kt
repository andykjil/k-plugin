package com.dsg.kplugin.service.migration.generator

import com.dsg.kplugin.common.constants.EMPTY_STRING
import com.dsg.kplugin.common.constants.Migration
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

class MigrationBuilder(
    private val project: Project,
    private val changelogDir: VirtualFile,
    private val newVersion: String,
    private val userName: String,
    private val prefix: String,
    private val changelogGenerator: MigrationFileGenerator,
    private val sqlGenerator: MigrationFileGenerator,
    private val rollbackGenerator: MigrationFileGenerator,
    private val sqlFileContent: String,
    private val rollbackFileContent: String,
) {

    fun build() {
        WriteCommandAction.runWriteCommandAction(project) {
            val newChangelogDir = changelogDir.createChildDirectory(this, newVersion)
            createAndWriteFile(
                parent = newChangelogDir,
                generator = changelogGenerator,
                version = newVersion,
                user = userName,
                sqlName = sqlGenerator.fileName(newVersion, prefix),
                rollbackName = rollbackGenerator.fileName(newVersion, prefix),
            )
            createAndWriteFile(
                parent = newChangelogDir,
                generator = sqlGenerator,
                version = newVersion,
                user = userName,
            )
            createAndWriteFile(
                parent = newChangelogDir,
                generator = rollbackGenerator,
                version = newVersion,
                user = userName,
            )
            val masterFile = changelogDir.findChild(Migration.CHANGELOG_MASTER_FILENAME)
                ?: changelogDir.createChildData(this, Migration.CHANGELOG_MASTER_FILENAME)

            val oldMasterContent = VfsUtil.loadText(masterFile).trimEnd()
            val includeContent = Migration.Templates.CHANGELOG_INCLUDE_CONTENT.format("$newVersion/changelog-$newVersion.yml")
            val newMasterContent = if (oldMasterContent.contains(newVersion)) {
                oldMasterContent
            } else {
                buildString {
                    append(oldMasterContent)
                    append(includeContent)
                }
            }
            VfsUtil.saveText(masterFile, newMasterContent)
        }
    }

    fun createAndWriteFile(
        parent: VirtualFile,
        generator: MigrationFileGenerator,
        version: String,
        user: String,
        sqlName: String = EMPTY_STRING,
        rollbackName: String = EMPTY_STRING,
        sqlContent: String = sqlFileContent,
        rollbackContent: String = rollbackFileContent,
    ): VirtualFile {
        val file = parent.createChildData(this, generator.fileName(version, prefix))
        val content = generator.generateContent(
            version,
            user,
            sqlName,
            rollbackName,
            sqlContent,
            rollbackContent,
        )
        VfsUtil.saveText(file, content)
        return file
    }
}
