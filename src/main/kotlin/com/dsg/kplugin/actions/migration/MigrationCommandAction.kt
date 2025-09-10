package com.dsg.kplugin.actions.migration

import com.dsg.kplugin.common.constants.EMPTY_STRING
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.migration.MigrationService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar

class MigrationCommandAction(
    private val bumpType: BumpType,
    private val migrationService: MigrationService,
) : DumbAwareAction(bumpType.text) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        WriteCommandAction.runWriteCommandAction(project) {
            try {
                migrationService.createMigrationFiles(project, bumpType, EMPTY_STRING, EMPTY_STRING)
            } catch (_: Exception) {
                // TODO обработать ошибку
            }
        }
    }

    private fun notify(project: Project, text: String) {
        StatusBar.Info.set(text, project)
    }
}
