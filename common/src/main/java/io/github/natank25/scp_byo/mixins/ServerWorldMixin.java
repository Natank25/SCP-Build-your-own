package io.github.natank25.scp_byo.mixins;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

	@Inject(method = "tick", at = @At("RETURN"))
	private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		//TODO Multiblocks.getServerMultiblocks((ServerWorld) (Object) this).tick();
	}
}
