# Development

## Build

```bash
./dev build kotlin-gradle-plugin-common
```

The project is a JVM Gradle-plugin support library targeting JDK 21.

## Documentation Checks

```bash
./dev verify docs kotlin-gradle-plugin-common
./gradlew -p kotlin-gradle-plugin-common dokkaGeneratePublicationHtml
```

Public helpers should have KDoc that explains Gradle configuration timing, failure behavior, and
version-formatting details.

## Publication Dry Run

```bash
./dev publish --dry-run kotlin-gradle-plugin-common
```

Generated Gradle metadata comes from `root.clj`. Update the root project definition and rerun
`./dev setup --local kotlin-gradle-plugin-common` instead of editing generated Gradle files directly.
