package org.popcraft.chunky.mixin.events;

import net.minecraft.server.MinecraftServer;
import org.popcraft.chunky.ChunkyFabric;
import org.popcraft.chunky.events.SchedulingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@SuppressWarnings("ConstantConditions")
@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;loadFavicon()Ljava/util/Optional;"))
    private void onServerStarted(CallbackInfo ci) {
        try {
            ChunkyFabric.SERVER_STARTED.accept((MinecraftServer) (Object) this);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void onServerStopping(CallbackInfo ci) {
        try {
            ChunkyFabric.SERVER_STOPPING.accept((MinecraftServer) (Object) this);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Inject(method = "runTasksTillTickEnd", at = @At("RETURN"))
    private void onTickEnd(CallbackInfo ci) {
        try {
            ChunkyFabric.SERVER_TICK_END.accept((MinecraftServer) (Object) this);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        SchedulingUtil.invokeTickEnd();
    }

}
