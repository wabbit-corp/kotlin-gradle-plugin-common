# API Reference

`kotlin-gradle-plugin-common` currently publishes one small helper surface:

- [`CompilerPluginGradleSupport.kt`](../src/main/kotlin/one/wabbit/gradleplugin/common/CompilerPluginGradleSupport.kt)

The public entry points are:

- `compilerPluginArtifactVersion(baseVersion, kotlinVersion)`
- `currentKotlinGradlePluginVersion(logOwner)`
- `currentKotlinGradlePluginVersion(logger)`
- `configureRequiredCompilerFlag(kotlinCompilation, requiredCompilerFlag, pluginDisplayName)`
- `addRequiredCompilerFlag(freeCompilerArgs, requiredCompilerFlag, taskPath, pluginDisplayName)`

For rendered API docs, run:

```bash
./gradlew dokkaGenerate
```

Then open:

```text
build/dokka/html/index.html
```
