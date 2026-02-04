package com.crashinvaders.vfx.demo.screens.demo.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.common.lml.CommonLmlParser;
import com.crashinvaders.vfx.common.viewcontroller.ViewControllerManager;
import com.crashinvaders.vfx.effects.ChainVfxEffect;
import com.github.czyzby.lml.parser.LmlData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EffectRosterViewControllerTest {

    private ViewControllerManager viewControllers;
    private CommonLmlParser lmlParser;
    private EffectRosterViewController controller;
    private VfxManager vfxManager;
    private VfxViewController vfxViewController;

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

        vfxViewController = mock(VfxViewController.class);
        vfxManager = mock(VfxManager.class);
        when(vfxViewController.getVfxManager()).thenReturn(vfxManager);
        when(viewControllers.get(VfxViewController.class)).thenReturn(vfxViewController);
    }

    @Test
    public void testAddEffectToChain() throws Exception {
        controller = new EffectRosterViewController(viewControllers, lmlParser);
        Group vgEffectsChain = mock(VerticalGroup.class);

        // Manual injection
        java.lang.reflect.Field vfxManagerField = EffectRosterViewController.class.getDeclaredField("vfxManager");
        vfxManagerField.setAccessible(true);
        vfxManagerField.set(controller, vfxManager);

        java.lang.reflect.Field vgEffectsChainField = EffectRosterViewController.class
                .getDeclaredField("vgEffectsChain");
        vgEffectsChainField.setAccessible(true);
        vgEffectsChainField.set(controller, vgEffectsChain);

        // Create a model to add
        Class<?> modelClass = Class.forName(
                "com.crashinvaders.vfx.demo.screens.demo.controllers.EffectRosterViewController$EffectEntryModel");
        java.lang.reflect.Constructor<?> modelConstructor = modelClass.getDeclaredConstructor(String.class,
                ChainVfxEffect.class);
        modelConstructor.setAccessible(true);
        ChainVfxEffect effect = mock(ChainVfxEffect.class);
        Object effectModel = modelConstructor.newInstance("Test Effect", effect);

        try (MockedConstruction<EffectRosterViewController.EffectEntryViewController> construction = mockConstruction(
                EffectRosterViewController.EffectEntryViewController.class,
                (mock, context) -> {
                    when(mock.getViewRoot()).thenReturn(new Group());
                    doReturn(effectModel).when(mock).getModel();
                })) {

            Method addEffectMethod = EffectRosterViewController.class.getDeclaredMethod("addEffectToChain", modelClass);
            addEffectMethod.setAccessible(true);
            addEffectMethod.invoke(controller, effectModel);

            verify(vfxManager).addEffect(eq(effect));
            verify(vgEffectsChain).addActor(any(Group.class));
        }
    }
}
