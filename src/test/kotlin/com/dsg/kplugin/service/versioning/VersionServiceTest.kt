package com.dsg.kplugin.service.versioning

import CHANGELOG_DEFAULT_VERSION
import com.dsg.kplugin.model.enums.BumpType
import com.intellij.openapi.vfs.VirtualFile
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkClass

class VersionServiceTest : FeatureSpec() {

    val versionService = VersionService()

    init {
        feature("bump version") {
            scenario("bump major version") {
                val newVersion = versionService.bumpVersion("1.2.3", BumpType.MAJOR)
                newVersion shouldBe "2.0.0"
            }
            scenario("bump minor version") {
                val newVersion = versionService.bumpVersion("1.2.3", BumpType.MINOR)
                newVersion shouldBe "1.3.0"
            }
            scenario("bump patch version") {
                val newVersion = versionService.bumpVersion("1.2.3", BumpType.PATCH)
                newVersion shouldBe "1.2.4"
            }
            scenario("bump version with invalid input") {
                val newVersion = versionService.bumpVersion("invalid.version", BumpType.PATCH)
                newVersion shouldBe "0.0.1"
            }
            scenario("bump version with empty input") {
                val newVersion = versionService.bumpVersion("", BumpType.MINOR)
                newVersion shouldBe "0.1.0"
            }
            scenario("bump version with null input") {
                val newVersion = versionService.bumpVersion("null", BumpType.MAJOR)
                newVersion shouldBe "1.0.0"
            }
        }
        feature("Finding last changelog") {

            scenario("Returns the latest version from mocked subdirectories") {
                val sub1 = mockkClass(VirtualFile::class)
                val sub2 = mockkClass(VirtualFile::class)
                val sub3 = mockkClass(VirtualFile::class)

                every { sub1.isDirectory } returns true
                every { sub1.name } returns "1.0.0"

                every { sub2.isDirectory } returns true
                every { sub2.name } returns "1.2.3"

                every { sub3.isDirectory } returns true
                every { sub3.name } returns "1.1.0"

                val root = mockkClass(VirtualFile::class)
                every { root.children } returns arrayOf(sub1, sub2, sub3)

                versionService.findLastChangelog(root) shouldBe "1.2.3"
            }

            scenario("Returns default version if no subdirectories") {
                val root = mockkClass(VirtualFile::class)
                every { root.children } returns emptyArray()

                versionService.findLastChangelog(root) shouldBe CHANGELOG_DEFAULT_VERSION
            }

            scenario("Ignores non-directory children") {
                val file = mockkClass(VirtualFile::class)
                every { file.isDirectory } returns false
                every { file.name } returns "not_a_dir"

                val root = mockkClass(VirtualFile::class)
                every { root.children } returns arrayOf(file)

                versionService.findLastChangelog(root) shouldBe CHANGELOG_DEFAULT_VERSION
            }
        }
    }
}
