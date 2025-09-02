package com.dsg.kplugin.service.filegenerator

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class RollbackGeneratorTest : FeatureSpec({

    val generator = RollbackGenerator()

    feature("RollbackGenerator") {

        scenario("fileName returns rollback SQL file name with prefix") {
            generator.fileName("1.0.0", "DSG-1234") shouldBe "DSG-1234-rollback.sql"
        }

        scenario("generateContent returns SQL rollback header with version") {
            val version = "1.2.3"
            val user = "test-user"
            val sqlFile = "DSG-1234.sql"
            val rollbackFile = "DSG-1234-rollback.sql"

            val content = generator.generateContent(
                newVersion = version,
                userName = user,
                sqlFileName = sqlFile,
                rollbackFileName = rollbackFile,
            )

            content shouldContain "-- SQL отката для версии $version"
        }
    }
})
