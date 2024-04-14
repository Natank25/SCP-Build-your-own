package io.github.natank25.scp_byo.item.client;

import io.github.natank25.scp_byo.item.custom.SlidingDoorItem;
import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

class SlidingDoorItemModel extends GeoModel<SlidingDoorItem> {
    @Override
    public Identifier getModelResource(SlidingDoorItem animatable) {
        return new Identifier(scp_byo.MOD_ID, "geo/sliding_door.geo.json");
    }

    @Override
    public Identifier getTextureResource(SlidingDoorItem animatable) {
        return new Identifier(scp_byo.MOD_ID, "textures/block/sliding_door.png");
    }

    @Override
    public Identifier getAnimationResource(SlidingDoorItem animatable) {
        return new Identifier(scp_byo.MOD_ID, "animations/sliding_door.animation.json");
    }
}
