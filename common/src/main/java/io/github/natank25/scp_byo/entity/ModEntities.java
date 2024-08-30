package io.github.natank25.scp_byo.entity;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;

public enum ModEntities {
	;
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Scp_byo.MOD_ID, RegistryKeys.ENTITY_TYPE);
	
	public static final RegistrySupplier<EntityType<Scp_096Entity>> SCP_096 = ENTITIES.register(Utils.newIdentifier("scp_096"), Scp_096Entity.TYPE);
	
	public static void registerEntities() {
		ENTITIES.register();
		
		EntityAttributeRegistry.register(SCP_096, Scp_096Entity::setAttributes);
	}
}