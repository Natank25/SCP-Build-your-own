package io.github.natank25.scpfondation;

import io.github.natank25.scpfondation.block.ModBlocks;
import io.github.natank25.scpfondation.item.ModItems;
import io.github.natank25.scpfondation.sounds.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScpFondation implements ModInitializer {
	public static final String MOD_ID = "scpfondation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModSounds.registerModSounds();
	}
}
