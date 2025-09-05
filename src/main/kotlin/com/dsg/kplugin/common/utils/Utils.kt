package com.dsg.kplugin.common.utils

fun String.camelToSnakeCase(): String {
    return this
        .replace(Regex("([a-z\\d])([A-Z])"), "$1_$2")
        .replace(Regex("([A-Z]+)([A-Z][a-z])"), "$1_$2")
        .lowercase()
}
