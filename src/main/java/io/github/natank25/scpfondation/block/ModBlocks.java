package io.github.natank25.scpfondation.block;

import io.github.natank25.scpfondation.ScpFondation;
import io.github.natank25.scpfondation.block.custom.ElevatorFloorBlock;
import io.github.natank25.scpfondation.block.custom.ExtendableBlock;
import io.github.natank25.scpfondation.block.custom.KeycardReaderBlock;
import io.github.natank25.scpfondation.block.custom.SlidingDoorBlock;
import io.github.natank25.scpfondation.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block CONCRETE_FLOOR = registerBlock("concrete_floor", new Block(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool()), ModItems.SCP_FONDATION_ITEM_GROUP);
    public static final Block TILE_FLOOR = registerBlock("tile_floor", new Block(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool()), ModItems.SCP_FONDATION_ITEM_GROUP);
    public static final Block ELEVATOR_FLOOR = registerBlock("elevator_floor", new Block(FabricBlockSettings.of(Material.STONE).strength(5, 1200).requiresTool()), ModItems.SCP_FONDATION_ITEM_GROUP);

    public static final Block KEYCARD_READER = registerBlock("keycard_reader", new KeycardReaderBlock(FabricBlockSettings.of(Material.METAL).strength(5, 1200).requiresTool(), 20, false, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON), ModItems.SCP_FONDATION_ITEM_GROUP);
    public static final Block SLIDING_DOOR = registerBlock("sliding_door", new SlidingDoorBlock(FabricBlockSettings.of(Material.METAL).strength(5,1200).requiresTool()), ModItems.SCP_FONDATION_ITEM_GROUP);

    public static final Block WHITE_WALL = registerBlock("white_wall", new ExtendableBlock(FabricBlockSettings.of(Material.STONE).strength(5,1200).requiresTool(),3), ModItems.SCP_FONDATION_ITEM_GROUP);
    public static final Block CONCRETE_WALL = registerBlock("concrete_wall", new ExtendableBlock(FabricBlockSettings.of(Material.STONE).strength(5,1200).requiresTool(),3), ModItems.SCP_FONDATION_ITEM_GROUP);
    public static final Block OFFICE_WALL = registerBlock("office_wall", new ExtendableBlock(FabricBlockSettings.of(Material.STONE).strength(5,1200).requiresTool(),3), ModItems.SCP_FONDATION_ITEM_GROUP);

    public static final Block ELEVATOR_WALL = registerBlock("elevator_wall", new ElevatorFloorBlock(FabricBlockSettings.of(Material.STONE).strength(5,1200).requiresTool()), ModItems.SCP_FONDATION_ITEM_GROUP);


    public static Block registerBlock(String name, Block block, ItemGroup itemGroup) {
        registerBlockItem(name, block, itemGroup);
        return Registry.register(Registries.BLOCK, new Identifier(ScpFondation.MOD_ID, name), block);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static Item registerBlockItem(String name, Block block, ItemGroup itemGroup) {
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> entries.add(block));
        return Registry.register(Registries.ITEM, new Identifier(ScpFondation.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        ScpFondation.LOGGER.debug("Registering ModBlocks for " + ScpFondation.MOD_ID);
    }
}
