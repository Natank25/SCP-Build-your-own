package io.github.natank25.scp_byo.item.custom;

import io.github.natank25.scp_byo.persistent_data.DoesSCP096Exist;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class SCP096SpawnEggItem extends SpawnEggItem {
    public SCP096SpawnEggItem(EntityType<? extends MobEntity> type, int primaryColor, int secondaryColor, Settings settings) {
        super(type, primaryColor, secondaryColor, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        } else {
            DoesSCP096Exist doesSCP096Exist = DoesSCP096Exist.getServerState(context.getWorld().getServer());

            if (doesSCP096Exist.doesSCP096Exists) {
                context.getPlayer().sendMessage(Text.translatable("scp_byo.commands.summonscp096.alreadyexists"), true);
                return ActionResult.CONSUME;
            } else {
                doesSCP096Exist.doesSCP096Exists = true;
                return super.useOnBlock(context);
            }
        }
    }
}
