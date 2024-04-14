package io.github.natank25.scp_byo.entity.client.scp_096;

import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

class Scp096Model extends GeoModel<Scp_096Entity> {
    @Override
    public Identifier getModelResource(Scp_096Entity animatable) {
        return new Identifier(scp_byo.MOD_ID, "geo/scp_096.geo.json");
    }

    @Override
    public Identifier getTextureResource(Scp_096Entity animatable) {
        return new Identifier(scp_byo.MOD_ID, "textures/entity/scp_096.png");
    }

    @Override
    public Identifier getAnimationResource(Scp_096Entity animatable) {
        return new Identifier(scp_byo.MOD_ID, "animations/scp_096.animation.json");
    }

}
