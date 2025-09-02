const val MIGRATION_TITLE = "Создать миграцию"
const val CHANGELOG_PATH = "src/main/resources/db.changelog"
const val CHANGELOG_DEFAULT_VERSION = "1.0.0"
const val MIGRATION_CREATED = "Миграция для версии %s создана."
const val MIGRATION_CREATE_ERROR = "Ошибка создания миграции %s: %s"
const val VERSION_REGEXP = """^(\d+)\.(\d+)\.(\d+)$"""

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