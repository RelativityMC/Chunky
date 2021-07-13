package org.popcraft.chunky.platform;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.Unit;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionType;
import org.popcraft.chunky.mixin.ServerChunkManagerMixin;
import org.popcraft.chunky.mixin.ThreadedAnvilChunkStorageMixin;
import org.popcraft.chunky.scheduler.SchedulerUtils;
import org.popcraft.chunky.util.Coordinate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class FabricWorld implements World {
    private ServerWorld serverWorld;
    private Border worldBorder;
    private static final ChunkTicketType<Unit> CHUNKY = ChunkTicketType.create("chunky", (unit, unit2) -> 0);

    public FabricWorld(ServerWorld serverWorld) {
        this.serverWorld = serverWorld;
        this.worldBorder = new FabricBorder(serverWorld.getWorldBorder());
    }

    @Override
    public String getName() {
        return serverWorld.getRegistryKey().getValue().toString();
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public CompletableFuture<Void> getChunkAtAsync(int x, int z) {
        if (Thread.currentThread() != serverWorld.getServer().getThread()) {
            return CompletableFuture.supplyAsync(() -> getChunkAtAsync(x, z), serverWorld.getServer()).thenCompose(Function.identity());
        } else {
            final CompletableFuture<Void> chunkFuture = new CompletableFuture<>();
            final ChunkPos chunkPos = new ChunkPos(x, z);
            serverWorld.getChunkManager().addTicket(CHUNKY, chunkPos, 0, Unit.INSTANCE);
            ThreadedAnvilChunkStorage threadedAnvilChunkStorage = serverWorld.getChunkManager().threadedAnvilChunkStorage;
            ThreadedAnvilChunkStorageMixin threadedAnvilChunkStorageMixin = (ThreadedAnvilChunkStorageMixin) threadedAnvilChunkStorage;
            SchedulerUtils.executor.execute(() -> {
                ChunkHolder chunkHolder = threadedAnvilChunkStorageMixin.invokeGetChunkHolder(chunkPos.toLong());
                if (chunkHolder == null) {
                    System.err.println("null chunkholder");
                    chunkFuture.complete(null);
                    serverWorld.getChunkManager().removeTicket(CHUNKY, chunkPos, 0, Unit.INSTANCE);
                } else {
                    threadedAnvilChunkStorage.getChunk(chunkHolder, ChunkStatus.FULL).thenAcceptAsync(either -> {
                        chunkFuture.complete(null);
                        serverWorld.getChunkManager().removeTicket(CHUNKY, chunkPos, 0, Unit.INSTANCE);
                    }, serverWorld.getServer());
                }
            });
            return chunkFuture;
        }
    }

    @Override
    public UUID getUUID() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSeaLevel() {
        return serverWorld.getSeaLevel();
    }

    @Override
    public Coordinate getSpawnCoordinate() {
        BlockPos spawnPos = serverWorld.getSpawnPos();
        return new Coordinate(spawnPos.getX(), spawnPos.getZ());
    }

    @Override
    public Border getWorldBorder() {
        return worldBorder;
    }

    @Override
    public Optional<Path> getEntitiesDirectory() {
        return getDirectory("entities");
    }

    @Override
    public Optional<Path> getPOIDirectory() {
        return getDirectory("poi");
    }

    @Override
    public Optional<Path> getRegionDirectory() {
        return getDirectory("region");
    }

    private Optional<Path> getDirectory(final String name) {
        if (name == null) {
            return Optional.empty();
        }
        Path directory = DimensionType.getSaveDirectory(serverWorld.getRegistryKey(), serverWorld.getServer().getSavePath(WorldSavePath.ROOT).toFile()).toPath().normalize().resolve(name);
        return Files.exists(directory) ? Optional.of(directory) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return serverWorld.equals(((FabricWorld) o).serverWorld);
    }

    @Override
    public int hashCode() {
        return serverWorld.hashCode();
    }
}
