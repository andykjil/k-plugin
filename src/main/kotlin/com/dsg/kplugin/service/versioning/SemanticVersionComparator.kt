package com.dsg.kplugin.service.versioning

import com.dsg.kplugin.model.SemanticVersion

class SemanticVersionComparator : VersionComparator {
    override fun maxVersion(versions: List<SemanticVersion>): SemanticVersion? {
        return versions.maxOrNull()
    }
}