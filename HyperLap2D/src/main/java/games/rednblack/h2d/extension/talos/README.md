**Moved this module to my own fork of LibGDX**

## HyperLap2D libGDX Talos Extension

HyperLap2D extension for libgdx runtime that adds [Talos](https://talosvfx.com) rendering support.

Currently based on custom [legacy fork](https://github.com/rednblackgames/talos-legacy) to maintain compatibility with v1 features.

### Integration

#### Gradle
![maven-central](https://img.shields.io/maven-central/v/games.rednblack.hyperlap2d/libgdx-talos-extension?color=blue&label=release)
![sonatype-nexus](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fgames%2Frednblack%2Fhyperlap2d%2Flibgdx-talos-extension%2Fmaven-metadata.xml)

Extension needs to be included into your `core` project.
```groovy
dependencies {
    api "games.rednblack.talos:runtime-libgdx:$talosVersion"
    api "games.rednblack.hyperlap2d:libgdx-talos-extension:$h2dTalosExtension"
}
```

#### Maven
```xml
<dependency>
  <groupId>games.rednblack.hyperlap2d</groupId>
  <artifactId>libgdx-talos-extension</artifactId>
  <version>0.1.6</version>
  <type>pom</type>
</dependency>
```

**Talos Runtime compatibility**

| HyperLap2D | Talos |
|------------|-------|
| 0.1.6      | 1.5.2 |
| 0.1.5      | 1.5.1 |
| 0.1.4      | 1.5.0 |
