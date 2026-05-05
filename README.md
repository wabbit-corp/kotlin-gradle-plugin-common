# kotlin-gradle-plugin-common

![](./.meta/github-project-banner.png)

<p align=center>
    <img src="https://img.shields.io/maven-central/v/one.wabbit/kotlin-gradle-plugin-common" alt="Maven Central">
    <img src="https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF" alt="Kotlin Multiplatform">
</p>

`kotlin-gradle-plugin-common` is a shared Gradle-side helper library for Wabbit Kotlin
compiler-plugin Gradle plugins.

It exists so plugin families such as `kotlin-acyclic`, `kotlin-typeclasses`, and
`kotlin-no-globals` can share version negotiation and compiler-option wiring instead of duplicating
that code in every Gradle plugin repository.

## Who This Is For

This module is for maintainers of Wabbit compiler-plugin Gradle integrations.

Application builds should not depend on it directly. Normal consumers should apply one of the
published Gradle plugins instead:

- `id("one.wabbit.acyclic")`
- `id("one.wabbit.typeclass")`
- `id("one.wabbit.no-globals")`

## 🚀 Installation

Use this dependency from a Gradle plugin implementation project:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("one.wabbit:kotlin-gradle-plugin-common:0.1.0")
}
```

The generated build for this repository compiles against:

- `gradleApi()`
- `org.jetbrains.kotlin:kotlin-gradle-plugin`

so consumers are expected to be Gradle plugin projects already.

## 🚀 Usage

In a Gradle plugin implementation, use the shared helpers to derive the Kotlin-line-specific
compiler-plugin artifact version and add required compiler flags:

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
    implementation("one.wabbit:kotlin-gradle-plugin-common:0.1.0")
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

That is the intended use case: plugin-implementation projects share one helper instead of each
repository re-implementing version negotiation and compiler-flag wiring slightly differently.

## Version Suffixes

Wabbit compiler plugins publish Kotlin-line-specific compiler artifacts. The helper:

```kotlin
compilerPluginArtifactVersion("0.1.0", "2.3.10")
```

returns:

```text
0.1.0-kotlin-2.3.10
```

Snapshot versions keep the `+dev-SNAPSHOT` suffix after the Kotlin-line marker.

## Status

This library is pre-1.0 and intended for Wabbit-maintained Gradle plugins. The API is intentionally
small, but it can still evolve while the surrounding compiler-plugin family settles.

## Documentation

- [User guide](docs/user-guide.md)
- [API reference notes](docs/api-reference.md)
- [Troubleshooting](docs/troubleshooting.md)
- [Development](docs/development.md)

Generated API docs can be built locally with Dokka. See [API reference notes](docs/api-reference.md)
for the command.

## Source Compatibility

- Kotlin/JVM target: JDK 21
- Kotlin compatibility line in the generated build: `2.3.10`

## Release Notes

- [CHANGELOG.md](CHANGELOG.md)

## Related Repositories

- `kotlin-acyclic`
- `kotlin-typeclasses`
- `kotlin-no-globals`

## Licensing

This project is licensed under the GNU Affero General Public License v3.0 (AGPL-3.0) for open
source use.

For commercial use, contact Wabbit Consulting Corporation at `wabbit@wabbit.one`.

## Contributing

Contributions are governed by the repository contribution policy and the Wabbit CLA. See
`CONTRIBUTING.md` and the files under `legal/`.
