package org.popcraft.chunky.events;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class SchedulingUtil {

    private static final ConcurrentLinkedQueue<Runnable> tickEnd = new ConcurrentLinkedQueue<>();
    public static final Executor tickEndExecutor = tickEnd::add;

    public static void invokeTickEnd() {
        Runnable runnable;
        while ((runnable = tickEnd.poll()) != null) {
            try {
                runnable.run();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private SchedulingUtil() {
    }

}
