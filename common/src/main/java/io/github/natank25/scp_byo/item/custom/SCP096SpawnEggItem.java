package io.github.natank25.scp_byo.item.custom;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class SCP096SpawnEggItem extends ArchitecturySpawnEggItem {
    public SCP096SpawnEggItem(RegistrySupplier<? extends EntityType<? extends MobEntity>> type, int primaryColor, int secondaryColor, Settings settings) {
        super(type, primaryColor, secondaryColor, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
		return ActionResult.SUCCESS;
        /*
        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        } else {
            DoesSCP096Exist doesSCP096Exist = ScpByoDataManager.getInstance(Objects.requireNonNull(context.getWorld().getServer()), context.getWorld()).getDoesSCP096Exists();

            if (doesSCP096Exist.doesSCP096Exists) {
                Objects.requireNonNull(context.getPlayer()).sendMessage(Text.translatable("scp_byo.commands.summonscp096.alreadyexists"), true);
                return ActionResult.CONSUME;
            } else {
                doesSCP096Exist.doesSCP096Exists = true;
                return super.useOnBlock(context);
            }
        }*/
    }

}
