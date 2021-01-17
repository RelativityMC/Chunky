package org.popcraft.chunky.platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FabricScheduler implements Scheduler {
    private ExecutorService executor;
    private final ThreadGroup tasks = new ThreadGroup("tasks");

    public FabricScheduler() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, Integer.MAX_VALUE, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
        threadPoolExecutor.setThreadFactory((runnable) -> {
            Thread thread = new Thread(tasks, runnable);
            thread.setDaemon(true);
            return thread;
        });
        this.executor = threadPoolExecutor;
    }

    @Override
    public void runTaskAsync(Runnable runnable) {
        executor.submit(runnable);
    }

    @Override
    public void cancelTasks() {
        tasks.interrupt();
    }
}
