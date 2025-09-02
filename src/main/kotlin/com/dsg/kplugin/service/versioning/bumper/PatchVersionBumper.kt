package com.dsg.kplugin.service.versioning.bumper

import com.dsg.kplugin.model.SemanticVersion

class PatchVersionBumper : VersionBumper {
    override fun bump(version: SemanticVersion): SemanticVersion {
        return SemanticVersion(version.major, version.minor, version.patch + 1)
    }
}
