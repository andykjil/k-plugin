package com.dsg.kplugin.service.filegenerator

import com.dsg.kplugin.service.migration.generator.SqlGenerator
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class SqlGeneratorTest : FeatureSpec({

    val generator = SqlGenerator()

    feature("SqlGenerator") {

        scenario("fileName returns sql file name with prefix") {
            generator.fileName("1.0.0", "DSG-1234") shouldBe "DSG-1234.sql"
        }

        scenario("generateContent returns SQL migration header with version") {
            val version = "2.3.4"
            val user = "test-user"
            val sqlFile = "DSG-1234.sql"
            val rollbackFile = "DSG-1234-rollback.sql"

            val content = generator.generateContent(
                newVersion = version,
                userName = user,
                sqlFileName = sqlFile,
                rollbackFileName = rollbackFile,
            )

            content shouldContain "-- SQL миграции для версии $version"
        }
    }
})
