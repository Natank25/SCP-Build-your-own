package io.github.natank25.scp_byo.block.entity.client;

import io.github.natank25.scp_byo.block.custom.SlidingDoor;
import io.github.natank25.scp_byo.block.entity.SlidingDoorBlockEntity;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

class SlidingDoorModel extends GeoModel<SlidingDoorBlockEntity> {

	@Override
	public Identifier getModelResource(GeoRenderState geoRenderState) {

		BlockState blockstate = geoRenderState.getGeckolibData(DataTickets.BLOCKSTATE);
		assert blockstate != null;
		return blockstate.get(SlidingDoor.HALF) == DoubleBlockHalf.LOWER ? Utils.newIdentifier("sliding_door_lower") : Utils.newIdentifier("sliding_door_upper");
	}

	@Override
	public Identifier getAnimationResource(SlidingDoorBlockEntity animatable) {
		return Utils.newIdentifier("sliding_door");
	}

	@Override
	public Identifier getTextureResource(GeoRenderState geoRenderState) {
		return Utils.newIdentifier("textures/block/sliding_door.png");
	}
}
