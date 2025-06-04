package io.github.natank25.scp_byo.fabric;

import io.github.natank25.scp_byo.Scp_byoClient;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.block.entity.client.SlidingDoorRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class Scp_byoClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Scp_byoClient.init();
	}
}
