package com.dsg.kplugin.service.versioning

import com.dsg.kplugin.model.version.SemanticVersion

class SemanticVersionComparator : VersionComparator {
    override fun maxVersion(versions: List<SemanticVersion>): SemanticVersion? {
        return versions.maxOrNull()
    }
}
