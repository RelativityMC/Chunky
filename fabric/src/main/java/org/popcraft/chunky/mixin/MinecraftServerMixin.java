package org.popcraft.chunky.mixin;

import net.minecraft.server.MinecraftServer;
import org.popcraft.chunky.scheduler.SchedulerUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onPostTickWorld(CallbackInfo ci) {
        SchedulerUtils.processQueue();
    }

}
