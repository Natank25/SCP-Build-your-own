package io.github.natank25.scp_byo;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.github.natank25.scp_byo.advancements.ModCriterions;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.block.entity.client.SlidingDoorRenderer;
import io.github.natank25.scp_byo.commands.ModCommands;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.client.scp_096.Scp096Renderer;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.sounds.ModSounds;
import io.github.natank25.scp_byo.world.gen.ModWorldGeneration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import software.bernie.geckolib.GeckoLib;

public class Scp_byo
{
	public static final String MOD_ID = "scp_byo";

	public static void init() {
		//region registering
		
		ModSounds.registerModSounds();
		
		ModBlocks.registerModBlocks();
		ModItems.registerItems();
		
		ModEntities.registerEntities();
		ModBlocksEntities.registerAllBlockEntity();
		ModCriterions.registerModCriterions();
		ModCommands.registerCommands();
		//endregion
		
		ModWorldGeneration.generateModWorldGen();
		ModGamerules.registerModGamerules();
		
		EnvExecutor.runInEnv(Env.CLIENT, () -> Client::onInitializeClient);
		
		GeckoLib.initialize();
		
		
	}
	
	@Environment(EnvType.CLIENT)
	public static class Client{
		
		
		@Environment(EnvType.CLIENT)
		public static void onInitializeClient() {
			//Block Entities
			// BlockEntityRendererRegistry.register(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
			
			
			// Non opaque blocks
			
			EntityRendererRegistry.register(ModEntities.SCP_096, Scp096Renderer::new);
			
			
			// Networking
        /*
        ClientPlayNetworking.registerGlobalReceiver(ModConstants.Networking.DESTROY_MULTIBLOCK_PACKET_ID, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            client.execute(() ->
                    ModWorldComponents.MULTIBLOCKS.get(client.world).tryDisassemble(pos));
        });
        */
		}
	}
}
