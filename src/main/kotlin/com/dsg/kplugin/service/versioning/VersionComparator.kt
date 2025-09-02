package com.dsg.kplugin.service.versioning

import com.dsg.kplugin.model.SemanticVersion

interface VersionComparator {
    fun maxVersion(versions: List<SemanticVersion>): SemanticVersion?
}
