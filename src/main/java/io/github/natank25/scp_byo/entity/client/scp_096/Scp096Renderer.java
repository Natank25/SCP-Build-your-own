package io.github.natank25.scp_byo.entity.client.scp_096;

import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Scp096Renderer extends GeoEntityRenderer<Scp_096Entity> {
    public Scp096Renderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new Scp096Model());
    }

    @Override
    public Identifier getTextureLocation(Scp_096Entity animatable) {
        return new Identifier(scp_byo.MOD_ID, "textures/entity/scp_096.png");
    }

    @Override
    public void render(Scp_096Entity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
