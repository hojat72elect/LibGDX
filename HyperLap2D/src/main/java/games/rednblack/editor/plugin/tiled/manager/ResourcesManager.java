package games.rednblack.editor.plugin.tiled.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import games.rednblack.editor.plugin.tiled.TiledPlugin;
import games.rednblack.editor.plugin.tiled.view.SpineDrawable;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.h2d.extension.spine.SpineDataObject;
import games.rednblack.h2d.extension.spine.SpineItemType;

public class ResourcesManager {

    private final String RESOURCES_FILE_NAME = "tiled";
    private final SkeletonRenderer skeletonRenderer = new SkeletonRenderer();
    private final ObjectMap<String, Texture> textureCache = new ObjectMap<>();
    private final ObjectMap<String, SpineDrawable> spineDrawableCache = new ObjectMap<>();
    private final TiledPlugin tiledPlugin;
    private TextureAtlas textureAtlas;

    public ResourcesManager(TiledPlugin tiledPlugin) {
        this.tiledPlugin = tiledPlugin;
        skeletonRenderer.setPremultipliedAlpha(true);

        init();
    }

    private void init() {
        FileHandle atlasTempFile = getResourceFileFromJar(RESOURCES_FILE_NAME + ".atlas");
        FileHandle pngTempFile = getResourceFileFromJar(RESOURCES_FILE_NAME + ".png");
        textureAtlas = new TextureAtlas(atlasTempFile);
        atlasTempFile.file().deleteOnExit();
        pngTempFile.file().deleteOnExit();

        loadTexture("tile-cursor");
        loadTexture("tile-eraser-cursor");
    }

    private void loadTexture(String name) {
        FileHandle file = getResourceFileFromJar(name + ".png");
        file.file().deleteOnExit();
        textureCache.put(name, new Texture(file));
    }

    private FileHandle getResourceFileFromJar(String fileName) {
        File tempFile = new File(tiledPlugin.getAPI().getCacheDir() + File.separator + fileName);

        try {
            InputStream in = getClass().getResourceAsStream("/pack/" + fileName);
            if (in == null) {
                in = getClass().getResourceAsStream("/" + fileName);
            }
            if (in == null) {
                File devFile = new File("assets/pack/" + fileName);
                if (devFile.exists()) {
                    in = new FileInputStream(devFile);
                } else {
                    // Try with absolute path if we are deeper
                    File absoluteFile = new File("d:/Dev/Repositories/LibGDX/HyperLap2D/assets/pack/" + fileName);
                    if (absoluteFile.exists()) {
                        in = new FileInputStream(absoluteFile);
                    }
                }
            }
            // Fallback: Check inside JAR plugins
            if (in == null) {
                File pluginsDir = new File("assets/plugins");
                if (!pluginsDir.exists()) {
                    pluginsDir = new File("d:/Dev/Repositories/LibGDX/HyperLap2D/assets/plugins");
                }

                if (pluginsDir.exists() && pluginsDir.isDirectory()) {
                    File[] jars = pluginsDir
                            .listFiles((dir, name) -> name.startsWith("plugin-tiled") && name.endsWith(".jar"));
                    if (jars != null) {
                        for (File jarFile : jars) {
                            try {
                                JarFile jar = new JarFile(jarFile);
                                // Based on jar scan, they are at root
                                JarEntry entry = jar.getJarEntry(fileName);
                                if (entry == null)
                                    entry = jar.getJarEntry("pack/" + fileName);

                                if (entry != null) {
                                    in = jar.getInputStream(entry);
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            if (in == null) {
                System.err.println("CRITICAL: Could not find resource: " + fileName);
                System.err.println("CWD: " + new File(".").getAbsolutePath());
                // Avoid NPE
                return null;
            }

            FileOutputStream out = new FileOutputStream(tempFile);
            ByteStreams.copy(in, out);
            if (in != null)
                in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FileHandle(tempFile);
    }

    public Texture getTexture(String name) {
        return textureCache.get(name);
    }

    public TextureRegion getTextureRegion(String name, int type) {
        TextureRegion region = textureAtlas.findRegion(name); // try to get region from plugin assets
        if (region == null) { // take the region from hyperlap assets
            switch (type) {
                case EntityFactory.IMAGE_TYPE:
                    region = tiledPlugin.getAPI().getSceneLoader().getRm().getTextureRegion(name);
                    break;
                case EntityFactory.SPRITE_TYPE:
                    region = tiledPlugin.getAPI().getSceneLoader().getRm().getSpriteAnimation(name).get(0);
                    break;
            }
        }
        return region;
    }

    public SpineDrawable getSpineDrawable(String name) {
        if (spineDrawableCache.get(name) == null) {
            SpineDataObject spineDataObject = (SpineDataObject) tiledPlugin.getAPI().getSceneLoader().getRm()
                    .getExternalItemType(SpineItemType.SPINE_TYPE, name);
            SkeletonData skeletonData = spineDataObject.skeletonData;
            Skeleton skeleton = new Skeleton(skeletonData);

            spineDrawableCache.put(name, new SpineDrawable(skeleton, skeletonRenderer));
        }

        return spineDrawableCache.get(name);
    }

    public NinePatch getPluginNinePatch(String name) {
        TextureAtlas.AtlasRegion region = textureAtlas.findRegion(name);
        if (region == null)
            return null;
        int[] splits = region.findValue("split");
        return new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);
    }
}
