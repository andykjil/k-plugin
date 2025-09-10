package com.dsg.kplugin.common.constants

object PluginInfo {
    const val PLUGIN_NAME = "K-Plugin"
    const val TOOL_BAR_TOOLTIP = "k plugin"
}

object UI {
    const val K_PLUGIN_SETTINGS = "Настройки k plugin"
    const val USER_NAME_TEXT = "Имя пользователя:"
    const val USER_CUSTOM_CHECKBOX_TEXT = "Использовать своё имя пользователя"
    const val OPEN_SETTINGS = "Открыть настройки"

    const val TOOLBAR_LOGS_TAB_TITLE = "Логи"
    const val TOOLBAR_LOGS_TITLE = "Логи"
    const val TOOLBAR_MIGRATIONS_TAB_TITLE = "Миграции"

    const val LAST_VERSION = "Последняя версия:"
    const val NEW_VERSION = "Новая версия:"
    const val SUCCESS = "Успех"
}

object Migration {
    const val CREATE_MIGRATION_TITLE = "Создать миграцию"
    const val MIGRATION_CREATED = "Миграция для версии %s создана."
    const val MIGRATION_CREATE_ERROR = "Ошибка создания миграции %s: %s"

    const val CHANGELOG_PATH = "src/main/resources/db.changelog"
    const val CHANGELOG_DEFAULT_VERSION = "1.0.0"
    const val MIGRATION_DIRECTORY = "db"
    const val CHANGELOG_MASTER_FILENAME = "db.changelog-master.yaml"
    const val USER_NAME_TEMPLATE = "user.name"
    const val MAPPING_PAGE_TITLE = "Настройки мапинга полей"
    const val MAPPING_ADD_NEW = "Добавить мапинг"
    const val MAPPING_DELETE = "Удалить мапинг"

    const val VERSION_REGEXP = """^(\d+)\.(\d+)\.(\d+)$"""

    object Templates {
        const val CHANGELOG_CONTENT = """
            databaseChangeLog:
              - changeSet:
                  id: %s
                  author: %s
                  changes:
                    - tagDatabase:
                        tag: %s
                    - sqlFile:
                        encoding: utf8
                        path: %s
                        relativeToChangelogFile: true
                  rollback:
                    - sqlFile:
                        encoding: utf8
                        path: %s
                        relativeToChangelogFile: true
        """

        const val CHANGELOG_INCLUDE_CONTENT = """
  - include:
        file: %s
        relativeToChangelogFile: true
        """

        const val SCHEMA_CREATE = "create schema if not exists %s;"
        const val TABLE_CREATE_START = "create table %s.%s ("
        const val TABLE_CREATE_END = ");"
        const val TABLE_COMMENT = "comment on table %s.%s is '%s';"
        const val COLUMN_COMMENT = "comment on column %s.%s.%s is '%s';"
        const val PRIMARY_KEY_DEFAULT_UUID = "primary key default gen_random_uuid()"
        const val NOT_NULL = "not null"
        const val DEFAULT_CURRENT_TIMESTAMP = "default current_timestamp"
        const val TIMESTAMP_WITH_TIMEZONE = "timestamp with time zone"
        const val TIMESTAMP = "timestamp"
        const val DROP_TABLE = "drop table if exists %s.%s cascade;"
    }
}

object MigrationMapping {
    const val TABLE_ROW_HEIGHT = 22
    val TABLE_INTERCELL_SPACING = java.awt.Dimension(4, 2)
    const val SCROLLPANE_BORDER = 8
    const val BUTTONS_HGAP = 5
    const val BUTTONS_VGAP = 0
    val COLUMN_NAMES = arrayOf("Kotlin Type", "PostgreSQL Type")
    val DEFAULT_DATA = arrayOf(
        arrayOf("UUID", "uuid"),
        arrayOf("String", "text"),
        arrayOf("Long", "numeric"),
        arrayOf("Double", "numeric"),
        arrayOf("Boolean", "boolean"),
        arrayOf("Int", "numeric"),
        arrayOf("LocalDate", "timestamp"),
        arrayOf("LocalDateTime", "timestamp"),
        arrayOf("BigDecimal", "numeric"),
        arrayOf("OffsetDateTime", "timestamp with time zone"),
        arrayOf("Instant", "timestamp with time zone"),
    )
}

const val EMPTY_STRING = ""
