package io.github.natank25.scp_byo.block;

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
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.function.Supplier;


public enum ModBlocks {
	;
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Scp_byo.MOD_ID, RegistryKeys.BLOCK);
	
	//region Normal Blocks
	public static final RegistrySupplier<Block> CONCRETE_FLOOR = registerBlock("concrete_floor", () -> new Block(AbstractBlock.Settings.of(Material.STONE).strength(5, 1200).requiresTool()));
	public static final RegistrySupplier<Block> TILE_FLOOR = registerBlock("tile_floor", () -> new Block(AbstractBlock.Settings.of(Material.STONE).strength(5, 1200).requiresTool()));
	public static final RegistrySupplier<Block> ELEVATOR_FLOOR = registerBlock("elevator_floor", () -> new Block(AbstractBlock.Settings.of(Material.STONE).strength(5, 1200).requiresTool()));
	public static final RegistrySupplier<Block> DIRTY_METAL = registerBlock("dirty_metal", () -> new Block(AbstractBlock.Settings.of(Material.STONE).strength(5, 1200).requiresTool()));
	public static final RegistrySupplier<Block> FONDATION_GLASS = registerBlock("fondation_glass", () -> new GlassBlock(AbstractBlock.Settings.of(Material.GLASS).strength(5, 1200).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(ModBlocks::isNever).solidBlock(ModBlocks::isNever).suffocates(ModBlocks::isNever).blockVision(ModBlocks::isNever)));
	//endregion
	
	//region Custom Blocks
	public static final RegistrySupplier<Block> KEYCARD_READER = registerBlock("keycard_reader", () -> new KeycardReaderBlock(AbstractBlock.Settings.of(Material.METAL).strength(5, 1200).requiresTool()));
	public static final RegistrySupplier<Block> ELEVATOR_WALL = registerBlock("elevator_wall", () -> new ElevatorWallBlock(AbstractBlock.Settings.of(Material.STONE).strength(5, 1200).requiresTool()));
	public static final RegistrySupplier<Block> SLIDING_DOOR = registerBlockWithoutItem("sliding_door", () -> new SlidingDoor(AbstractBlock.Settings.of(Material.METAL).strength(5, 1200).requiresTool().nonOpaque(), ModSounds.SLIDING_DOOR_OPEN.get(), ModSounds.SLIDING_DOOR_OPEN.get()));
	
	//region Extendable Blocks
	public static final RegistrySupplier<Block> WHITE_WALL = registerBlock("white_wall", () -> new ExtendableBlock(AbstractBlock.Settings.of(Material.STONE).strength(5, 1200).requiresTool(), 10));
	public static final RegistrySupplier<Block> CONCRETE_WALL = registerBlock("concrete_wall", () -> new ExtendableBlock(AbstractBlock.Settings.of(Material.STONE).strength(5, 1200).requiresTool(), 10));
	public static final RegistrySupplier<Block> OFFICE_WALL = registerBlock("office_wall", () -> new ExtendableBlock(AbstractBlock.Settings.of(Material.STONE).strength(5, 1200).requiresTool(), 10));
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
	
	private static RegistrySupplier<Block> registerBlock(String name, Supplier<Block> block) {
		RegistrySupplier<Block> toReturn = registerBlockWithoutItem(name, block);
		
		registerBlockItem(name, toReturn);
		
		return toReturn;
	}
	
	public static <T extends Block> RegistrySupplier<Item> registerBlockItem(String name, RegistrySupplier<T> block) {
		return ModItems.ITEMS.register(Utils.newIdentifier(name), () -> new BlockItem(block.get(), ModItems.defaultSettings()));
	}
	
	private static RegistrySupplier<Block> registerBlockWithoutItem(String name, Supplier<Block> block) {
		return BLOCKS.register(Utils.newIdentifier(name), block);
	}
	
	
	public static void registerModBlocks() {
		BLOCKS.register();
	}
	
	public static void registerTranslucentBlocks() {
		RenderTypeRegistry.register(RenderLayer.getTranslucent(), FONDATION_GLASS.get());
	}
}
