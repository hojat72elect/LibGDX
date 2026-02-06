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
import java.util.concurrent.atomic.AtomicReference;

/**
 * Unit tests for {@link AsyncTask}.
 */
public class AsyncTaskTest {

    private Application previousApp;

    @Before
    public void setUp() {
        previousApp = Gdx.app;
        Gdx.app = (Application) Proxy.newProxyInstance(
                Application.class.getClassLoader(),
                new Class[]{Application.class},
                (proxy, method, args) -> {
                    if ("postRunnable".equals(method.getName()) && args != null && args.length == 1 && args[0] instanceof Runnable) {
                        // Execute runnable directly to ensure sequential execution and avoid race conditions
                        ((Runnable) args[0]).run();
                        return null;
                    }
                    return null;
                });
    }

    @After
    public void tearDown() {
        Gdx.app = previousApp;
    }

    @Test
    public void testGetThreadName() {
        AsyncTask task = new TestAsyncTask("my-thread");
        Assert.assertEquals("my-thread", task.getThreadName());
    }

    @Test
    public void testGetStatusInitial() {
        AsyncTask task = new TestAsyncTask("t");
        Assert.assertEquals(AsyncTask.Status.PENDING, task.getStatus());
    }

    @Test
    public void testExecuteThrowsWhenAlreadyFinished() throws InterruptedException {
        CountDownLatch done = new CountDownLatch(1);
        AsyncTask task = new AsyncTask("t") {
            @Override
            protected void doInBackground() {
                // complete immediately
            }
        };
        task.addListener(new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
            }

            @Override
            public void progressChanged(int newProgressPercent) {
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
        Assert.assertTrue("Task should complete", done.await(2, TimeUnit.SECONDS));
        Assert.assertEquals(AsyncTask.Status.FINISHED, task.getStatus());

        try {
            task.execute();
            Assert.fail("Should throw when executed again");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("already executed"));
        }
    }

    @Test
    public void testExecuteThrowsWhenAlreadyRunning() throws InterruptedException {
        final CountDownLatch inBackground = new CountDownLatch(1);
        final CountDownLatch release = new CountDownLatch(1);
        AsyncTask task = new AsyncTask("t") {
            @Override
            protected void doInBackground() {
                inBackground.countDown();
                try {
                    release.await(3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        task.execute();
        Assert.assertTrue("doInBackground should start", inBackground.await(2, TimeUnit.SECONDS));

        try {
            task.execute();
            Assert.fail("Should throw when already running");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("already running"));
        } finally {
            release.countDown();
        }
    }

    @Test
    public void testListenerFinishedCalled() throws InterruptedException {
        CountDownLatch finished = new CountDownLatch(1);
        AsyncTask task = new TestAsyncTask("t");
        task.addListener(new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
            }

            @Override
            public void progressChanged(int newProgressPercent) {
            }

            @Override
            public void finished() {
                finished.countDown();
            }

            @Override
            public void failed(String message, Exception exception) {
            }
        });
        task.execute();
        Assert.assertTrue(finished.await(2, TimeUnit.SECONDS));
        Assert.assertEquals(AsyncTask.Status.FINISHED, task.getStatus());
    }

    @Test
    public void testListenerProgressChanged() throws InterruptedException {
        CountDownLatch done = new CountDownLatch(1);
        AtomicInteger lastProgress = new AtomicInteger(-1);
        AsyncTask task = new AsyncTask("t") {
            @Override
            protected void doInBackground() {
                setProgressPercent(25);
                setProgressPercent(50);
                setProgressPercent(100);
            }
        };
        task.addListener(new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
            }

            @Override
            public void progressChanged(int newProgressPercent) {
                lastProgress.set(newProgressPercent);
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
        Assert.assertEquals(100, lastProgress.get());
    }

    @Test
    public void testListenerMessageChanged() throws InterruptedException {
        CountDownLatch done = new CountDownLatch(1);
        AtomicReference<String> lastMessage = new AtomicReference<>();
        AsyncTask task = new AsyncTask("t") {
            @Override
            protected void doInBackground() {
                setMessage("step 1");
                setMessage("step 2");
            }
        };
        task.addListener(new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
                lastMessage.set(message);
            }

            @Override
            public void progressChanged(int newProgressPercent) {
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
        Assert.assertTrue("Should receive at least one message", lastMessage.get() != null && (lastMessage.get().equals("step 1") || lastMessage.get().equals("step 2")));
    }

    @Test
    public void testFailedNotifiesListener() throws InterruptedException {
        CountDownLatch failed = new CountDownLatch(1);
        AtomicReference<String> msgRef = new AtomicReference<>();
        AtomicReference<Exception> exRef = new AtomicReference<>();
        AsyncTask task = new AsyncTask("t") {
            @Override
            protected void doInBackground() {
                failed("error message", new RuntimeException("cause"));
            }
        };
        task.addListener(new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
            }

            @Override
            public void progressChanged(int newProgressPercent) {
            }

            @Override
            public void finished() {
                failed.countDown();
            }

            @Override
            public void failed(String message, Exception exception) {
                msgRef.set(message);
                exRef.set(exception);
            }
        });
        task.execute();
        Assert.assertTrue(failed.await(2, TimeUnit.SECONDS));
        Assert.assertEquals("error message", msgRef.get());
        Assert.assertNotNull(exRef.get());
        Assert.assertEquals("cause", exRef.get().getMessage());
    }

    @Test
    public void testFailedWithExceptionOnly() throws InterruptedException {
        CountDownLatch failed = new CountDownLatch(1);
        AtomicReference<String> msgRef = new AtomicReference<>();
        AsyncTask task = new AsyncTask("t") {
            @Override
            protected void doInBackground() {
                failed(new IllegalStateException("oops"));
            }
        };
        task.addListener(new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
            }

            @Override
            public void progressChanged(int newProgressPercent) {
            }

            @Override
            public void finished() {
            }

            @Override
            public void failed(String message, Exception exception) {
                msgRef.set(message);
                failed.countDown();
            }
        });
        task.execute();
        Assert.assertTrue("Failed callback should be invoked", failed.await(2, TimeUnit.SECONDS));
        Assert.assertNotNull(msgRef.get());
        Assert.assertTrue("Message should contain exception message", msgRef.get().contains("oops") || "oops".equals(msgRef.get()));
    }

    @Test
    public void testAddListenerRemoveListener() {
        AsyncTask task = new TestAsyncTask("t");
        AsyncTaskListener listener = new AsyncTaskListener() {
            @Override
            public void messageChanged(String message) {
            }

            @Override
            public void progressChanged(int newProgressPercent) {
            }

            @Override
            public void finished() {
            }

            @Override
            public void failed(String message, Exception exception) {
            }
        };
        task.addListener(listener);
        Assert.assertTrue(task.removeListener(listener));
        Assert.assertFalse(task.removeListener(listener));
    }

    private static class TestAsyncTask extends AsyncTask {
        TestAsyncTask(String threadName) {
            super(threadName);
        }

        @Override
        protected void doInBackground() {
        }
    }
}
