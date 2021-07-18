package org.popcraft.chunky.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.popcraft.chunky.scheduler.SchedulerUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin {

    @Shadow @Final private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;findAmbiguities(Lcom/mojang/brigadier/AmbiguityConsumer;)V"), method = "<init>")
    private void onRegister(CallbackInfo ci) {
        SchedulerUtils.invokeRegisterCommand(this.dispatcher);
    }

}
