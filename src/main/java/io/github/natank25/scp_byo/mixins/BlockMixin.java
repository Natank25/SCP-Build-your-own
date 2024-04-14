package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.persistent_data.cca.register.ModWorldComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
	@Inject(method = "onBroken", at=@At("HEAD"))
	private void onBroken(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
		ModWorldComponents.MULTIBLOCKS.get(world).tryDisassemble(pos);
	}
	
	
	@Inject(method = "onDestroyedByExplosion", at=@At("HEAD"))
	private void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion, CallbackInfo ci){
		ModWorldComponents.MULTIBLOCKS.get(world).tryDisassemble(pos);
	}
}
