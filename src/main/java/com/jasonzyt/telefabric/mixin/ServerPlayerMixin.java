package com.jasonzyt.telefabric.mixin;

import com.jasonzyt.telefabric.events.callbacks.PlayerDeathCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "die", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        PlayerDeathCallback.EVENT.invoker().onDeath((ServerPlayer) (Object) this, source);
    }
}