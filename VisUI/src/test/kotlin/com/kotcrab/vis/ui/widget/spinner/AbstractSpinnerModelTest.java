package com.kotcrab.vis.ui.widget.spinner;

import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AbstractSpinnerModel}.
 */
public class AbstractSpinnerModelTest {

    @Mock
    private Spinner mockSpinner;

    @Mock
    private VisValidatableTextField mockTextField;

    private TestSpinnerModel model;

    @BeforeClass
    public static void setupGdx() {
        if (com.badlogic.gdx.Gdx.files == null) {
            com.badlogic.gdx.Gdx.files = (com.badlogic.gdx.Files) Proxy.newProxyInstance(
                    com.badlogic.gdx.Files.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Files.class},
                    (proxy, method, args) -> {
                        if ("classpath".equals(method.getName())) {
                            return new com.badlogic.gdx.files.FileHandle("test");
                        }
                        return null;
                    });
        }
        if (com.badlogic.gdx.Gdx.app == null) {
            com.badlogic.gdx.Gdx.app = (com.badlogic.gdx.Application) Proxy.newProxyInstance(
                    com.badlogic.gdx.Application.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Application.class},
                    (proxy, method, args) -> null);
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new TestSpinnerModel(false);
        when(mockSpinner.getTextField()).thenReturn(mockTextField);
        when(mockSpinner.isProgrammaticChangeEvents()).thenReturn(true);
    }

    @Test
    public void testBindWithAllowRebindFalse() {
        Spinner firstSpinner = mock(Spinner.class);
        when(firstSpinner.isProgrammaticChangeEvents()).thenReturn(true);

        model.bind(firstSpinner);

        // Second bind should throw exception
        try {
            model.bind(mockSpinner);
            fail("Expected IllegalStateException for rebinding non-rebindable model");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("can't be reused"));
        }
    }

    @Test
    public void testBindWithAllowRebindTrue() {
        TestSpinnerModel rebindableModel = new TestSpinnerModel(true);

        rebindableModel.bind(mockSpinner);
        rebindableModel.bind(mockSpinner); // Should not throw exception

        assertNotNull("Spinner should be bound", rebindableModel.spinner);
    }

    @Test
    public void testIncrementWithoutFireEvent() {
        model.bind(mockSpinner);
        model.setValueChanged(true);

        boolean result = model.increment();

        assertTrue("Increment should return true when value changes", result);
        verify(mockSpinner).notifyValueChanged(true);
    }

    @Test
    public void testIncrementWithFireEventFalse() {
        model.bind(mockSpinner);
        model.setValueChanged(true);

        boolean result = model.increment(false);

        assertTrue("Increment should return true when value changes", result);
        verify(mockSpinner).notifyValueChanged(false);
    }

    @Test
    public void testIncrementWithoutValueChange() {
        model.bind(mockSpinner);
        model.setValueChanged(false);

        boolean result = model.increment();

        assertFalse("Increment should return false when value doesn't change", result);
        verify(mockSpinner, never()).notifyValueChanged(anyBoolean());
    }

    @Test
    public void testDecrementWithoutFireEvent() {
        model.bind(mockSpinner);
        model.setValueChanged(true);

        boolean result = model.decrement();

        assertTrue("Decrement should return true when value changes", result);
        verify(mockSpinner).notifyValueChanged(true);
    }

    @Test
    public void testDecrementWithFireEventFalse() {
        model.bind(mockSpinner);
        model.setValueChanged(true);

        boolean result = model.decrement(false);

        assertTrue("Decrement should return true when value changes", result);
        verify(mockSpinner).notifyValueChanged(false);
    }

    @Test
    public void testDecrementWithoutValueChange() {
        model.bind(mockSpinner);
        model.setValueChanged(false);

        boolean result = model.decrement();

        assertFalse("Decrement should return false when value doesn't change", result);
        verify(mockSpinner, never()).notifyValueChanged(anyBoolean());
    }

    @Test
    public void testWrapProperty() {
        assertFalse("Wrap should be false by default", model.isWrap());

        model.setWrap(true);
        assertTrue("Wrap should be true after setting", model.isWrap());

        model.setWrap(false);
        assertFalse("Wrap should be false after setting to false", model.isWrap());
    }

    @Test
    public void testAllowRebindProperty() {
        assertFalse("Allow rebind should be false by default", model.isAllowRebind());

        TestSpinnerModel rebindableModel = new TestSpinnerModel(true);
        assertTrue("Allow rebind should be true when set", rebindableModel.isAllowRebind());
    }

    @Test
    public void testSetAllowRebind() {
        model.setAllowRebind(true);
        assertTrue("Allow rebind should be true after setting", model.isAllowRebind());

        model.setAllowRebind(false);
        assertFalse("Allow rebind should be false after setting", model.isAllowRebind());
    }

    /**
     * Test implementation of AbstractSpinnerModel for testing purposes.
     */
    private static class TestSpinnerModel extends AbstractSpinnerModel {
        private boolean valueChanged = false;

        public TestSpinnerModel(boolean allowRebind) {
            super(allowRebind);
        }

        @Override
        protected boolean incrementModel() {
            return valueChanged;
        }

        @Override
        protected boolean decrementModel() {
            return valueChanged;
        }

        @Override
        public void textChanged() {

        }

        @Override
        public String getText() {
            return "test";
        }

        public void setValueChanged(boolean valueChanged) {
            this.valueChanged = valueChanged;
        }
    }
}
