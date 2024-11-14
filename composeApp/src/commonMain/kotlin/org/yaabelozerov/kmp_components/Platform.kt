package org.yaabelozerov.kmp_components

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform