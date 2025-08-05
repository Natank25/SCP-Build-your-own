package io.github.natank25.scp_byo.mixins;

import net.minecraft.block.AbstractBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
	@Inject(method = "onStateReplaced", at = @At("HEAD"))
	private void onStateReplaced(ServerWorld world, BlockPos pos, boolean moved, CallbackInfo ci) {
		//TODO Multiblocks.getServerMultiblocks(world).tryDisassemble(pos);
	}
}
