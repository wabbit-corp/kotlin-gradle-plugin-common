// SPDX-License-Identifier: AGPL-3.0-or-later

package one.wabbit.gradleplugin.common

import org.gradle.api.GradleException
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.ListProperty
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

private const val SNAPSHOT_VERSION_SUFFIX = "+dev-SNAPSHOT"

/**
 * Builds the compiler-plugin artifact version for a specific Kotlin Gradle Plugin line.
 *
 * Wabbit compiler plugins publish Kotlin-line-specific artifacts. Release versions are suffixed as
 * `<base>-kotlin-<kotlinVersion>`, while development snapshots preserve the `+dev-SNAPSHOT` suffix
 * after the Kotlin-line marker.
 *
 * @param baseVersion the repository base version, optionally ending in `+dev-SNAPSHOT`.
 * @param kotlinVersion the Kotlin Gradle Plugin version used by the consuming Gradle plugin.
 * @return the compiler-plugin artifact version matching [kotlinVersion].
 */
fun compilerPluginArtifactVersion(baseVersion: String, kotlinVersion: String): String =
    if (baseVersion.endsWith(SNAPSHOT_VERSION_SUFFIX)) {
        "${baseVersion.removeSuffix(SNAPSHOT_VERSION_SUFFIX)}-kotlin-$kotlinVersion$SNAPSHOT_VERSION_SUFFIX"
    } else {
        "$baseVersion-kotlin-$kotlinVersion"
    }

/**
 * Reads the Kotlin Gradle Plugin version using a logger owned by [logOwner].
 *
 * This overload is convenient for Gradle plugin entry points that already have a plugin class and
 * want Gradle's Kotlin plugin version lookup to log under that class name.
 *
 * @param logOwner the class used to create the Gradle logger.
 * @return the current Kotlin Gradle Plugin version visible to the build.
 */
fun currentKotlinGradlePluginVersion(logOwner: Class<*>): String =
    currentKotlinGradlePluginVersion(Logging.getLogger(logOwner))

/**
 * Reads the Kotlin Gradle Plugin version using [logger] for diagnostics.
 *
 * @param logger the Gradle logger passed to Kotlin's version lookup helper.
 * @return the current Kotlin Gradle Plugin version visible to the build.
 */
fun currentKotlinGradlePluginVersion(logger: Logger): String = getKotlinPluginVersion(logger)

/**
 * Configures a Kotlin compilation to include a required compiler flag.
 *
 * The helper resolves the compilation's compile task, accesses its `freeCompilerArgs`, and delegates
 * to [addRequiredCompilerFlag]. Failures are wrapped in [GradleException] with messages that name the
 * affected compilation and plugin.
 *
 * @param kotlinCompilation the compilation whose compile task should receive the flag.
 * @param requiredCompilerFlag the compiler flag that must be present.
 * @param pluginDisplayName a human-readable plugin name used in Gradle error messages.
 * @throws GradleException if the compile task or compiler options cannot be accessed.
 */
fun configureRequiredCompilerFlag(
    kotlinCompilation: KotlinCompilation<*>,
    requiredCompilerFlag: String,
    pluginDisplayName: String,
) {
    val compileTaskProvider =
        runCatching { kotlinCompilation.compileTaskProvider }
            .getOrElse { error ->
                throw GradleException(
                    "Could not locate the Kotlin compile task for ${kotlinCompilation.target.project.path}:${kotlinCompilation.compilationName}; $pluginDisplayName requires $requiredCompilerFlag.",
                    error,
                )
            }
    compileTaskProvider.configure { task ->
        addRequiredCompilerFlag(
            freeCompilerArgs =
                runCatching { task.compilerOptions.freeCompilerArgs }
                    .getOrElse { error ->
                        throw GradleException(
                            "Could not access Kotlin compiler options for ${task.path}; $pluginDisplayName requires $requiredCompilerFlag.",
                            error,
                        )
                    },
            requiredCompilerFlag = requiredCompilerFlag,
            taskPath = task.path,
            pluginDisplayName = pluginDisplayName,
        )
    }
}

/**
 * Adds [requiredCompilerFlag] to [freeCompilerArgs] if it is not already present.
 *
 * This helper is intentionally idempotent, so Gradle plugins can call it from multiple configuration
 * paths without duplicating the compiler argument.
 *
 * @param freeCompilerArgs the Kotlin compiler argument list property to update.
 * @param requiredCompilerFlag the compiler flag that must be present.
 * @param taskPath the Gradle task path used in error messages.
 * @param pluginDisplayName a human-readable plugin name used in error messages.
 * @throws GradleException if the compiler argument property cannot be read or updated.
 */
fun addRequiredCompilerFlag(
    freeCompilerArgs: ListProperty<String>,
    requiredCompilerFlag: String,
    taskPath: String,
    pluginDisplayName: String,
) {
    runCatching {
            if (requiredCompilerFlag !in freeCompilerArgs.getOrElse(emptyList())) {
                freeCompilerArgs.add(requiredCompilerFlag)
            }
        }
        .getOrElse { error ->
            throw GradleException(
                "Could not add $requiredCompilerFlag to Kotlin compilation task $taskPath for $pluginDisplayName.",
                error,
            )
        }
}
