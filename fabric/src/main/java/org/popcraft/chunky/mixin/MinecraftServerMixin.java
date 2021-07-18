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

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setFavicon(Lnet/minecraft/server/ServerMetadata;)V", ordinal = 0), method = "runServer")
    private void onServerStarted(CallbackInfo ci) {
        SchedulerUtils.invokeOnServerStart((MinecraftServer) (Object) this);
    }

    @Inject(at = @At("HEAD"), method = "shutdown")
    private void onServerStopping(CallbackInfo ci) {
        SchedulerUtils.invokeOnServerStop((MinecraftServer) (Object) this);
    }

}
