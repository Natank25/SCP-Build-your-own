package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.persistent_data.DoesSCP096Exist;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SummonCommand.class)
class SummonSCP096Mixin {
	
	
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void injectSummonSCP096Command(ServerCommandSource source, RegistryEntry.Reference<EntityType<?>> entityType, Vec3d pos, NbtCompound nbt, boolean initialize, CallbackInfoReturnable<Integer> cir) {
		
		if (ModEntities.SCP_096.getId() == entityType.value().arch$registryName() && initialize) {
			DoesSCP096Exist doesSCP096Exist = DoesSCP096Exist.get(source.getWorld());
			
			if (doesSCP096Exist != null && doesSCP096Exist.getDoesSCP096Exist()) {
				Objects.requireNonNull(source.getPlayer()).sendMessage(Text.translatable("scp_byo.commands.summonscp096.alreadyexists"));
				
				cir.setReturnValue(1);
			}
			
		}
	}
}
