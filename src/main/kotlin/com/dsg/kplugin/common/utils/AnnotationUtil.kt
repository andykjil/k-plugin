package com.dsg.kplugin.common.utils

import com.dsg.kplugin.components.settings.migration.MigrationMappingSettings
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtParameter

class AnnotationUtil(private val mappingService: MigrationMappingSettings) {

    fun isKtClassWithTableAnnotation(element: PsiElement): Boolean {
        val ktClass = getKtClass(element) ?: return false
        return ktClass.annotationEntries.any { it.text.contains("Table") }
    }

    fun getKtClass(element: PsiElement): KtClass? = when (element) {
        is KtClass -> element
        is KtParameter -> PsiTreeUtil.getParentOfType(element, KtClass::class.java)
        else -> PsiTreeUtil.getParentOfType(element, KtClass::class.java)
    }

    fun extractAnnotationValue(annotationEntry: KtAnnotationEntry, paramName: String): String? {
        return annotationEntry.valueArguments.firstOrNull {
            it.getArgumentName()?.asName?.asString() == paramName
        }?.getArgumentExpression()?.text?.removeSurrounding("\"")
    }

    fun resolveSqlType(kotlinType: String): String {
        val cleanType = kotlinType.removeSuffix("?").substringAfterLast('.')
        val mapping = mappingService.state.mappings.find { it.kotlinType == cleanType }
        return mapping?.postgresType ?: "text"
    }
}
