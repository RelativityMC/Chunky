package org.popcraft.chunky.platform;

import org.popcraft.chunky.ChunkySponge;
import org.popcraft.chunky.util.Coordinate;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SpongeWorld implements World {
    private org.spongepowered.api.world.World world;
    private ChunkySponge plugin;

    public SpongeWorld(org.spongepowered.api.world.World world, ChunkySponge plugin) {
        this.world = world;
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public CompletableFuture<Void> getChunkAtAsync(int x, int z) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Sponge.getGame().getScheduler().createTaskBuilder().execute(() -> {
            world.loadChunk(x, 0, z, true);
            future.complete(null);
        }).submit(plugin);
        return future;
    }

    @Override
    public UUID getUUID() {
        return world.getUniqueId();
    }

    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public Coordinate getSpawnCoordinate() {
        Location<org.spongepowered.api.world.World> spawnLocation = world.getSpawnLocation();
        return new Coordinate(spawnLocation.getBlockX(), spawnLocation.getBlockZ());
    }

    @Override
    public Border getWorldBorder() {
        return new SpongeBorder(world.getWorldBorder());
    }
}
