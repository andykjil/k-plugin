package com.dsg.kplugin.common

const val PLUGIN_NAME = "k plugin"
const val CREATE_MIGRATION_TITLE = "Создать миграцию"
const val TOOL_BAR_TOOLTIP = "k plugin"
const val CHANGELOG_PATH = "src/main/resources/db.changelog"
const val CHANGELOG_DEFAULT_VERSION = "1.0.0"
const val MIGRATION_CREATED = "Миграция для версии %s создана."
const val MIGRATION_CREATE_ERROR = "Ошибка создания миграции %s: %s"
const val VERSION_REGEXP = """^(\d+)\.(\d+)\.(\d+)$"""
const val USER_NAME_TEMPLATE = "user.name"
const val MIGRATION_DIRECTORY = "db"
const val CHANGELOG_MASTER_FILENAME = "db.changelog-master.yaml"
const val USER_CUSTOM_CHECKBOX_TEXT = "Использовать своё имя пользователя"
const val K_PLUGIN_SETTINGS = "Настройки k plugin"
const val USER_NAME_TEXT = "Имя пользователя:"
const val OPEN_SETTINGS = "Открыть настройки"
const val TOOLBAR_LOGS_TAB_TITLE = "Логи"
const val TOOLBAR_LOGS_TITLE = "Логи"
const val TOOLBAR_MIGRATIONS_TAB_TITLE = "Миграции"
const val LAST_VERSION = "Последняя версия:"
const val NEW_VERSION = "Новая версия:"
const val SUCCESS = "Успех"

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
