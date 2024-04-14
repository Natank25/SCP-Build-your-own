package io.github.natank25.scp_byo.item;

import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.item.custom.SCP096SpawnEggItem;
import io.github.natank25.scp_byo.item.custom.SlidingDoorItem;
import io.github.natank25.scp_byo.item.custom.Wrench;
import io.github.natank25.scp_byo.scp_byo;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum ModItems {
    ;

    public static final ItemGroup SCP_FONDATION_ITEM_GROUP = FabricItemGroup.builder(new Identifier(scp_byo.MOD_ID)).displayName(Text.literal("SCP Fondation")).icon(() -> new ItemStack(ModBlocks.OFFICE_WALL.asItem())).build();

    //region Normal Items
    public static final Item KEYCARD_1 = registerItem("keycard_1", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_2 = registerItem("keycard_2", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_3 = registerItem("keycard_3", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_4 = registerItem("keycard_4", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_5 = registerItem("keycard_5", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_6 = registerItem("keycard_6", new Item(new Item.Settings().maxCount(1)));
    //endregion

    //region Custom Items
    public static final Item WRENCH = registerItem("wrench", new Wrench(new Item.Settings().maxCount(1)));

    public static final Item SLIDING_DOOR_ITEM = registerItem("sliding_door", new SlidingDoorItem(ModBlocks.SLIDING_DOOR, new FabricItemSettings()));

    public static final Item SCP_096_SPAWN_EGG = registerItem("scp_096_spawn_egg", new SCP096SpawnEggItem(ModEntities.SCP_096, 0xECECEC, 0xD8D8D8, new FabricItemSettings()));
    //endregion

    public static void registerModItems() {
        scp_byo.LOGGER.debug("Registering Mod Items for " + scp_byo.MOD_ID);
    }

    private static Item registerItem(String name, Item item) {
        ItemGroupEvents.modifyEntriesEvent(SCP_FONDATION_ITEM_GROUP).register(entries -> entries.add(item));
        return Registry.register(Registries.ITEM, new Identifier(scp_byo.MOD_ID, name), item);
    }

}
