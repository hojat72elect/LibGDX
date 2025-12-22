**Moved this module to my own fork of LibGDX**

## HyperLap2D libGDX Typing Label Extension

HyperLap2D extension for libgdx runtime that adds [Typing Label - TextraTypist](https://github.com/tommyettinger/textratypist) support.

### Integration

#### Gradle
![maven-central](https://img.shields.io/maven-central/v/games.rednblack.hyperlap2d/libgdx-typinglabel-extension?color=blue&label=release)
![sonatype-nexus](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fgames%2Frednblack%2Fhyperlap2d%2Flibgdx-typinglabel-extension%2Fmaven-metadata.xml)

Extension needs to be included into your `core` project.
```groovy
dependencies {
    api "com.github.tommyettinger:textratypist:$textratypistVersion"
    api "games.rednblack.hyperlap2d:libgdx-typinglabel-extension:$h2dTypingLabelExtension"
}
```

#### Maven
```xml
<dependency>
  <groupId>games.rednblack.hyperlap2d</groupId>
  <artifactId>libgdx-typinglabel-extension</artifactId>
  <version>0.1.6</version>
  <type>pom</type>
</dependency>
```

**Typing Label Runtime compatibility**

| HyperLap2D | Typing Label           |
|------------|------------------------|
| 0.1.6      | 6be1236 (TextraTypist) |
| 0.1.5      | 6be1236 (TextraTypist) |
| 0.1.4      | 1.3.0 (Typing Labels)  |

