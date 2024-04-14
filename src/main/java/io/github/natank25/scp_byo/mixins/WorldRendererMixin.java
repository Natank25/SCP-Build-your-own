package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.mutliblock.Multiblock;
import io.github.natank25.scp_byo.persistent_data.cca.register.ModWorldComponents;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
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

    @Shadow private @Nullable ClientWorld world;

    @Shadow
    private static void drawCuboidShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {
    }

    @Inject(method="drawBlockOutline", at=@At("HEAD"), cancellable = true)
    private void drawBlockOutlineMixin(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci) {
		Optional<Multiblock> optionalMultiblock = this.world.getComponent(ModWorldComponents.MULTIBLOCKS).getMultiblock(pos);
        if(optionalMultiblock.isPresent()){
            Multiblock multiblock = optionalMultiblock.get();
            drawCuboidShapeOutline(
                    matrices,
                    vertexConsumer,
                    multiblock.getShape(),
                    multiblock.getGlobalBottomLeftVec().getX() - cameraX,
                    multiblock.getGlobalBottomLeftVec().getY() - cameraY,
                    multiblock.getGlobalBottomLeftVec().getZ() - cameraZ,
                    0.0F, 0.0F, 0.0F, 0.4F
            );
            ci.cancel();
        }
    }
}
