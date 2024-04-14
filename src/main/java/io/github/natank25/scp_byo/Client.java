package io.github.natank25.scp_byo;

import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.block.entity.client.SlidingDoorRenderer;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.client.scp_096.Scp096Renderer;
import io.github.natank25.scp_byo.persistent_data.cca.register.ModWorldComponents;
import io.github.natank25.scp_byo.utils.ModConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //Block Entities
        BlockEntityRendererRegistry.register(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY, SlidingDoorRenderer::new);


        // Non opaque blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FONDATION_GLASS, RenderLayer.getTranslucent());

        EntityRendererRegistry.register(ModEntities.SCP_096, Scp096Renderer::new);
        
        
        // Networking
        ClientPlayNetworking.registerGlobalReceiver(ModConstants.Networking.DESTROY_MULTIBLOCK_PACKET_ID, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            client.execute(() ->
                    ModWorldComponents.MULTIBLOCKS.get(client.world).tryDisassemble(pos));
        });
    }
}
