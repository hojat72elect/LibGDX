**Moved this module to my own fork of LibGDX**

## HyperLap2D libGDX Runtime

HyperLap2D runtime for libGDX framework.

### Integration

#### Gradle
![maven-central](https://img.shields.io/maven-central/v/games.rednblack.hyperlap2d/runtime-libgdx?color=blue&label=release)
![sonatype-nexus](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fgames%2Frednblack%2Fhyperlap2d%2Fruntime-libgdx%2Fmaven-metadata.xml)

Runtime needs to be included into your `core` project.
```groovy
dependencies {
    //HyperLap2D Runtime
    api "games.rednblack.hyperlap2d:runtime-libgdx:$h2dVersion"

    //Mandatory
    api "com.badlogicgames.gdx:gdx:$gdxVersion"
    api "com.badlogicgames.gdx:gdx-box2d:$gdxVersion"
    api "com.badlogicgames.gdx:gdx-freetype:$gdxVersion"
    api "net.onedaybeard.artemis:artemis-odb:$artemisVersion"
}
```

#### Maven
```xml
<dependency>
  <groupId>games.rednblack.hyperlap2d</groupId>
  <artifactId>runtime-libgdx</artifactId>
  <version>0.1.6</version>
  <type>pom</type>
</dependency>
```

### Support

**Compatibility Table**

| HyperLap2D | libGDX | Artemis |
|------------|--------|---------|
| 0.1.6      | 1.14.0 | 2.3.0   |
| 0.1.5      | 1.13.1 | 2.3.0   |
| 0.1.4      | 1.12.1 | 2.3.0   |
| 0.1.3      | 1.12.0 | 2.3.0   |

You can learn how to use runtime in [Wiki](https://hyperlap2d.rednblack.games/wiki)

#### Overlap2D

HyperLap2D libGDX Runtime is a fork of [overlap2d-runtime-libgdx](https://github.com/UnderwaterApps/overlap2d-runtime-libgdx). A very special thanks to UnderwaterApps's Team and all of their Contributors for creating it, as without, HyperLap2D could never be possible.
Check out original: [`OVERLAP2D-AUTHORS`](https://github.com/rednblackgames/HyperLap2D/blob/master/OVERLAP2D-AUTHORS) and [`OVERLAP2D-CONTRIBUTORS`](https://github.com/rednblackgames/HyperLap2D/blob/master/OVERLAP2D-CONTRIBUTORS)
