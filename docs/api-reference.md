# API Reference

Generated Dokka API docs can be built with:

```bash
./gradlew dokkaGeneratePublicationHtml
```

## Public Entry Points

- `compilerPluginArtifactVersion(baseVersion, kotlinVersion)`: returns the compiler-plugin artifact
  version for a repository base version and Kotlin Gradle Plugin version.
- `currentKotlinGradlePluginVersion(logOwner)`: reads the active Kotlin Gradle Plugin version and
  logs under the provided class.
- `currentKotlinGradlePluginVersion(logger)`: reads the active Kotlin Gradle Plugin version using an
  explicit Gradle logger.
- `configureRequiredCompilerFlag(kotlinCompilation, requiredCompilerFlag, pluginDisplayName)`:
  configures a Kotlin compilation's compile task so the requested flag is present.
- `addRequiredCompilerFlag(freeCompilerArgs, requiredCompilerFlag, taskPath, pluginDisplayName)`:
  idempotently appends a flag to a compiler-argument list property.

## Version Formatting

Release base versions are formatted as:

```text
<baseVersion>-kotlin-<kotlinVersion>
```

Snapshot base versions ending in `+dev-SNAPSHOT` are formatted as:

```text
<baseVersionWithoutSnapshot>-kotlin-<kotlinVersion>+dev-SNAPSHOT
```

## Failure Behavior

Compiler-flag helpers throw `GradleException` when a compilation task or compiler option property
cannot be accessed. Error messages include the task or compilation and plugin display name so build
failures point at the plugin requiring the flag.
