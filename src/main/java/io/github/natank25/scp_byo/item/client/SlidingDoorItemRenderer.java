package io.github.natank25.scp_byo.item.client;

import io.github.natank25.scp_byo.item.custom.SlidingDoorItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SlidingDoorItemRenderer extends GeoItemRenderer<SlidingDoorItem> {
    public SlidingDoorItemRenderer() {
        super(new SlidingDoorItemModel());
    }
}
