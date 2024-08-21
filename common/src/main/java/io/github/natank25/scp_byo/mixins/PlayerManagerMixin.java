package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.events.WorldSyncCallback;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	
	@Inject(method = "sendWorldInfo", at = @At("RETURN"))
	private void sendWorldInfo(ServerPlayerEntity player, ServerWorld world, CallbackInfo ci) {
		WorldSyncCallback.EVENT.invoker().onPlayerStartTracking(player, world);
	}
	
}
