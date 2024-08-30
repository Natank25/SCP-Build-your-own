package io.github.natank25.scp_byo.item.custom;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.natank25.scp_byo.persistent_data.DoesSCP096Exist;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.Objects;

public class SCP096SpawnEggItem extends ArchitecturySpawnEggItem {
	public SCP096SpawnEggItem(RegistrySupplier<? extends EntityType<? extends MobEntity>> type, int primaryColor, int secondaryColor, Settings settings) {
		super(type, primaryColor, secondaryColor, settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		
		if (context.getWorld().isClient()) {
			return ActionResult.SUCCESS;
		} else {
			DoesSCP096Exist doesSCP096Exist = DoesSCP096Exist.get(context.getWorld());
			
			if (doesSCP096Exist.getDoesSCP096Exist()) {
				Objects.requireNonNull(context.getPlayer()).sendMessage(Text.translatable("scp_byo.commands.summonscp096.alreadyexists"), true);
				return ActionResult.CONSUME;
			} else {
				return super.useOnBlock(context);
			}
		}
	}
	
}
