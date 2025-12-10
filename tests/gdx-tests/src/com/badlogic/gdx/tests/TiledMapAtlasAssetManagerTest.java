package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.BaseTiledMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.tests.utils.OrthoCamController;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;

public class TiledMapAtlasAssetManagerTest extends GdxTest {

    String errorMessage;
    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private OrthoCamController cameraController;
    private AssetManager assetManager;
    private BitmapFont font;
    private SpriteBatch batch;
    private final String fileName = "data/maps/tiled-atlas-processed/test.tmx";
    private final String fileNameWithImageLayers = "data/maps/tiled-atlas-processed/test_w_imglayers.tmx";
    private final String fileNameWithCollectionImages = "data/maps/tiled-atlas-processed/test_w_imglayers_coi.tmx";
    private int mapType = 0;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, (w / h) * 10, 10);
        camera.zoom = 2;
        camera.update();

        cameraController = new OrthoCamController(camera);
        Gdx.input.setInputProcessor(cameraController);

        font = new BitmapFont();
        batch = new SpriteBatch();

        BaseTiledMapLoader.Parameters params = new BaseTiledMapLoader.Parameters();
        params.forceTextureFilters = true;
        params.textureMinFilter = TextureFilter.Linear;
        params.textureMagFilter = TextureFilter.Linear;

        assetManager = new AssetManager();
        assetManager.setErrorListener((asset, throwable) -> errorMessage = throwable.getMessage());

        assetManager.setLoader(TiledMap.class, new AtlasTmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load(fileName, TiledMap.class);
        assetManager.load(fileNameWithImageLayers, TiledMap.class);
        assetManager.load(fileNameWithCollectionImages, TiledMap.class);
    }

    @Override
    public void render() {
        ScreenUtils.clear(100f / 255f, 100f / 255f, 250f / 255f, 1f);
        camera.update();
        assetManager.update(16);
        if (renderer == null && assetManager.isLoaded(fileName)) {
            map = assetManager.get(fileName);
            renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
        } else if (renderer != null) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                if (mapType != 0) {
                    mapType = 0;
                    map = assetManager.get(fileName);
                    renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                if (mapType != 1) {
                    if (renderer instanceof Disposable) ((Disposable) renderer).dispose();
                    mapType = 1;
                    map = assetManager.get(fileNameWithImageLayers);
                    renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                if (mapType != 2) {
                    if (renderer instanceof Disposable) ((Disposable) renderer).dispose();
                    mapType = 2;
                    map = assetManager.get(fileNameWithCollectionImages);
                    renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
                }
            }

            renderer.setView(camera);
            renderer.render();
        }

        batch.begin();
        if (errorMessage != null) font.draw(batch, "ERROR : " + errorMessage, 10, 50);
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        font.draw(batch, "Press keys 1, 2 and 3 to toggle between a map with packed imagelayers.", 170, 20);
        batch.end();
    }
}
