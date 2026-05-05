# Troubleshooting

## The Compiler Plugin Artifact Version Does Not Resolve

Check that the compiler-plugin artifact was published for the Kotlin Gradle Plugin line returned by
`currentKotlinGradlePluginVersion`. A base version of `0.1.0` and Kotlin version `2.3.10` maps to:

```text
0.1.0-kotlin-2.3.10
```

For snapshots, confirm that the published artifact keeps `+dev-SNAPSHOT` at the end of the derived
version.

## The Required Compiler Flag Is Missing

Call `configureRequiredCompilerFlag` from a point where Kotlin compilations are available and before
the compile task executes. The helper configures the compilation's compile task provider and should
be run during Gradle configuration, not from task action code.

## GradleException Mentions Compiler Options

The helper wraps task and compiler-option access failures in `GradleException` so consuming plugins
fail with actionable messages. Inspect the task path and plugin display name in the error message to
identify which plugin attempted to add the flag.
