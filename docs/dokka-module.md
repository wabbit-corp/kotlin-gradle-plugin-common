# Module kotlin-gradle-plugin-common

Shared Gradle-side helper library for Wabbit Kotlin compiler-plugin Gradle plugins.

This module is intended for Gradle plugin implementation projects. It centralizes Kotlin-line
compiler-plugin artifact version calculation, Kotlin Gradle Plugin version lookup, and required
compiler-flag wiring.

Most application builds should not depend on this module directly. They should apply the published
Wabbit Gradle plugins instead.
