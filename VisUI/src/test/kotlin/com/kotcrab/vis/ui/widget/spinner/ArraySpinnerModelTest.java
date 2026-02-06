package com.kotcrab.vis.ui.widget.spinner;

import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ArraySpinnerModel}.
 */
public class ArraySpinnerModelTest {

    @Mock
    private Spinner mockSpinner;

    @Mock
    private VisValidatableTextField mockTextField;

    private ArraySpinnerModel<String> model;
    private Array<String> testItems;

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
        when(mockSpinner.getTextField()).thenReturn(mockTextField);
        when(mockSpinner.isProgrammaticChangeEvents()).thenReturn(true);

        testItems = new Array<>();
        testItems.addAll("Item1", "Item2", "Item3");
        model = new ArraySpinnerModel<>(testItems);
    }

    @Test
    public void testConstructorWithItems() {
        assertEquals("Should have 3 items", 3, model.getItems().size);
        assertEquals("Current should be first item", "Item1", model.getCurrent());
        assertEquals("Current index should be 0", 0, model.getCurrentIndex());
    }

    @Test
    public void testConstructorEmpty() {
        ArraySpinnerModel<String> emptyModel = new ArraySpinnerModel<>();
        assertEquals("Should have 0 items", 0, emptyModel.getItems().size);
        assertNull("Current should be null", emptyModel.getCurrent());
        assertEquals("Current index should be -1", -1, emptyModel.getCurrentIndex());
    }

    @Test
    public void testBind() {
        model.bind(mockSpinner);

        verify(mockTextField).addValidator(any(InputValidator.class));
        verify(mockSpinner).notifyValueChanged(true);
        assertEquals("Current should be first item after bind", "Item1", model.getCurrent());
    }

    @Test
    public void testItemToString() {
        ArraySpinnerModel<String> stringModel = new ArraySpinnerModel<>(testItems);
        assertEquals("Should return item string", "Item1", stringModel.itemToString("Item1"));
        assertNull("Should return null for null item", stringModel.itemToString(null));

        ArraySpinnerModel<Integer> intModel = new ArraySpinnerModel<>(new Array<>(new Integer[]{1, 2, 3}));
        assertEquals("Should return integer string", "1", intModel.itemToString(1));
    }

    @Test
    public void testIncrementModel() {
        model.bind(mockSpinner);

        assertTrue("Should increment from first to second item", model.incrementModel());
        assertEquals("Should be on second item", "Item2", model.getCurrent());
        assertEquals("Should be at index 1", 1, model.getCurrentIndex());

        assertTrue("Should increment from second to third item", model.incrementModel());
        assertEquals("Should be on third item", "Item3", model.getCurrent());
        assertEquals("Should be at index 2", 2, model.getCurrentIndex());
    }

    @Test
    public void testIncrementModelAtEndWithoutWrap() {
        model.bind(mockSpinner);
        model.setCurrent(2); // Go to last item

        assertFalse("Should not increment beyond last item without wrap", model.incrementModel());
        assertEquals("Should stay on last item", "Item3", model.getCurrent());
        assertEquals("Should stay at index 2", 2, model.getCurrentIndex());
    }

    @Test
    public void testIncrementModelAtEndWithWrap() {
        model.bind(mockSpinner);
        model.setWrap(true);
        model.setCurrent(2); // Go to last item

        assertTrue("Should wrap to first item", model.incrementModel());
        assertEquals("Should be on first item", "Item1", model.getCurrent());
        assertEquals("Should be at index 0", 0, model.getCurrentIndex());
    }

    @Test
    public void testDecrementModel() {
        model.bind(mockSpinner);
        model.setCurrent(2); // Go to last item

        assertTrue("Should decrement from third to second item", model.decrementModel());
        assertEquals("Should be on second item", "Item2", model.getCurrent());
        assertEquals("Should be at index 1", 1, model.getCurrentIndex());

        assertTrue("Should decrement from second to first item", model.decrementModel());
        assertEquals("Should be on first item", "Item1", model.getCurrent());
        assertEquals("Should be at index 0", 0, model.getCurrentIndex());
    }

    @Test
    public void testDecrementModelAtStartWithoutWrap() {
        model.bind(mockSpinner);

        assertFalse("Should not decrement before first item without wrap", model.decrementModel());
        assertEquals("Should stay on first item", "Item1", model.getCurrent());
        assertEquals("Should stay at index 0", 0, model.getCurrentIndex());
    }

    @Test
    public void testDecrementModelAtStartWithWrap() {
        model.bind(mockSpinner);
        model.setWrap(true);

        assertTrue("Should wrap to last item", model.decrementModel());
        assertEquals("Should be on last item", "Item3", model.getCurrent());
        assertEquals("Should be at index 2", 2, model.getCurrentIndex());
    }

    @Test
    public void testGetText() {
        assertEquals("Should return current item as string", "Item1", model.getText());

        model.setCurrent(1);
        assertEquals("Should return updated current item as string", "Item2", model.getText());
    }

    @Test
    public void testTextChanged() {
        model.bind(mockSpinner);
        when(mockTextField.getText()).thenReturn("Item2");

        model.textChanged();

        assertEquals("Should update current item based on text", "Item2", model.getCurrent());
        assertEquals("Should update current index", 1, model.getCurrentIndex());
    }

    @Test
    public void testTextChangedWithInvalidText() {
        model.bind(mockSpinner);
        when(mockTextField.getText()).thenReturn("InvalidItem");

        model.textChanged();

        assertEquals("Should not change current item for invalid text", "Item1", model.getCurrent());
        assertEquals("Should not change current index", 0, model.getCurrentIndex());
    }

    @Test
    public void testInvalidateDataSet() {
        model.bind(mockSpinner);
        model.setCurrent(2);

        model.getItems().removeIndex(1); // Remove "Item2"
        model.invalidateDataSet();

        verify(mockSpinner, times(3)).notifyValueChanged(true);
        assertEquals("Current index should be clamped", 1, model.getCurrentIndex());
        assertEquals("Current item should be updated", "Item3", model.getCurrent());
    }

    @Test
    public void testSetItems() {
        Array<String> newItems = new Array<>();
        newItems.addAll("New1", "New2");

        model.bind(mockSpinner);
        model.setItems(newItems);

        verify(mockSpinner, times(2)).notifyValueChanged(true);
        assertEquals("Should have 2 new items", 2, model.getItems().size);
        assertEquals("Current should be first new item", "New1", model.getCurrent());
        assertEquals("Current index should be 0", 0, model.getCurrentIndex());
    }

    @Test
    public void testSetCurrentByIndex() {
        model.bind(mockSpinner);

        model.setCurrent(1);
        assertEquals("Should set current to index 1", "Item2", model.getCurrent());
        assertEquals("Current index should be 1", 1, model.getCurrentIndex());
        verify(mockSpinner, times(2)).notifyValueChanged(true);
    }

    @Test
    public void testSetCurrentByIndexWithFireEvent() {
        model.bind(mockSpinner);

        model.setCurrent(1, false);
        assertEquals("Should set current to index 1", "Item2", model.getCurrent());
        verify(mockSpinner).notifyValueChanged(false);
    }

    @Test
    public void testSetCurrentByItem() {
        model.bind(mockSpinner);

        model.setCurrent("Item3");
        assertEquals("Should set current to Item3", "Item3", model.getCurrent());
        assertEquals("Current index should be 2", 2, model.getCurrentIndex());
        verify(mockSpinner, times(2)).notifyValueChanged(true);
    }

    @Test
    public void testSetCurrentByNonExistentItem() {
        model.bind(mockSpinner);

        model.setCurrent("NonExistent");
        assertEquals("Should set current to first item for non-existent item", "Item1", model.getCurrent());
        assertEquals("Current index should be 0", 0, model.getCurrentIndex());
        verify(mockSpinner, times(2)).notifyValueChanged(true);
    }

    @Test
    public void testSetCurrentByItemWithFireEvent() {
        model.bind(mockSpinner);

        model.setCurrent("Item2", false);
        assertEquals("Should set current to Item2", "Item2", model.getCurrent());
        verify(mockSpinner).notifyValueChanged(false);
    }

    @Test
    public void testEmptyArrayModel() {
        ArraySpinnerModel<String> emptyModel = new ArraySpinnerModel<>();
        emptyModel.bind(mockSpinner);

        assertFalse("Increment should return false for empty model", emptyModel.incrementModel());
        assertFalse("Decrement should return false for empty model", emptyModel.decrementModel());
        assertNull("GetText should return null for empty model", emptyModel.getText());
        assertEquals("Current index should be -1", -1, emptyModel.getCurrentIndex());
    }
}
