package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
	@Inject(method = "onStateReplaced", at = @At("HEAD"))
	private void onStateReplaced(ServerWorld world, BlockPos pos, boolean moved, CallbackInfo ci) {
		Multiblocks.get(world).tryDisassemble(pos);
	}
}
