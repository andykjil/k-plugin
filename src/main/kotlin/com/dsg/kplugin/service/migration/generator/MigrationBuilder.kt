package com.dsg.kplugin.service.migration.generator

import com.dsg.kplugin.common.constants.CHANGELOG_INCLUDE_CONTENT
import com.dsg.kplugin.common.constants.CHANGELOG_MASTER_FILENAME
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
) {
    fun build() {
        WriteCommandAction.runWriteCommandAction(project) {
            val newChangelogDir = createDirectory()
            val changelogFile = createFile(
                parent = newChangelogDir,
                fileName = changelogGenerator.fileName(newVersion, prefix),
            )
            val sqlFile = createFile(
                parent = newChangelogDir,
                fileName = sqlGenerator.fileName(newVersion, prefix),
            )
            val rollbackFile = createFile(
                parent = newChangelogDir,
                fileName = rollbackGenerator.fileName(newVersion, prefix),
            )
            val masterFile = changelogDir.findChild(CHANGELOG_MASTER_FILENAME)
                ?: changelogDir.createChildData(this, CHANGELOG_MASTER_FILENAME)

            writeFileContent(
                file = changelogFile,
                content = changelogGenerator.generateContent(
                    newVersion,
                    userName,
                    sqlFile.name,
                    rollbackFile.name,
                ),
            )
            writeFileContent(
                file = sqlFile,
                content = sqlGenerator.generateContent(newVersion, userName),
            )
            writeFileContent(
                file = rollbackFile,
                content = rollbackGenerator.generateContent(newVersion, userName),
            )
            updateMasterFile(masterFile)
        }
    }

    private fun createDirectory(): VirtualFile {
        return changelogDir.createChildDirectory(this, newVersion)
    }

    private fun createFile(parent: VirtualFile, fileName: String): VirtualFile {
        return parent.createChildData(this, fileName)
    }

    private fun writeFileContent(file: VirtualFile, content: String) {
        VfsUtil.saveText(file, content)
    }

    private fun updateMasterFile(masterFile: VirtualFile) {
        val oldMasterContent = VfsUtil.loadText(masterFile).trimEnd()
        val includeContent = CHANGELOG_INCLUDE_CONTENT.format("$newVersion/changelog-$newVersion.yml")
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
