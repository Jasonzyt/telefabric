package com.jasonzyt.telefabric.events.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public interface PlayerDeathCallback {
    Event<PlayerDeathCallback> EVENT = EventFactory.createArrayBacked(PlayerDeathCallback.class,
            (listeners) -> (player, source) -> {
                for (PlayerDeathCallback listener : listeners) {
                    listener.onDeath(player, source);
                }
            });

    void onDeath(ServerPlayer player, DamageSource source);
}