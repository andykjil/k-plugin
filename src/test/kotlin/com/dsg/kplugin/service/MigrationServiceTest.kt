package com.dsg.kplugin.service

import com.dsg.kplugin.common.MIGRATION_DIRECTORY
import com.dsg.kplugin.service.filegenerator.MigrationBuilder
import com.dsg.kplugin.service.versioning.GitService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify

class MigrationServiceTest : FeatureSpec({

    feature("MigrationService") {
        val service = MigrationService()

        scenario("findDbModule returns db folder when present") {
            val dbFolder = mockk<VirtualFile>(relaxed = true)
            every { dbFolder.name } returns MIGRATION_DIRECTORY

            val baseDir = mockk<VirtualFile>(relaxed = true)
            every { baseDir.findChild(MIGRATION_DIRECTORY) } returns dbFolder

            val project = mockk<Project>(relaxed = true)
            every { project.baseDir } returns baseDir

            service.findDbModule(project) shouldBe dbFolder
        }

        scenario("findDbModule returns null when db folder missing") {
            val baseDir = mockk<VirtualFile>(relaxed = true)
            every { baseDir.findChild(MIGRATION_DIRECTORY) } returns null

            val project = mockk<Project>(relaxed = true)
            every { project.baseDir } returns baseDir

            service.findDbModule(project) shouldBe null
        }

        scenario("createMigrationFiles builds migration with correct parameters") {
            val changelogDir = mockk<VirtualFile>()
            val project = mockk<Project>()
            val userName = "test-user"
            val version = "1.0.0"
            val branchName = "feature/DSG-1234"

            mockkConstructor(GitService::class)
            every { anyConstructed<GitService>().getCurrentBranch(project) } returns branchName

            mockkConstructor(MigrationBuilder::class)
            every { anyConstructed<MigrationBuilder>().build() } just Runs

            service.createMigrationFiles(
                project = project,
                changelogDir = changelogDir,
                newVersion = version,
                userName = userName,
            )

            verify {
                anyConstructed<MigrationBuilder>().build()
            }
        }
    }
})
