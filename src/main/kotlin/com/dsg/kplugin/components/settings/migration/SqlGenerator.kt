package com.dsg.kplugin.components.settings.migration

import com.dsg.kplugin.common.constants.Migration
import com.dsg.kplugin.model.migration.FieldInfo
import com.dsg.kplugin.model.migration.TableInfo

class SqlGenerator {

    fun generateCreateScript(tableInfo: TableInfo): String = buildString {
        appendLine(Migration.Templates.SCHEMA_CREATE.format(tableInfo.schema))
        appendLine()
        appendLine(Migration.Templates.TABLE_CREATE_START.format(tableInfo.schema, tableInfo.name))
        appendLine(
            tableInfo.fields.joinToString(",\n") { "    ${buildColumnDefinition(it)}" },
        )
        appendLine(Migration.Templates.TABLE_CREATE_END)
        appendLine()
        appendLine(Migration.Templates.TABLE_COMMENT.format(tableInfo.schema, tableInfo.name, ""))
        appendLine()
        tableInfo.fields.forEach {
            appendLine(Migration.Templates.COLUMN_COMMENT.format(tableInfo.schema, tableInfo.name, it.name, ""))
        }
    }

    fun generateRollbackScript(tableInfo: TableInfo): String =
        Migration.Templates.DROP_TABLE.format(tableInfo.schema, tableInfo.name)

    private fun buildColumnDefinition(field: FieldInfo): String {
        val base = "${field.name} ${field.type}"
        val constraints = buildList {
            if (field.isId) {
                add(Migration.Templates.PRIMARY_KEY_DEFAULT_UUID)
            } else if (!field.isNullable) add(Migration.Templates.NOT_NULL)
            if (!field.isId && field.hasDefault) {
                when (field.type) {
                    Migration.Templates.TIMESTAMP, Migration.Templates.TIMESTAMP_WITH_TIMEZONE -> add(Migration.Templates.DEFAULT_CURRENT_TIMESTAMP)
                }
            }
        }
        return listOf(base).plus(constraints).joinToString(" ")
    }
}
