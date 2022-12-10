package org.popcraft.chunky.iterator;

import org.popcraft.chunky.Selection;
import org.popcraft.chunky.shape.ShapeType;
import org.popcraft.chunky.util.Parameter;

public final class ChunkIteratorFactory {
    private ChunkIteratorFactory() {
    }

    public static ChunkIterator getChunkIterator(final Selection selection, final long count) {
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
        final String shape = selection.shape();
        if (ShapeType.RECTANGLE.equals(shape) || ShapeType.ELLIPSE.equals(shape) || ShapeType.OVAL.equals(shape)) {
            return new Loop2ChunkIterator(selection, count);
        }
        return switch (selection.pattern().getType()) {
            case PatternType.LOOP -> new Loop2ChunkIterator(selection, count);
            case PatternType.SPIRAL -> new SpiralChunkIterator(selection, count);
            case PatternType.CSV -> new CsvChunkIterator(selection, count);
            default -> new ConcentricChunkIterator(selection, count);
        };
    }

    public static ChunkIterator getChunkIterator(final Selection selection) {
        return getChunkIterator(selection, 0);
    }
}
