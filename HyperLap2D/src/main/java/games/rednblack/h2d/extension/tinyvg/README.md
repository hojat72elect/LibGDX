**Moved this module to my own fork of LibGDX**

## HyperLap2D libGDX TinyVG Extension

HyperLap2D extension for libgdx runtime that adds [TinyVG](https://tinyvg.tech/) rendering support using [`gdx-tinyvg`](https://github.com/lyze237/gdx-TinyVG).

### Integration

#### Gradle
![maven-central](https://img.shields.io/maven-central/v/games.rednblack.hyperlap2d/libgdx-tinyvg-extension?color=blue&label=release)
![sonatype-nexus](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fgames%2Frednblack%2Fhyperlap2d%2Flibgdx-tinyvg-extension%2Fmaven-metadata.xml)

Extension needs to be included into your `core` project.
```groovy
dependencies {
    //See https://github.com/lyze237/gdx-TinyVG#installation
    api "com.github.lyze237:gdx-TinyVG:$gdxTinyVGVersion"
    
    api "games.rednblack.hyperlap2d:libgdx-tinyvg-extension:$h2dTinyVGExtension"
}
```

#### Maven
```xml
<dependency>
  <groupId>games.rednblack.hyperlap2d</groupId>
  <artifactId>libgdx-tinyvg-extension</artifactId>
  <version>0.1.6</version>
  <type>pom</type>
</dependency>
```

**TinyVG Runtime compatibility**

| HyperLap2D | gdx-TinyVG         |
|------------| ------------------ |
| 0.1.6      | 7a8927633e         |
| 0.1.5      | 7a8927633e         |
| 0.1.4      | 7a8927633e         |
