package io.github.natank25.scp_byo.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public interface WorldSyncCallback {
	Event<WorldSyncCallback> EVENT = EventFactory.createLoop();
	
	/**
	 * Called when a player starts tracking a world (eg. by joining it).
	 */
	void onPlayerStartTracking(ServerPlayerEntity player, ServerWorld world);
}
