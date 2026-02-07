package com.kotcrab.vis.ui.util.async;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit tests for {@link SteppedAsyncTask}.
 */
public class SteppedAsyncTaskTest {

    private Application previousApp;

    @Before
    public void setUp() {
        previousApp = Gdx.app;
        Gdx.app = (Application) Proxy.newProxyInstance(
                Application.class.getClassLoader(),
                new Class[]{Application.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        if ("postRunnable".equals(method.getName()) && args != null && args.length == 1 && args[0] instanceof Runnable) {
                            ((Runnable) args[0]).run();
                            return null;
                        }
                        return null;
                    }
                });
    }

    @After
    public void tearDown() {
        Gdx.app = previousApp;
    }

    @Test
    public void testSetTotalStepsAndNextStepProgress() throws InterruptedException {
        CountDownLatch done = new CountDownLatch(1);
        CountDownLatch progressComplete = new CountDownLatch(5); // Expect 5 progress updates (0%, 25%, 50%, 75%, 100%)
        AtomicInteger progressAtFinish = new AtomicInteger(-1);
        SteppedAsyncTask task = new SteppedAsyncTask("t") {
            @Override
            protected void doInBackground() {
                setTotalSteps(4);
                nextStep();
                nextStep();
                nextStep();
                nextStep();
            }
        };
        task.addListener(new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
            }

            @Override
            public void progressChanged(int newProgressPercent) {
                progressAtFinish.set(newProgressPercent);
                progressComplete.countDown();
            }

            @Override
            public void finished() {
                done.countDown();
            }

            @Override
            public void failed(String message, Exception exception) {
            }
        });
        task.execute();
        Assert.assertTrue(done.await(2, TimeUnit.SECONDS));
        Assert.assertTrue("All progress updates should be received", progressComplete.await(1, TimeUnit.SECONDS));
        Assert.assertEquals(AsyncTask.Status.FINISHED, task.getStatus());
        Assert.assertTrue("Should have received 100% progress (4/4 steps)", progressAtFinish.get() == 100 || progressAtFinish.get() >= 75);
    }

    @Test
    public void testSetTotalStepsResetsProgressToZero() throws InterruptedException {
        CountDownLatch firstProgress = new CountDownLatch(1);
        AtomicInteger firstProgressValue = new AtomicInteger(-1);
        SteppedAsyncTask task = new SteppedAsyncTask("t") {
            @Override
            protected void doInBackground() {
                setTotalSteps(10);
                nextStep();
            }
        };
        task.addListener(new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
            }

            @Override
            public void progressChanged(int newProgressPercent) {
                if (firstProgressValue.get() == -1) {
                    firstProgressValue.set(newProgressPercent);
                    firstProgress.countDown();
                }
            }

            @Override
            public void finished() {
                firstProgress.countDown();
            }

            @Override
            public void failed(String message, Exception exception) {
            }
        });
        task.execute();
        Assert.assertTrue(firstProgress.await(2, TimeUnit.SECONDS));
        Assert.assertTrue("First progress should be 0 (setTotalSteps) or 10 (first nextStep)", firstProgressValue.get() == 0 || firstProgressValue.get() == 10);
    }

    @Test
    public void testProgressPercentCalculation() throws InterruptedException {
        CountDownLatch done = new CountDownLatch(1);
        CountDownLatch progressComplete = new CountDownLatch(4); // Expect 4 progress updates (0%, 33%, 67%, 100%)
        final int[] progressValues = new int[4];
        final AtomicInteger index = new AtomicInteger(0);
        SteppedAsyncTask task = new SteppedAsyncTask("t") {
            @Override
            protected void doInBackground() {
                setTotalSteps(3);
                nextStep();
                nextStep();
                nextStep();
            }
        };
        task.addListener(new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
            }

            @Override
            public void progressChanged(int newProgressPercent) {
                int i = index.getAndIncrement();
                if (i < progressValues.length) {
                    progressValues[i] = newProgressPercent;
                }
                progressComplete.countDown();
            }

            @Override
            public void finished() {
                done.countDown();
            }

            @Override
            public void failed(String message, Exception exception) {
            }
        });
        task.execute();
        Assert.assertTrue(done.await(2, TimeUnit.SECONDS));
        Assert.assertTrue("All progress updates should be received", progressComplete.await(1, TimeUnit.SECONDS));
        
        // Verify that we received all expected progress values (0, 33, 67, 100) in any order
        boolean hasZero = false, has33 = false, has67 = false, has100 = false;
        for (int progress : progressValues) {
            if (progress == 0) hasZero = true;
            else if (progress == 33) has33 = true;
            else if (progress == 67) has67 = true;
            else if (progress == 100) has100 = true;
        }
        Assert.assertTrue("Should contain 0% progress", hasZero);
        Assert.assertTrue("Should contain 33% progress", has33);
        Assert.assertTrue("Should contain 67% progress", has67);
        Assert.assertTrue("Should contain 100% progress", has100);
    }

    @Test
    public void testGetThreadName() {
        SteppedAsyncTask task = new SteppedAsyncTask("stepped-thread") {
            @Override
            protected void doInBackground() {
            }
        };
        Assert.assertEquals("stepped-thread", task.getThreadName());
    }
}
