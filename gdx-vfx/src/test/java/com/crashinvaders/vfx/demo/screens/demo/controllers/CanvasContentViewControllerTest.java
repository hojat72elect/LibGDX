package com.crashinvaders.vfx.demo.screens.demo.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.crashinvaders.vfx.common.lml.CommonLmlParser;
import com.crashinvaders.vfx.common.viewcontroller.ViewControllerManager;
import com.crashinvaders.vfx.scene2d.VfxWidgetGroup;
import com.github.czyzby.lml.parser.LmlData;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class CanvasContentViewControllerTest {

    private ViewControllerManager viewControllers;
    private CommonLmlParser lmlParser;
    private AssetManager assets;
    private CanvasContentViewController controller;

    @Before
    public void setUp() {
        Gdx.app = mock(Application.class);
        when(Gdx.app.getType()).thenReturn(Application.ApplicationType.Desktop);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;
        Gdx.graphics = mock(Graphics.class);
        Gdx.files = mock(Files.class);

        viewControllers = mock(ViewControllerManager.class);
        lmlParser = mock(CommonLmlParser.class);
        assets = mock(AssetManager.class);
        LmlData lmlData = mock(LmlData.class);
        when(lmlParser.getData()).thenReturn(lmlData);

        controller = new CanvasContentViewController(viewControllers, lmlParser, assets);
    }

    @Test
    public void testOnMatchWidgetSizeChanged() throws Exception {
        VfxWidgetGroup vfxGroup = mock(VfxWidgetGroup.class);
        Field field = CanvasContentViewController.class.getDeclaredField("vfxGroup");
        field.setAccessible(true);
        field.set(controller, vfxGroup);

        CheckBox checkBox = mock(CheckBox.class);
        when(checkBox.isChecked()).thenReturn(true);

        controller.onMatchWidgetSizeChanged(checkBox);

        verify(vfxGroup).setMatchWidgetSize(true);

        when(checkBox.isChecked()).thenReturn(false);
        controller.onMatchWidgetSizeChanged(checkBox);
        verify(vfxGroup).setMatchWidgetSize(false);
    }

    @Test
    public void testOnTransparentBackgroundChanged() throws Exception {
        Image imgBackground = mock(Image.class);
        Field field = CanvasContentViewController.class.getDeclaredField("imgBackground");
        field.setAccessible(true);
        field.set(controller, imgBackground);

        CheckBox checkBox = mock(CheckBox.class);
        when(checkBox.isChecked()).thenReturn(true);

        controller.onTransparentBackgroundChanged(checkBox);

        verify(imgBackground).setVisible(false);

        when(checkBox.isChecked()).thenReturn(false);
        controller.onTransparentBackgroundChanged(checkBox);
        verify(imgBackground).setVisible(true);
    }
}
