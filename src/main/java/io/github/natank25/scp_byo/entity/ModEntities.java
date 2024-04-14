package io.github.natank25.scp_byo.entity;

import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.scp_byo;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public enum ModEntities {
    ;
    public static final EntityType<Scp_096Entity> SCP_096 = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(scp_byo.MOD_ID, "scp_096"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, Scp_096Entity::new)
                    .dimensions(EntityDimensions.changing(0.6f, 2.5f)).build());

}