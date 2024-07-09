package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.persistent_data.ScpByoDataManager;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
	@Inject(method = "onStateReplaced", at=@At("HEAD"))
	private void onStateReplaced(World world, BlockPos pos, BlockState state, boolean moved, CallbackInfo ci) {
		ScpByoDataManager.getInstance(world.getServer(), world).getMultiblocks().tryDisassemble(pos);
	}
}
