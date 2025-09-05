package com.dsg.kplugin.service.versioning

import com.dsg.kplugin.model.version.SemanticVersion

interface VersionComparator {
    fun maxVersion(versions: List<SemanticVersion>): SemanticVersion?
}
