package io.github.natank25.scp_byo.block;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.block.custom.ElevatorWallBlock;
import io.github.natank25.scp_byo.block.custom.ExtendableBlock;
import io.github.natank25.scp_byo.block.custom.KeycardReaderBlock;
import io.github.natank25.scp_byo.block.custom.SlidingDoor;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.sounds.ModSounds;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.function.Function;
import java.util.function.Supplier;


public enum ModBlocks {
	;
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Scp_byo.MOD_ID, RegistryKeys.BLOCK);
	
	//region Normal Blocks
	public static final RegistrySupplier<Block> CONCRETE_FLOOR = registerBlock("concrete_floor", Block::new, Blocks.STONE.getSettings().strength(5, 1200).requiresTool());
	public static final RegistrySupplier<Block> TILE_FLOOR = registerBlock("tile_floor", Block::new, Blocks.STONE.getSettings().strength(5, 1200).requiresTool());
	public static final RegistrySupplier<Block> ELEVATOR_FLOOR = registerBlock("elevator_floor", Block::new, Blocks.STONE.getSettings().strength(5, 1200).requiresTool());
	public static final RegistrySupplier<Block> DIRTY_METAL = registerBlock("dirty_metal", Block::new, Blocks.STONE.getSettings().strength(5, 1200).requiresTool());
	public static final RegistrySupplier<Block> FOUNDATION_GLASS = registerBlock("foundation_glass", TransparentBlock::new, Blocks.GLASS.getSettings().strength(5, 1200).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(ModBlocks::isNever).solidBlock(ModBlocks::isNever).suffocates(ModBlocks::isNever).blockVision(ModBlocks::isNever));
	//endregion
	
	//region Custom Blocks
	public static final RegistrySupplier<Block> KEYCARD_READER = registerBlock("keycard_reader", KeycardReaderBlock::new,Blocks.IRON_BLOCK.getSettings().strength(5, 1200).requiresTool());
	public static final RegistrySupplier<Block> ELEVATOR_WALL = registerBlock("elevator_wall", ElevatorWallBlock::new, Blocks.STONE.getSettings().strength(5, 1200).requiresTool());
	public static final RegistrySupplier<Block> SLIDING_DOOR = registerBlockWithoutItem("sliding_door", (settings) -> new SlidingDoor(settings, ModSounds.SLIDING_DOOR_OPEN.get(), ModSounds.SLIDING_DOOR_OPEN.get()), Blocks.IRON_BLOCK.getSettings().strength(5, 1200).requiresTool().nonOpaque());
	
	//region Extendable Blocks
	public static final RegistrySupplier<Block> WHITE_WALL = registerBlock("white_wall", (settings) -> new ExtendableBlock(settings, 10), Blocks.STONE.getSettings().strength(5, 1200).requiresTool());
	public static final RegistrySupplier<Block> CONCRETE_WALL = registerBlock("concrete_wall", (settings) -> new ExtendableBlock(settings, 10), Blocks.STONE.getSettings().strength(5, 1200).requiresTool());
	public static final RegistrySupplier<Block> OFFICE_WALL = registerBlock("office_wall", (settings) -> new ExtendableBlock(settings, 10), Blocks.STONE.getSettings().strength(5, 1200).requiresTool());
	//endregion
	
	//endregion
	
	
	//region Predicates
	private static boolean isNever(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return false;
	}
	
	private static boolean isNever(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityType<?> entityType) {
		return false;
	}
	//endregion
	
	private static RegistrySupplier<Block> registerBlock(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
		RegistrySupplier<Block> toReturn = registerBlockWithoutItem(name,blockFactory, settings);

		registerBlockItem(name, toReturn);
		
		return toReturn;
	}
	
	public static <T extends Block> RegistrySupplier<Item> registerBlockItem(String name, RegistrySupplier<T> block) {
		return ModItems.registerItem(name, settings -> new BlockItem(block.get(), settings), ModItems.defaultSettings());
	}
	
	private static RegistrySupplier<Block> registerBlockWithoutItem(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
		RegistryKey<Block> blockKey = keyOfBlock(name);

		return BLOCKS.register(Utils.newIdentifier(name), () -> blockFactory.apply(settings.registryKey(blockKey)));
	}

	private static RegistryKey<Block> keyOfBlock(String name) {
		return RegistryKey.of(RegistryKeys.BLOCK, Utils.newIdentifier(name));
	}
	
	public static void registerModBlocks() {
		BLOCKS.register();
	}
	
	public static void registerTranslucentBlocks() {
		RenderTypeRegistry.register(RenderLayer.getTranslucent(), FOUNDATION_GLASS.get());
	}
}
