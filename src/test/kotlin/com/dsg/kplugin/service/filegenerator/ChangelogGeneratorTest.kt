package com.dsg.kplugin.service.filegenerator

import com.dsg.kplugin.service.migration.generator.ChangelogGenerator
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe

class ChangelogGeneratorTest : FeatureSpec({

    val generator = ChangelogGenerator()

    feature("ChangelogGenerator") {

        scenario("Generates correct file name") {
            generator.fileName("1.2.3", "ignored") shouldBe "changelog-1.2.3.yml"
        }

        scenario("Generates content with correct placeholders replaced") {
            val newVersion = "1.2.3"
            val userName = "test-user"
            val sqlFile = "DSG-1234.sql"
            val rollbackFile = "DSG-1234-rollback.sql"

            val content = generator.generateContent(
                newVersion = newVersion,
                userName = userName,
                sqlFileName = sqlFile,
                rollbackFileName = rollbackFile,
            )

            content.contains(newVersion) shouldBe true
            content.contains(userName) shouldBe true
            content.contains(sqlFile) shouldBe true
            content.contains(rollbackFile) shouldBe true
        }
    }
})
