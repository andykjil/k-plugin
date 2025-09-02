package com.dsg.kplugin.service.versioning.bumper

import com.dsg.kplugin.model.SemanticVersion

interface VersionBumper {
    fun bump(version: SemanticVersion): SemanticVersion
}