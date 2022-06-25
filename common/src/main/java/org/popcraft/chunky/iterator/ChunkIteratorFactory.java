package org.popcraft.chunky.iterator;

import org.popcraft.chunky.Selection;
import org.popcraft.chunky.shape.ShapeType;
import org.popcraft.chunky.util.Parameter;

public class ChunkIteratorFactory {
    private ChunkIteratorFactory() {
    }

    public static ChunkIterator getChunkIterator(Selection selection, long count) {
        if (selection.pattern().getType().startsWith("chunked_"))
            return new WrappingChunkedChunkIterator(new Selection(
                    selection.chunky(),
                    selection.world(),
                    selection.centerX(),
                    selection.centerZ(),
                    selection.radiusX(),
                    selection.radiusZ(),
                    Parameter.of(selection.pattern().getType().substring("chunked_".length()), selection.pattern().getValue().orElse(null)),
                    selection.shape()
            ), count);
        switch (selection.shape()) {
            case ShapeType.RECTANGLE:
            case ShapeType.ELLIPSE:
            case ShapeType.OVAL:
                return new Loop2ChunkIterator(selection, count);
            default:
                break;
        }
        switch (selection.pattern().getType()) {
            case PatternType.LOOP:
                return new Loop2ChunkIterator(selection, count);
            case PatternType.SPIRAL:
                return new SpiralChunkIterator(selection, count);
            case PatternType.CSV:
                return new CsvChunkIterator(selection, count);
            case PatternType.CONCENTRIC:
            default:
                return new ConcentricChunkIterator(selection, count);
        }
    }

    public static ChunkIterator getChunkIterator(Selection selection) {
        return getChunkIterator(selection, 0);
    }
}
