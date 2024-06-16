package br.com.rodrigogurgel.playground.architecture

internal val JAVA_PACKAGES = arrayOf(
    "java..",
)

internal val KOTLIN_PACKAGES = arrayOf(
    "kotlin..",
    "kotlinx..",
)

internal val CORE_PACKAGES = arrayOf(
    "com.github.michaelbull..",
    "org.jetbrains..",
    *JAVA_PACKAGES,
    *KOTLIN_PACKAGES,
)

internal val COMMONS_PACKAGES = arrayOf(
    "org.apache..",
    "org.slf4j..",
    "org.springframework..",
    "io.micrometer..",
    *CORE_PACKAGES,
    *JAVA_PACKAGES,
    *KOTLIN_PACKAGES,
)
