package com.crashinvaders.vfx.demo.screens.demo.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.common.lml.CommonLmlParser;
import com.crashinvaders.vfx.common.viewcontroller.ViewControllerManager;
import com.crashinvaders.vfx.scene2d.VfxWidgetGroup;
import com.github.czyzby.lml.parser.LmlData;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class StatisticPanelViewControllerTest {

    private ViewControllerManager viewControllers;
    private CommonLmlParser lmlParser;
    private StatisticPanelViewController controller;
    private VfxManager vfxManager;
    private Label lblFboSize;
    private Label lblFps;

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
        LmlData lmlData = mock(LmlData.class);
        when(lmlParser.getData()).thenReturn(lmlData);

        controller = new StatisticPanelViewController(viewControllers, lmlParser);

        vfxManager = mock(VfxManager.class);
        lblFboSize = mock(Label.class);
        lblFps = mock(Label.class);

        controller.lblFboSize = lblFboSize;
        controller.lblFps = lblFps;

        // Inject vfxManager via reflection as it's private and initialized in
        // onViewCreated
        try {
            Field field = StatisticPanelViewController.class.getDeclaredField("vfxManager");
            field.setAccessible(true);
            field.set(controller, vfxManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOnViewCreated() {
        Group sceneRoot = mock(Group.class);
        VfxWidgetGroup vfxGroup = mock(VfxWidgetGroup.class);
        when(sceneRoot.findActor("vfxGroup")).thenReturn(vfxGroup);
        when(vfxGroup.getVfxManager()).thenReturn(vfxManager);

        controller.onViewCreated(sceneRoot);

        verify(sceneRoot).findActor("vfxGroup");
        verify(vfxGroup).getVfxManager();
    }

    @Test
    public void testUpdateFboSizeView() {
        when(vfxManager.getWidth()).thenReturn(800);
        when(vfxManager.getHeight()).thenReturn(600);

        controller.updateFboSizeView();

        verify(lblFboSize).setText("800x600");
    }

    @Test
    public void testUpdateFpsView() {
        Graphics graphics = mock(Graphics.class);
        when(graphics.getFramesPerSecond()).thenReturn(60);

        Graphics oldGraphics = Gdx.graphics;
        Gdx.graphics = graphics;
        try {
            controller.updateFpsView();
            verify(lblFps).setText("60");
        } finally {
            Gdx.graphics = oldGraphics;
        }
    }
}
