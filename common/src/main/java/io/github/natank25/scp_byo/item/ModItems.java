package io.github.natank25.scp_byo.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.item.custom.SCP096SpawnEggItem;
import io.github.natank25.scp_byo.item.custom.SlidingDoorItem;
import io.github.natank25.scp_byo.item.custom.Wrench;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.function.Function;

import static io.github.natank25.scp_byo.Scp_byo.MOD_ID;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM);
	public static final DeferredRegister<ItemGroup> ITEM_GROUPS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM_GROUP);

	public static final RegistrySupplier<ItemGroup> SCP_FOUNDATION_ITEM_GROUP = ITEM_GROUPS.register(Utils.newIdentifier("main_creative_tab"), () -> CreativeTabRegistry.create(Text.translatable("itemGroup.scp_byo.main_creative_tab"), () -> new ItemStack(ModBlocks.OFFICE_WALL.get().asItem())));
	//region Normal Items
	public static final RegistrySupplier<Item> KEYCARD_1 = registerItem("keycard_1", Item::new, defaultSettings().maxCount(1));
	public static final RegistrySupplier<Item> KEYCARD_2 = registerItem("keycard_2", Item::new, defaultSettings().maxCount(1));
	public static final RegistrySupplier<Item> KEYCARD_3 = registerItem("keycard_3", Item::new, defaultSettings().maxCount(1));
	public static final RegistrySupplier<Item> KEYCARD_4 = registerItem("keycard_4", Item::new, defaultSettings().maxCount(1));
	public static final RegistrySupplier<Item> KEYCARD_5 = registerItem("keycard_5", Item::new, defaultSettings().maxCount(1));
	public static final RegistrySupplier<Item> KEYCARD_6 = registerItem("keycard_6", Item::new, defaultSettings().maxCount(1));
	//region Custom Items
	public static final RegistrySupplier<Item> WRENCH = registerItem("wrench", Wrench::new, defaultSettings().maxCount(1).recipeRemainder(ITEMS.getRegistrar().get(Utils.newIdentifier("wrench"))));
	
	//endregion
	public static final RegistrySupplier<Item> SLIDING_DOOR_ITEM = registerItem("sliding_door", SlidingDoorItem::new, defaultSettings());
	public static final RegistrySupplier<Item> SCP_096_SPAWN_EGG = registerItem("scp_096_spawn_egg", (settings) -> new SCP096SpawnEggItem(ModEntities.SCP_096, settings), defaultSettings());
	
	public static Item.Settings defaultSettings() {
		return new Item.Settings().useItemPrefixedTranslationKey().arch$tab(SCP_FOUNDATION_ITEM_GROUP);
	}
	//endregion

	public static RegistrySupplier<Item> registerItem(String name, Function<Item.Settings, Item> blockFactory, Item.Settings settings) {
		RegistryKey<Item> itemKey = keyOfItem(name);
		return ITEMS.register(Utils.newIdentifier(name), () -> blockFactory.apply(settings.registryKey(itemKey)));
	}

	public static void registerItems() {
		ITEMS.register();
	}

	public static void registerItemGroups(){
		ITEM_GROUPS.register();
	}

	private static RegistryKey<Item> keyOfItem(String name) {
		return RegistryKey.of(RegistryKeys.ITEM, Utils.newIdentifier(name));
	}
}
