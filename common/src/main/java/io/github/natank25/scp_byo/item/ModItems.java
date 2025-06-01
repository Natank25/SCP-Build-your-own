package io.github.natank25.scp_byo.item;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.item.custom.SCP096SpawnEggItem;
import io.github.natank25.scp_byo.item.custom.Wrench;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

import static io.github.natank25.scp_byo.Scp_byo.MOD_ID;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM);

	public static final ItemGroup SCP_FONDATION_ITEM_GROUP = CreativeTabRegistry.create(Text.translatable("itemGroup.scp_byo.main_creative_tab"), () -> new ItemStack(ModBlocks.OFFICE_WALL.get().asItem()));
	//region Normal Items
	public static final RegistrySupplier<Item> KEYCARD_1 = registerItem("keycard_1", () -> new Item(defaultSettings().maxCount(1)));
	public static final RegistrySupplier<Item> KEYCARD_2 = registerItem("keycard_2", () -> new Item(defaultSettings().maxCount(1)));
	public static final RegistrySupplier<Item> KEYCARD_3 = registerItem("keycard_3", () -> new Item(defaultSettings().maxCount(1)));
	public static final RegistrySupplier<Item> KEYCARD_4 = registerItem("keycard_4", () -> new Item(defaultSettings().maxCount(1)));
	public static final RegistrySupplier<Item> KEYCARD_5 = registerItem("keycard_5", () -> new Item(defaultSettings().maxCount(1)));
	public static final RegistrySupplier<Item> KEYCARD_6 = registerItem("keycard_6", () -> new Item(defaultSettings().maxCount(1)));
	//region Custom Items
	public static final RegistrySupplier<Item> WRENCH = registerItem("wrench", () -> new Wrench(defaultSettings().maxCount(1).recipeRemainder(ITEMS.getRegistrar().get(Utils.newIdentifier("wrench")))));
	
	//endregion
	public static final RegistrySupplier<Item> SLIDING_DOOR_ITEM = registerItem("sliding_door", ModItems::getSlidingDoorItem);
	public static final RegistrySupplier<Item> SCP_096_SPAWN_EGG = registerItem("scp_096_spawn_egg", () -> new SCP096SpawnEggItem(ModEntities.SCP_096, 0xECECEC, 0xD8D8D8, defaultSettings()));
	
	public static Item.Settings defaultSettings() {
		return new Item.Settings().arch$tab(SCP_FONDATION_ITEM_GROUP);
	}
	//endregion
	
	public static RegistrySupplier<Item> registerItem(String name, Supplier<Item> item) {
		return ITEMS.register(Utils.newIdentifier(name), item);
	}
	
	
	public static void registerItems() {
		
		ITEMS.register();
	}
	
	@ExpectPlatform
	private static Item getSlidingDoorItem() {
		// Just throw an error, the content should get replaced at runtime by Architectury API
		// Something is terribly wrong if this is not replaced.
		throw new AssertionError();
	}
}
