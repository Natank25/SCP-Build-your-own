package io.github.natank25.scp_byo.block.entity.client;

import io.github.natank25.scp_byo.block.entity.SlidingDoorBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SlidingDoorRenderer extends GeoBlockRenderer<SlidingDoorBlockEntity> {
    public SlidingDoorRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new SlidingDoorModel());
    }
    
}
