package org.popcraft.chunky.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.File;

@Mixin(ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageMixin {
    @Invoker("getChunkHolder")
    public ChunkHolder IGetChunkHolder(long pos);

    @Invoker("getUpdatedChunkNbt")
    public NbtCompound getUpdatedChunkNbt(ChunkPos pos);

    @Accessor("chunksToUnload")
    public Long2ObjectLinkedOpenHashMap<ChunkHolder> getChunksToUnload();

    @Accessor("saveDir")
    public File getSaveDir();
}
