package org.popcraft.chunky;

import org.popcraft.chunky.iterator.PatternType;
import org.popcraft.chunky.platform.Border;
import org.popcraft.chunky.platform.World;
import org.popcraft.chunky.platform.util.Location;
import org.popcraft.chunky.platform.util.Vector2;
import org.popcraft.chunky.shape.ShapeType;
import org.popcraft.chunky.util.Parameter;

public class Selection {
    public static final double DEFAULT_CENTER_X = 0d;
    public static final double DEFAULT_CENTER_Z = 0d;
    public static final double DEFAULT_RADIUS = 500d;
    private final Chunky chunky;
    private final World world;
    private final double centerX;
    private final double centerZ;
    private final double radiusX;
    private final double radiusZ;
    private final Parameter pattern;
    private final String shape;
    private final int centerChunkX;
    private final int centerChunkZ;
    private final int radiusChunksX;
    private final int radiusChunksZ;
    private final int diameterChunksX;
    private final int diameterChunksZ;

    public Selection(Chunky chunky, World world, double centerX, double centerZ, double radiusX, double radiusZ, Parameter pattern, String shape) {
        this.chunky = chunky;
        this.world = world;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.radiusX = radiusX;
        this.radiusZ = radiusZ;
        this.pattern = pattern;
        this.shape = shape;
        this.centerChunkX = (int) centerX >> 4;
        this.centerChunkZ = (int) centerZ >> 4;
        this.radiusChunksX = (int) Math.ceil(radiusX / 16f);
        this.radiusChunksZ = (int) Math.ceil(radiusZ / 16f);
        this.diameterChunksX = 2 * radiusChunksX + 1;
        this.diameterChunksZ = 2 * radiusChunksZ + 1;
    }

    public static Builder builder(Chunky chunky, World world) {
        return new Builder(chunky, world);
    }

    public Chunky chunky() {
        return chunky;
    }

    public World world() {
        return this.world;
    }

    public double centerX() {
        return this.centerX;
    }

    public double centerZ() {
        return this.centerZ;
    }

    public double radiusX() {
        return this.radiusX;
    }

    public double radiusZ() {
        return this.radiusZ;
    }

    public Parameter pattern() {
        return this.pattern;
    }

    public String shape() {
        return this.shape;
    }

    public int centerChunkX() {
        return this.centerChunkX;
    }

    public int centerChunkZ() {
        return this.centerChunkZ;
    }

    public int radiusChunksX() {
        return this.radiusChunksX;
    }

    public int radiusChunksZ() {
        return this.radiusChunksZ;
    }

    public int diameterChunksX() {
        return this.diameterChunksX;
    }

    public int diameterChunksZ() {
        return this.diameterChunksZ;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static final class Builder {
        private final Chunky chunky;
        private World world;
        private double centerX = DEFAULT_CENTER_X;
        private double centerZ = DEFAULT_CENTER_Z;
        private double radiusX = DEFAULT_RADIUS;
        private double radiusZ = DEFAULT_RADIUS;
        private Parameter pattern = Parameter.of(PatternType.CONCENTRIC);
        private String shape = ShapeType.SQUARE;

        private Builder(Chunky chunky, World world) {
            this.chunky = chunky;
            this.world = world;
        }

        public Builder world(World world) {
            this.world = world;
            return this;
        }

        public Builder center(double centerX, double centerZ) {
            this.centerX = centerX;
            this.centerZ = centerZ;
            return this;
        }

        public Builder centerX(double centerX) {
            this.centerX = centerX;
            return this;
        }

        public Builder centerZ(double centerZ) {
            this.centerZ = centerZ;
            return this;
        }

        public Builder radius(double radius) {
            this.radiusX = radius;
            this.radiusZ = radius;
            return this;
        }

        public Builder radiusX(double radiusX) {
            this.radiusX = radiusX;
            return this;
        }

        public Builder radiusZ(double radiusZ) {
            this.radiusZ = radiusZ;
            return this;
        }

        public Builder pattern(Parameter pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder shape(String shape) {
            this.shape = shape;
            return this;
        }

        public Builder spawn() {
            Location spawn = world.getSpawn();
            this.centerX = spawn.getX();
            this.centerZ = spawn.getZ();
            return this;
        }

        public Builder worldborder() {
            Border border = world.getWorldBorder();
            Vector2 center = border.getCenter();
            this.centerX = center.getX();
            this.centerZ = center.getZ();
            this.radiusX = border.getRadiusX();
            this.radiusZ = border.getRadiusZ();
            return this;
        }

        public Selection build() {
            return new Selection(chunky, world, centerX, centerZ, radiusX, radiusZ, pattern, shape);
        }
    }
}
