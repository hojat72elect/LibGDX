package com.kotcrab.vis.ui.util.adapter;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link CachedItemAdapter}.
 */
public class CachedItemAdapterTest {

    private TestCachedAdapter adapter;
    private List<String> createViewCalls;
    private List<String> updateViewCalls;

    @Before
    public void setUp() {
        createViewCalls = new ArrayList<>();
        updateViewCalls = new ArrayList<>();
        adapter = new TestCachedAdapter(createViewCalls, updateViewCalls);
    }

    @Test
    public void testGetViewCreatesViewOnFirstCall() {
        String item = "item1";
        Actor view = adapter.getView(item);
        Assert.assertNotNull(view);
        Assert.assertEquals(1, createViewCalls.size());
        Assert.assertEquals(item, createViewCalls.get(0));
        Assert.assertEquals(0, updateViewCalls.size());
    }

    @Test
    public void testGetViewCachesViewOnSecondCall() {
        String item = "item1";
        Actor view1 = adapter.getView(item);
        Actor view2 = adapter.getView(item);
        Assert.assertSame(view1, view2);
        Assert.assertEquals(1, createViewCalls.size());
        Assert.assertEquals(1, updateViewCalls.size());
        Assert.assertEquals(item, updateViewCalls.get(0));
    }

    @Test
    public void testGetViewDifferentItemsCreateDifferentViews() {
        Actor view1 = adapter.getView("a");
        Actor view2 = adapter.getView("b");
        Assert.assertNotNull(view1);
        Assert.assertNotNull(view2);
        Assert.assertNotSame(view1, view2);
        Assert.assertEquals(2, createViewCalls.size());
    }

    @Test
    public void testGetViewsReturnsInternalMap() {
        adapter.getView("x");
        Assert.assertEquals(1, adapter.getViews().size);
        Assert.assertTrue(adapter.getViews().containsKey("x"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetViewThrowsWhenCreateViewReturnsNull() {
        CachedItemAdapter<String, Actor> nullAdapter = new CachedItemAdapter<String, Actor>() {
            @Override
            protected Actor createView(String item) {
                return null;
            }

            @Override
            protected void updateView(Actor view, String item) {
            }
        };
        nullAdapter.getView("x");
    }

    private static class TestCachedAdapter extends CachedItemAdapter<String, Actor> {
        private final List<String> createViewCalls;
        private final List<String> updateViewCalls;

        TestCachedAdapter(List<String> createViewCalls, List<String> updateViewCalls) {
            this.createViewCalls = createViewCalls;
            this.updateViewCalls = updateViewCalls;
        }

        @Override
        protected Actor createView(String item) {
            createViewCalls.add(item);
            return new Actor();
        }

        @Override
        protected void updateView(Actor view, String item) {
            updateViewCalls.add(item);
        }
    }
}
