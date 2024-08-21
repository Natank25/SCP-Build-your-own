package io.github.natank25.scp_byo.mixins;

import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientWorld.class)
public class ClientWorldMixin extends WorldMixin {
	
	@Inject(method = "tick", at = @At("RETURN"))
	private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		this.scp_byoGetDataManager().getMultiblocks().tick();
	}
}
