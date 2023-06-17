package org.popcraft.chunky.mixin.events;

import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.popcraft.chunky.events.SchedulingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkTicketManager.class)
public class MixinChunkTicketManager {

//    @Inject(method = "tick", at = @At("RETURN"))
//    private void postTick(CallbackInfoReturnable<Boolean> cir) {
//        SchedulingUtil.invokePostTicketUpdate();
//    }

}
