package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblock;
import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	
	@Shadow
	private @Nullable ClientWorld world;

	@Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
	private void drawBlockOutlineMixin(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, int color, CallbackInfo ci) {
		if (world == null)
			return;
		Multiblocks multiblocks = Multiblocks.get(world);
		if (multiblocks == null)
			return;
		Optional<? extends Multiblock> optionalMultiblock = multiblocks.getMultiblock(pos);
		if (optionalMultiblock.isPresent()) {
			Multiblock multiblock = optionalMultiblock.get();
			VertexRendering.drawOutline(
					matrices,
					vertexConsumer,
					multiblock.getShape(),
					multiblock.getGlobalBottomLeftVec().getX() - cameraX,
					multiblock.getGlobalBottomLeftVec().getY() - cameraY,
					multiblock.getGlobalBottomLeftVec().getZ() - cameraZ,
					color
			);
			ci.cancel();
		}
	}
}
