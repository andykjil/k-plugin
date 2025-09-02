package com.dsg.kplugin.service.versioning.bumper

import com.dsg.kplugin.model.SemanticVersion

class MinorVersionBumper : VersionBumper {
    override fun bump(version: SemanticVersion): SemanticVersion {
        return SemanticVersion(version.major, version.minor + 1, 0)
    }
}
