package io.github.natank25.scp_byo.block.entity.client;

import io.github.natank25.scp_byo.block.custom.SlidingDoor;
import io.github.natank25.scp_byo.block.entity.SlidingDoorBlockEntity;
import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

class SlidingDoorModel extends GeoModel<SlidingDoorBlockEntity> {
    @Override
    public Identifier getModelResource(SlidingDoorBlockEntity animatable) {
        return animatable.getCachedState().get(SlidingDoor.HALF) == DoubleBlockHalf.LOWER ? new Identifier(scp_byo.MOD_ID, "geo/sliding_door_lower.geo.json") : new Identifier(scp_byo.MOD_ID, "geo/sliding_door_upper.geo.json");
    }

    @Override
    public Identifier getTextureResource(SlidingDoorBlockEntity animatable) {
        return new Identifier(scp_byo.MOD_ID, "textures/block/sliding_door.png");

    }

    @Override
    public Identifier getAnimationResource(SlidingDoorBlockEntity animatable) {
        return new Identifier(scp_byo.MOD_ID, "animations/sliding_door.animation.json");

    }
}
