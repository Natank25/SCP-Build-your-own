package io.github.natank25.scp_byo.persistent_data.cca.register;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.natank25.scp_byo.persistent_data.cca.components.BooleanComponent;
import io.github.natank25.scp_byo.utils.Utils;

public class ModEntitiesComponents implements EntityComponentInitializer {
	
	public static final ComponentKey<BooleanComponent> HAS_SEEN_SCP = ComponentRegistry.getOrCreate(Utils.newIdentifier("has_seen_scp"), BooleanComponent.class);
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(HAS_SEEN_SCP, playerEntity -> new BooleanComponent(false), RespawnCopyStrategy.ALWAYS_COPY);
	}
}
