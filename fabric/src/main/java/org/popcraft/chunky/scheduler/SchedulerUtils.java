package org.popcraft.chunky.scheduler;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

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

    private static final Set<Consumer<MinecraftServer>> serverStartListeners = Sets.newConcurrentHashSet();

    public static void registerOnServerStart(Consumer<MinecraftServer> consumer) {
        SchedulerUtils.serverStartListeners.add(consumer);
    }

    public static void invokeOnServerStart(MinecraftServer server) {
        SchedulerUtils.serverStartListeners.forEach(consumer -> consumer.accept(server));
    }

    private static final Set<Consumer<MinecraftServer>> serverStopListeners = Sets.newConcurrentHashSet();

    public static void registerOnServerStop(Consumer<MinecraftServer> consumer) {
        SchedulerUtils.serverStopListeners.add(consumer);
    }

    public static void invokeOnServerStop(MinecraftServer server) {
        SchedulerUtils.serverStopListeners.forEach(consumer -> consumer.accept(server));
    }


    private static final Set<Consumer<CommandDispatcher<ServerCommandSource>>> commandListeners = Sets.newConcurrentHashSet();

    public static void registerCommand(Consumer<CommandDispatcher<ServerCommandSource>> consumer) {
        SchedulerUtils.commandListeners.add(consumer);
    }

    public static void invokeRegisterCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        SchedulerUtils.commandListeners.forEach(consumer -> consumer.accept(dispatcher));
    }

}
