package org.popcraft.chunky.iterator;

import org.popcraft.chunky.Selection;
import org.popcraft.chunky.util.ChunkCoordinate;

import java.util.LinkedList;
import java.util.Queue;

public class WrappingChunkedChunkIterator implements ChunkIterator {

    private static final float CHUNK_SIZE = 8;
    private final ChunkIterator delegatingIterator;
    private final Queue<ChunkCoordinate> queue = new LinkedList<>();

    public WrappingChunkedChunkIterator(Selection selection, long count) {
        delegatingIterator = ChunkIteratorFactory.getChunkIterator(new Selection(
                selection.world(),
                selection.centerX(),
                selection.centerZ(),
                Math.ceil(selection.radiusX() / CHUNK_SIZE),
                Math.ceil(selection.radiusZ() / CHUNK_SIZE),
                selection.pattern(),
                selection.shape()
        ), (long) Math.ceil(count / (CHUNK_SIZE * CHUNK_SIZE)));
    }

    @Override
    public long total() {
        return (long) (delegatingIterator.total() * CHUNK_SIZE * CHUNK_SIZE);
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
                queue.add(new ChunkCoordinate(areaCoordinate.x * (int) CHUNK_SIZE + x, areaCoordinate.z + (int) CHUNK_SIZE + z));
    }

    @Override
    public synchronized ChunkCoordinate next() {
        if (queue.isEmpty()) fillQueue();
        assert !queue.isEmpty();
        return queue.poll();
    }
}
