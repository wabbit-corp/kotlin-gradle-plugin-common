# kotlin-gradle-plugin-common

`kotlin-gradle-plugin-common` is a shared helper library for Wabbit Kotlin compiler-plugin Gradle plugins.

It exists so plugin families such as `kotlin-acyclic`, `kotlin-typeclasses`, and `kotlin-no-globals` can share the same Gradle-side support code instead of copying small versioning and compiler-option helpers into every repository.

## Who This Is For

This module is primarily for maintainers of Wabbit compiler-plugin Gradle integrations.

Most users should not depend on it directly. Normal consumer builds should use one of the published Gradle plugins instead:

- `id("one.wabbit.acyclic")`
- `id("one.wabbit.typeclass")`
- `id("one.wabbit.no-globals")`

## Coordinates

- library artifact: `one.wabbit:kotlin-gradle-plugin-common:0.0.1`

## Status

This library is pre-1.0 and intended for maintainers of Wabbit Gradle plugins.

The API is small, but it is still allowed to evolve while the surrounding compiler-plugin family settles.

## What It Provides

The shared helpers currently cover:

- compiler-plugin artifact version calculation using the repository base version plus the active Kotlin line
- Kotlin Gradle Plugin version lookup
- common wiring for required compiler flags on Kotlin compilations

## Installation

This module is intended for Gradle plugin implementation projects, not for application code.

```kotlin
dependencies {
    implementation("one.wabbit:kotlin-gradle-plugin-common:0.0.1")
}
```

The generated build for this repository compiles against:

- `gradleApi()`
- `org.jetbrains.kotlin:kotlin-gradle-plugin`

so consumers are expected to be Gradle plugin projects already.

## Quick Start

In a Gradle plugin implementation, use the shared helpers to derive the Kotlin-line-specific compiler-plugin artifact version and add required compiler flags without duplicating the same boilerplate in each repository:

```kotlin
import one.wabbit.gradleplugin.common.compilerPluginArtifactVersion
import one.wabbit.gradleplugin.common.configureRequiredCompilerFlag
import one.wabbit.gradleplugin.common.currentKotlinGradlePluginVersion

val kotlinVersion = currentKotlinGradlePluginVersion(MyGradlePlugin::class.java)
val compilerPluginVersion =
    compilerPluginArtifactVersion(
        baseVersion = "0.0.1",
        kotlinVersion = kotlinVersion,
    )

dependencies {
    implementation("one.wabbit:kotlin-gradle-plugin-common:0.0.1")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}

kotlin.target.compilations.configureEach {
    configureRequiredCompilerFlag(
        kotlinCompilation = this,
        requiredCompilerFlag = "-Xcontext-parameters",
        pluginDisplayName = "My Compiler Plugin",
    )
}
```

That is the intended use case: plugin-implementation projects share one helper instead of each repository re-implementing version negotiation and compiler-flag wiring slightly differently.

## Documentation

- [API reference](docs/api-reference.md)
- API docs are currently generated locally with `./gradlew dokkaGenerate`, then published from `build/dokka/html/index.html`.
- Main source entry point: [`src/main/kotlin/one/wabbit/gradleplugin/common/CompilerPluginGradleSupport.kt`](src/main/kotlin/one/wabbit/gradleplugin/common/CompilerPluginGradleSupport.kt)

## Source Compatibility

- Kotlin/JVM target: JDK 21
- Kotlin compatibility line in the generated build: `2.3.10`

## Release Notes

- [`CHANGELOG.md`](CHANGELOG.md)

## Related Repositories

- `kotlin-acyclic`
- `kotlin-typeclasses`
- `kotlin-no-globals`
