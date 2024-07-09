package io.github.natank25.scp_byo.fabric.item.client;

import io.github.natank25.scp_byo.fabric.item.custom.SlidingDoorItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SlidingDoorItemRenderer extends GeoItemRenderer<SlidingDoorItem> {
    public SlidingDoorItemRenderer() {
        super(new SlidingDoorItemModel());
    }
}
