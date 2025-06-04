package io.github.natank25.scp_byo.item.client;

import io.github.natank25.scp_byo.item.custom.SlidingDoorItem;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

class SlidingDoorItemModel extends GeoModel<SlidingDoorItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Utils.newIdentifier("sliding_door");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Utils.newIdentifier("textures/block/sliding_door.png");
    }

    @Override
    public Identifier getAnimationResource(SlidingDoorItem animatable) {
        return Utils.newIdentifier("sliding_door");
    }

}
