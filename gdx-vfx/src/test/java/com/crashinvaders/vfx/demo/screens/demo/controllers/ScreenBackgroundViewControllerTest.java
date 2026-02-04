package com.crashinvaders.vfx.demo.screens.demo.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.github.czyzby.lml.parser.LmlData;
import com.github.czyzby.lml.parser.LmlParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ScreenBackgroundViewControllerTest {

    private LmlParser lmlParser;
    private LmlData lmlData;
    private AssetManager assets;
    private ScreenBackgroundViewController controller;

    @Before
    public void setUp() {
        Gdx.app = mock(Application.class);
        when(Gdx.app.getType()).thenReturn(Application.ApplicationType.Desktop);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;
        Gdx.graphics = mock(Graphics.class);
        Gdx.files = mock(Files.class);

        lmlParser = mock(LmlParser.class);
        lmlData = mock(LmlData.class);
        assets = mock(AssetManager.class);

        when(lmlParser.getData()).thenReturn(lmlData);

        controller = new ScreenBackgroundViewController(lmlParser, assets);
    }

    @Test
    public void testConstructorRegistersActionContainer() {
        verify(lmlData).addActionContainer(eq("ScreenBackgroundViewController"), eq(controller));
    }

    @Test
    public void testCreateCheckerboardDrawable() {
        Texture texture = mock(Texture.class);
        when(assets.get("bg-transparency-tile.png")).thenReturn(texture);
        when(texture.getWidth()).thenReturn(16);
        when(texture.getHeight()).thenReturn(16);

        Drawable drawable = controller.createCheckerboardDrawable();

        assertNotNull(drawable);
        assertTrue(drawable instanceof TiledDrawable);
        verify(assets).get("bg-transparency-tile.png");
    }
}
