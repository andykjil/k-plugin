package com.dsg.kplugin.model

import com.dsg.kplugin.model.version.SemanticVersion
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe

class SemanticVersionTest : FeatureSpec({

    feature("Parsing semantic versions") {

        scenario("Parses valid version string") {
            val version = SemanticVersion.parse("1.2.3")
            version shouldBe SemanticVersion(1, 2, 3)
        }

        scenario("Returns null for invalid version string") {
            SemanticVersion.parse("abc") shouldBe null
            SemanticVersion.parse("1.2") shouldBe null
            SemanticVersion.parse("1.2.3.4") shouldBe null
        }
    }

    feature("Comparisons") {

        scenario("Major version comparison") {
            val v1 = SemanticVersion(2, 0, 0)
            val v2 = SemanticVersion(1, 9, 9)
            (v1 > v2) shouldBe true
        }

        scenario("Minor version comparison") {
            val v1 = SemanticVersion(1, 5, 0)
            val v2 = SemanticVersion(1, 4, 9)
            (v1 > v2) shouldBe true
        }

        scenario("Patch version comparison") {
            val v1 = SemanticVersion(1, 2, 5)
            val v2 = SemanticVersion(1, 2, 3)
            (v1 > v2) shouldBe true
        }

        scenario("Equality") {
            val v1 = SemanticVersion(1, 2, 3)
            val v2 = SemanticVersion(1, 2, 3)
            (v1 == v2) shouldBe true
        }
    }

    feature("String representation") {

        scenario("toString returns correctly formatted version") {
            val v = SemanticVersion(1, 2, 3)
            v.toString() shouldBe "1.2.3"
        }
    }
})
