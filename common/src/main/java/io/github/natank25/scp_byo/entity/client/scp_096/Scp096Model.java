package io.github.natank25.scp_byo.entity.client.scp_096;

import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

class Scp096Model extends GeoModel<Scp_096Entity> {
	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		return Utils.newIdentifier( "scp_096");
	}

	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		return Utils.newIdentifier("textures/entity/scp_096.png");
	}

	@Override
	public Identifier getAnimationResource(Scp_096Entity animatable) {
		return Utils.newIdentifier("scp_096");
	}
}
