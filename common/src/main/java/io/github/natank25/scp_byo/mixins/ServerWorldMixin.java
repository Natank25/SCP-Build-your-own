package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblocks;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

	@Inject(method = "tick", at = @At("RETURN"))
	private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		Objects.requireNonNull(Multiblocks.get((ServerWorld) (Object) this)).tick();
	}
}
