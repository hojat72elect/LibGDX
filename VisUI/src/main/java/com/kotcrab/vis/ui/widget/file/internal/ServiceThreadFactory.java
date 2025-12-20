package com.kotcrab.vis.ui.widget.file.internal;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceThreadFactory implements ThreadFactory {
    private final AtomicLong count = new AtomicLong(0);
    private final String threadPrefix;

    public ServiceThreadFactory(String threadPrefix) {
        super();
        this.threadPrefix = threadPrefix + "-";
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName(threadPrefix + count.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}
