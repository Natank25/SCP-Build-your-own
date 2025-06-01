package io.github.natank25.scp_byo.entity.client.scp_096;

import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class Scp096Renderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<Scp_096Entity, R> {
	public Scp096Renderer(EntityRendererFactory.Context renderManager) {
		super(renderManager, new Scp096Model());
	}

    @Override
    public Identifier getTextureLocation(R renderState) {
        return Utils.newIdentifier("textures/entity/scp_096.png");
    }
}
