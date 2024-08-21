package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.persistent_data.ScpByoDataManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin extends WorldMixin {
	
	@Inject(method = "<init>*", at = @At("RETURN"))
	private void scp_byo$init(CallbackInfo ci) {
		scp_byo$DataManager = ScpByoDataManager.getOrCreate(((ServerWorld) (Object) this)); // Override default value from WorldMixin
	}
	
	@Inject(method = "tick", at = @At("RETURN"))
	private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		this.scp_byoGetDataManager().getMultiblocks().tick();
	}
}
