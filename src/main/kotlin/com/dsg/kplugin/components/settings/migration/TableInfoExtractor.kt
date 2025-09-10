package com.dsg.kplugin.components.settings.migration

import com.dsg.kplugin.common.utils.AnnotationUtil
import com.dsg.kplugin.model.migration.FieldInfo
import com.dsg.kplugin.model.migration.TableInfo
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtParameter

class TableInfoExtractor(private val annotationUtil: AnnotationUtil) {

    fun extract(ktClass: KtClass): TableInfo? {
        val tableAnnotation = ktClass.annotationEntries.find { it.text.contains("Table") } ?: return null
        val schema = annotationUtil.extractAnnotationValue(tableAnnotation, "schema") ?: "public"
        val name = annotationUtil.extractAnnotationValue(tableAnnotation, "name") ?: ktClass.name ?: return null
        val fields = ktClass.primaryConstructor?.valueParameters?.mapNotNull { extractFieldInfo(it) }.orEmpty()
        if (fields.none { it.isId }) return null
        return TableInfo(schema, name, fields)
    }

    private fun extractFieldInfo(param: KtParameter): FieldInfo? {
        val name = param.name ?: return null
        val typeReference = param.typeReference ?: return null
        val typeText = typeReference.text
        val isId = param.annotationEntries.any { it.text.contains("Id") }
        val hasDefault = param.hasDefaultValue() && param.defaultValue?.text != "null"
        val isNullable = typeText.endsWith("?") && !hasDefault
        val sqlType = annotationUtil.resolveSqlType(typeText)
        return FieldInfo(name, sqlType, isId, isNullable, hasDefault)
    }
}
