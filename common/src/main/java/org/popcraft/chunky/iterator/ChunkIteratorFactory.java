package org.popcraft.chunky.iterator;

import org.popcraft.chunky.Selection;

public class ChunkIteratorFactory {
    public static ChunkIterator getChunkIterator(Selection selection, long count) {
        if (selection.pattern().startsWith("chunked_"))
            return new WrappingChunkedChunkIterator(new Selection(
                    selection.world(),
                    selection.centerX(),
                    selection.centerZ(),
                    selection.radiusX(),
                    selection.radiusZ(),
                    selection.pattern().substring("chunked_".length()),
                    selection.shape()
            ), count);
        switch (selection.shape()) {
            case "rectangle":
            case "oval":
            case "ellipse":
                return new Loop2ChunkIterator(selection, count);
            default:
                break;
        }
        switch (selection.pattern()) {
            case "loop":
                return new Loop2ChunkIterator(selection, count);
            case "spiral":
                return new SpiralChunkIterator(selection, count);
            case "concentric":
            default:
                return new ConcentricChunkIterator(selection, count);
        }
    }

    public static ChunkIterator getChunkIterator(Selection selection) {
        return getChunkIterator(selection, 0);
    }
}
