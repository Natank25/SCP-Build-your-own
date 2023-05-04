package io.github.natank25.scp_byo.item;

import io.github.natank25.scp_byo.scp_byo;
import io.github.natank25.scp_byo.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final ItemGroup SCP_FONDATION_ITEM_GROUP = FabricItemGroup.builder(new Identifier(scp_byo.MOD_ID))
            .displayName(Text.literal("SCP Fondation"))
            .icon(() -> new ItemStack(ModBlocks.OFFICE_WALL.asItem()))
            .build();

    public static final Item KEYCARD_1 = registerItem("keycard_1", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_2 = registerItem("keycard_2", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_3 = registerItem("keycard_3", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_4 = registerItem("keycard_4", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_5 = registerItem("keycard_5", new Item(new Item.Settings().maxCount(1)));
    public static final Item KEYCARD_6 = registerItem("keycard_6", new Item(new Item.Settings().maxCount(1)));
    public static final Item WRENCH = registerItem("wrench", new Item(new Item.Settings()));

    public static void registerModItems() {
        scp_byo.LOGGER.debug("Registering Mod Items for " + scp_byo.MOD_ID);
    }

    private static Item registerItem(String name, Item item) {
        ItemGroupEvents.modifyEntriesEvent(ModItems.SCP_FONDATION_ITEM_GROUP).register(entries -> entries.add(item));
        return Registry.register(Registries.ITEM, new Identifier(scp_byo.MOD_ID, name), item);
    }
}
