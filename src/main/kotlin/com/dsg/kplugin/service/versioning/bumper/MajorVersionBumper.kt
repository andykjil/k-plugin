package com.dsg.kplugin.service.versioning.bumper

import com.dsg.kplugin.model.SemanticVersion

class MajorVersionBumper : VersionBumper {
    override fun bump(version: SemanticVersion): SemanticVersion {
        return SemanticVersion(version.major + 1, 0, 0)
    }
}