package org.popcraft.chunky.mixin;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("UnnecessaryInterfaceModifier")
@Mixin(ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageMixin {
    @Invoker
    public ChunkHolder invokeGetCurrentChunkHolder(long pos);

}
