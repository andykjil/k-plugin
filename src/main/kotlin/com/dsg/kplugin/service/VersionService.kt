package com.dsg.kplugin.service

import CHANGELOG_DEFAULT_VERSION
import com.dsg.kplugin.model.SemanticVersion
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.versioning.SemanticVersionComparator
import com.dsg.kplugin.service.versioning.VersionComparator
import com.dsg.kplugin.service.versioning.bumper.MajorVersionBumper
import com.dsg.kplugin.service.versioning.bumper.MinorVersionBumper
import com.dsg.kplugin.service.versioning.bumper.PatchVersionBumper
import com.dsg.kplugin.service.versioning.bumper.VersionBumper
import com.intellij.openapi.vfs.VirtualFile


class VersionService {

    private val comparator: VersionComparator = SemanticVersionComparator()

    private fun getBumper(type: BumpType): VersionBumper = when (type) {
        BumpType.MAJOR -> MajorVersionBumper()
        BumpType.MINOR -> MinorVersionBumper()
        BumpType.PATCH -> PatchVersionBumper()
    }

    fun findLastChangelog(changelogDir: VirtualFile): String {
        val subDirs = changelogDir.children.filter { it.isDirectory }.map { it.name }
        val versions = subDirs.mapNotNull { SemanticVersion.parse(it) }
        val maxVersion = comparator.maxVersion(versions)
        return maxVersion?.toString() ?: CHANGELOG_DEFAULT_VERSION
    }

    fun bumpVersion(version: String, type: BumpType): String {
        val semanticVersion = SemanticVersion.parse(version) ?: SemanticVersion(0, 0, 0)
        val bumper = getBumper(type)
        val bumpedVersion = bumper.bump(semanticVersion)
        return bumpedVersion.toString()
    }
}
