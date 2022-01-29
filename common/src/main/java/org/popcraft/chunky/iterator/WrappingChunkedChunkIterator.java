package org.popcraft.chunky.iterator;

import org.popcraft.chunky.Selection;
import org.popcraft.chunky.util.ChunkCoordinate;

import java.util.LinkedList;
import java.util.Queue;

public class WrappingChunkedChunkIterator implements ChunkIterator {

    private static final int CHUNK_SIZE = 8;
    private final ChunkIterator delegatingIterator;
    private final Queue<ChunkCoordinate> queue = new LinkedList<>();
    private final int centerChunkX;
    private final int centerChunkZ;

    public WrappingChunkedChunkIterator(Selection selection, long count) {
        this.centerChunkX = selection.centerChunkX();
        this.centerChunkZ = selection.centerChunkZ();
        delegatingIterator = ChunkIteratorFactory.getChunkIterator(new Selection(
                selection.world(),
                0,
                0,
                Math.ceil(selection.radiusX() / (double) CHUNK_SIZE),
                Math.ceil(selection.radiusZ() / (double) CHUNK_SIZE),
                selection.pattern(),
                selection.shape()
        ), (long) Math.ceil(count / (double) (CHUNK_SIZE * CHUNK_SIZE)));
        System.out.println(String.format("WrappingChunkedIterator: %s", this.name()));
    }

    @Override
    public long total() {
        return delegatingIterator.total() * CHUNK_SIZE * CHUNK_SIZE;
    }

    @Override
    public String name() {
        return String.format("chunked_%s", delegatingIterator.name());
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty() || delegatingIterator.hasNext();
    }

    private void fillQueue() {
        final ChunkCoordinate areaCoordinate = delegatingIterator.next();
        for (int x = 0; x < CHUNK_SIZE; x ++)
            for (int z = 0; z < CHUNK_SIZE; z ++)
                queue.add(new ChunkCoordinate(areaCoordinate.x * CHUNK_SIZE + x + centerChunkX, areaCoordinate.z * CHUNK_SIZE + z + centerChunkZ));
    }

    @Override
    public synchronized ChunkCoordinate next() {
        if (queue.isEmpty()) fillQueue();
        assert !queue.isEmpty();
        return queue.poll();
    }
}