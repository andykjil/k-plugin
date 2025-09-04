package com.dsg.kplugin.model

import com.dsg.kplugin.common.VERSION_REGEXP

data class SemanticVersion(val major: Int, val minor: Int, val patch: Int) : Comparable<SemanticVersion> {
    companion object {
        private val versionRegex = Regex(VERSION_REGEXP)

        fun parse(versionString: String): SemanticVersion? {
            val match = versionRegex.matchEntire(versionString) ?: return null
            val (maj, min, pat) = match.destructured
            return SemanticVersion(maj.toInt(), min.toInt(), pat.toInt())
        }
    }

    override fun compareTo(other: SemanticVersion): Int {
        return compareBy<SemanticVersion> { it.major }
            .thenBy { it.minor }
            .thenBy { it.patch }
            .compare(this, other)
    }

    override fun toString(): String = "$major.$minor.$patch"
}
