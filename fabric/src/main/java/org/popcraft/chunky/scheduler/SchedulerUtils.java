package org.popcraft.chunky.scheduler;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class SchedulerUtils {

    private static final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    public static final Executor executor = tasks::add;

    public static void processQueue() {
        Runnable task;
        while ((task = tasks.poll()) != null)
            try {
                task.run();
            } catch (Throwable t) {
                t.printStackTrace();
            }
    }

}
