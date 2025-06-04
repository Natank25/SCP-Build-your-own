package io.github.natank25.scp_byo;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.block.entity.client.SlidingDoorRenderer;

public class Scp_byoClient {
    public static void init(){
        BlockEntityRendererRegistry.register(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
        ModBlocks.registerTranslucentBlocks();
    }
}
