package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.persistent_data.cca.register.ModWorldComponents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
	@Inject(method = "onStateReplaced", at=@At("HEAD"))
	private void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
		ModWorldComponents.MULTIBLOCKS.get(world).tryDisassemble(pos);
	}
}
