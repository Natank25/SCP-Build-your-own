package io.github.natank25.scp_byo;

import io.github.natank25.scp_byo.advancements.ModCriterions;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.commands.ModCommands;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.mutliblock.BlockPatternsRegistry;
import io.github.natank25.scp_byo.sounds.ModSounds;
import io.github.natank25.scp_byo.utils.ModConstants;
import io.github.natank25.scp_byo.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

public class scp_byo implements ModInitializer {
	public static final String MOD_ID = "scp_byo";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	
	@Override
	public void onInitialize() {
		//region Registration
		
		
		//region Networking
		
		ServerPlayNetworking.registerGlobalReceiver(ModConstants.Networking.GRANT_ADVANCEMENT_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			String criterionName = buf.readString();
			Identifier advancementId = buf.readIdentifier();
			
			server.execute(() -> player.getAdvancementTracker().grantCriterion(server.getAdvancementLoader().get(advancementId), criterionName));
		});
		
		//endregion
		
		
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModSounds.registerModSounds();
		ModBlocksEntities.registerAllBlockEntity();
		ModCommands.registerCommands();
		ModWorldGeneration.generateModWorldGen();
		ModGamerules.registerModGamerules();
		BlockPatternsRegistry.registerModBlockPatterns();
		ModCriterions.registerModCriterions();
		
		FabricDefaultAttributeRegistry.register(ModEntities.SCP_096, Scp_096Entity.setAttributes());
		
		//endregion
		
		
		GeckoLib.initialize();
		
	}
}
