# User Guide

## Intended Use

Use `kotlin-gradle-plugin-common` from Gradle plugin implementation projects that need to wire a
Wabbit Kotlin compiler plugin into Kotlin compilations.

Application projects should apply the end-user Gradle plugin instead of depending on this helper
library directly.

## Kotlin-Line Compiler Plugin Versions

Wabbit compiler-plugin artifacts are published per Kotlin Gradle Plugin line. Use
`compilerPluginArtifactVersion` to derive the compiler-plugin artifact version from the repository
base version and the active Kotlin version:

```kotlin
import one.wabbit.gradleplugin.common.compilerPluginArtifactVersion

val version = compilerPluginArtifactVersion(
    baseVersion = "0.1.0",
    kotlinVersion = "2.3.10",
)

check(version == "0.1.0-kotlin-2.3.10")
```

Snapshot base versions ending in `+dev-SNAPSHOT` keep the snapshot suffix:

```kotlin
val snapshot = compilerPluginArtifactVersion(
    baseVersion = "0.1.0+dev-SNAPSHOT",
    kotlinVersion = "2.3.10",
)

check(snapshot == "0.1.0-kotlin-2.3.10+dev-SNAPSHOT")
```

## Reading the Kotlin Gradle Plugin Version

Use `currentKotlinGradlePluginVersion` when a Gradle plugin needs the Kotlin line currently visible
to the build:

```kotlin
import one.wabbit.gradleplugin.common.currentKotlinGradlePluginVersion

val kotlinVersion = currentKotlinGradlePluginVersion(MyPlugin::class.java)
```

The overload accepting `Logger` is useful when the plugin already has a Gradle logger.

## Required Compiler Flags

Use `configureRequiredCompilerFlag` from Kotlin compilation configuration code:

```kotlin
import one.wabbit.gradleplugin.common.configureRequiredCompilerFlag

kotlin.target.compilations.configureEach {
    configureRequiredCompilerFlag(
        kotlinCompilation = this,
        requiredCompilerFlag = "-Xcontext-parameters",
        pluginDisplayName = "My Compiler Plugin",
    )
}
```

The helper is idempotent. If the flag is already present, it is not added again.
