package com.dsg.kplugin.service.versioning.bumper

import com.dsg.kplugin.model.version.SemanticVersion

interface VersionBumper {
    fun bump(version: SemanticVersion): SemanticVersion
}
