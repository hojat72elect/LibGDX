package com.crashinvaders.vfx.demo.screens.demo.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.common.lml.CommonLmlParser;
import com.crashinvaders.vfx.common.viewcontroller.ViewControllerManager;
import com.crashinvaders.vfx.scene2d.VfxWidgetGroup;
import com.github.czyzby.lml.parser.LmlData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VfxViewControllerTest {

    private ViewControllerManager viewControllers;
    private CommonLmlParser lmlParser;
    private VfxViewController controller;

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

        controller = new VfxViewController(viewControllers, lmlParser);
    }

    @Test
    public void testCreateCanvas() {
        VfxManager vfxManager = mock(VfxManager.class);

        try (MockedConstruction<WidgetGroup> widgetGroupMock = mockConstruction(WidgetGroup.class);
                MockedConstruction<VfxWidgetGroup> vfxWidgetGroupMock = mockConstruction(VfxWidgetGroup.class,
                        (mock, context) -> {
                            when(mock.getVfxManager()).thenReturn(vfxManager);
                        })) {

            Actor result = controller.createCanvas();

            assertNotNull(result);
            assertTrue(result instanceof VfxWidgetGroup);

            VfxWidgetGroup vfxGroup = (VfxWidgetGroup) result;
            verify(vfxGroup).setName("vfxGroup");
            verify(vfxGroup).setMatchWidgetSize(true);
            verify(vfxManager).setBlendingEnabled(false);

            assertNotNull(controller.getVfxManager());
            assertEquals(vfxManager, controller.getVfxManager());
            assertNotNull(controller.getCanvasRoot());
        }
    }
}
