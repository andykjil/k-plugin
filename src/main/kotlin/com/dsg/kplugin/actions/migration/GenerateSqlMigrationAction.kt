package com.dsg.kplugin.actions.migration

import com.dsg.kplugin.common.utils.AnnotationUtil
import com.dsg.kplugin.components.settings.migration.MigrationMappingSettings
import com.dsg.kplugin.components.settings.migration.SqlGenerator
import com.dsg.kplugin.components.settings.migration.TableInfoExtractor
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.migration.MigrationService
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages

class GenerateSqlMigrationAction : AnAction("Создать миграцию") {

    private val migrationService = MigrationService()
    private val mappingService = MigrationMappingSettings.getInstance()
    private val annotationUtil = AnnotationUtil(mappingService)
    private val tableInfoExtractor = TableInfoExtractor(annotationUtil)
    private val sqlGenerator = SqlGenerator()

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)
        e.presentation.isEnabled = psiElement?.let { annotationUtil.isKtClassWithTableAnnotation(it) } ?: false
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return
        val ktClass = annotationUtil.getKtClass(psiElement) ?: return

        try {
            val tableInfo = tableInfoExtractor.extract(ktClass) ?: run {
                Messages.showErrorDialog(project, "Класс не содержит корректную аннотацию @Table", "Ошибка")
                return
            }
            val sqlScript = sqlGenerator.generateCreateScript(tableInfo)
            val rollbackScript = sqlGenerator.generateRollbackScript(tableInfo)
            migrationService.createMigrationFiles(
                project,
                BumpType.MINOR,
                sqlScript,
                rollbackScript,
            )
            Messages.showInfoMessage(project, "Миграция успешно создана!", "Успех")
        } catch (ex: Exception) {
            Messages.showErrorDialog(project, "Ошибка генерации миграции: ${ex.message}", "Ошибка")
        }
    }
}
