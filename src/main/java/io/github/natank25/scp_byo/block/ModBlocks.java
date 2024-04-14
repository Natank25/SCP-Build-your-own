package io.github.natank25.scp_byo.block;

import io.github.natank25.scp_byo.block.custom.ElevatorWallBlock;
import io.github.natank25.scp_byo.block.custom.ExtendableBlock;
import io.github.natank25.scp_byo.block.custom.KeycardReaderBlock;
import io.github.natank25.scp_byo.block.custom.SlidingDoor;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.scp_byo;
import io.github.natank25.scp_byo.sounds.ModSounds;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public enum ModBlocks {
    ;


    //region Normal Blocks
    public static final Block CONCRETE_FLOOR = registerBlock("concrete_floor", new Block(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool()));
    public static final Block TILE_FLOOR = registerBlock("tile_floor", new Block(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool()));
    public static final Block ELEVATOR_FLOOR = registerBlock("elevator_floor", new Block(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool()));
    public static final Block DIRTY_METAL = registerBlock("dirty_metal", new Block(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool()));
    public static final Block FONDATION_GLASS = registerBlock("fondation_glass", new GlassBlock(FabricBlockSettings.of(Material.GLASS).strength(5, 1200).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(ModBlocks::isNever).solidBlock(ModBlocks::isNever).suffocates(ModBlocks::isNever).blockVision(ModBlocks::isNever)));
    //endregion

    //region Custom Blocks
    public static final Block KEYCARD_READER = registerBlock("keycard_reader", new KeycardReaderBlock(FabricBlockSettings.of(Material.METAL).strength(5, 1200).requiresTool(), 20, false, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON));
    public static final Block ELEVATOR_WALL = registerBlock("elevator_wall", new ElevatorWallBlock(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool()));
    public static final Block SLIDING_DOOR = registerBlockWithoutItem("sliding_door",new SlidingDoor(FabricBlockSettings.of(Material.METAL).strength(5, 1200).requiresTool().nonOpaque(), ModSounds.SLIDING_DOOR_OPEN, ModSounds.SLIDING_DOOR_OPEN));

    //region Extendable Blocks
    public static final Block WHITE_WALL = registerBlock("white_wall", new ExtendableBlock(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool(), 10));
    public static final Block CONCRETE_WALL = registerBlock("concrete_wall", new ExtendableBlock(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool(), 10));
    public static final Block OFFICE_WALL = registerBlock("office_wall", new ExtendableBlock(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool(), 10));
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

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block, ModItems.SCP_FONDATION_ITEM_GROUP);
        return Registry.register(Registries.BLOCK, new Identifier(scp_byo.MOD_ID, name), block);
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(scp_byo.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block, ItemGroup itemGroup) {
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> entries.add(block));
        Registry.register(Registries.ITEM, new Identifier(scp_byo.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        scp_byo.LOGGER.debug("Registering ModBlocks for " + scp_byo.MOD_ID);
    }
}
