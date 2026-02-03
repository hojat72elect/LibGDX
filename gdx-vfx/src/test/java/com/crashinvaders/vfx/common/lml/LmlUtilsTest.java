package com.crashinvaders.vfx.common.lml;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.lml.parser.LmlData;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlView;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.parser.action.ActionContainerWrapper;
import com.github.czyzby.lml.parser.impl.tag.Dtd;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.io.Writer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LmlUtilsTest {

    @Test
    public void testSaveDtdSchema() {
        LmlParser parser = mock(LmlParser.class);
        FileHandle file = mock(FileHandle.class);
        Writer writer = mock(Writer.class);

        when(parser.isStrict()).thenReturn(true);
        when(file.writer(false, "UTF-8")).thenReturn(writer);

        try (MockedStatic<Dtd> dtdMock = mockStatic(Dtd.class)) {
            LmlUtils.saveDtdSchema(parser, file);

            verify(parser).setStrict(false);
            dtdMock.verify(() -> Dtd.saveSchema(eq(parser), eq(writer)));
            verify(parser).setStrict(true);
            try {
                verify(writer).close();
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }
    }

    @Test
    public void testParseLmlTemplateSimple() {
        LmlParser parser = mock(LmlParser.class);
        FileHandle file = mock(FileHandle.class);
        Actor actor = new Actor();
        Array<Actor> actors = new Array<>();
        actors.add(actor);

        when(parser.parseTemplate(file)).thenReturn(actors);

        Actor result = LmlUtils.parseLmlTemplate(parser, file);

        assertEquals(actor, result);
        verify(parser).parseTemplate(file);
    }

    @Test
    public void testParseLmlTemplateWithObject() {
        LmlParser parser = mock(LmlParser.class);
        FileHandle file = mock(FileHandle.class);
        Object view = new Object();
        Actor actor = new Actor();
        Array<Actor> actors = new Array<>();
        actors.add(actor);

        when(parser.createView(view, file)).thenReturn(actors);

        Actor result = LmlUtils.parseLmlTemplate(parser, view, file);

        assertEquals(actor, result);
        verify(parser).createView(view, file);
    }

    @Test
    public void testParseLmlTemplateWithViewController() {
        LmlParser parser = mock(LmlParser.class);
        LmlData lmlData = mock(LmlData.class);
        LmlView viewController = mock(LmlView.class);
        FileHandle file = mock(FileHandle.class);
        ActionContainerWrapper acw = mock(ActionContainerWrapper.class);
        ActionContainer actionContainer = mock(ActionContainer.class);

        when(parser.getData()).thenReturn(lmlData);
        when(viewController.getViewId()).thenReturn("testId");
        when(lmlData.getActionContainer("testId")).thenReturn(acw);
        when(acw.getActionContainer()).thenReturn(actionContainer);

        Actor actor = new Actor();
        Array<Actor> actors = new Array<>();
        actors.add(actor);
        when(parser.createView(viewController, file)).thenReturn(actors);

        Actor result = LmlUtils.parseLmlTemplate(parser, viewController, file);

        assertEquals(actor, result);
        // Verify action container was added back
        verify(lmlData).addActionContainer("testId", actionContainer);
    }

    @Test
    public void testParseLmlTemplateWithStageRemoval() {
        LmlParser parser = mock(LmlParser.class);
        LmlData lmlData = mock(LmlData.class);
        LmlView viewController = mock(LmlView.class);
        FileHandle file = mock(FileHandle.class);
        Stage stage = mock(Stage.class);

        when(parser.getData()).thenReturn(lmlData);
        when(viewController.getViewId()).thenReturn("testId");
        when(viewController.getStage()).thenReturn(stage);

        Actor actor = spy(new Actor());
        Array<Actor> actors = new Array<>();
        actors.add(actor);
        when(parser.createView(viewController, file)).thenReturn(actors);

        Actor result = LmlUtils.parseLmlTemplate(parser, viewController, file);

        assertEquals(actor, result);
        // Verify actor was removed from stage (mock actor.remove())
        verify(actor).remove();
    }
}
