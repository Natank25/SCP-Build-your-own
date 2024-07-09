package io.github.natank25.scp_byo.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public abstract class ScpEntity extends PathAwareEntity {
	ScpEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}
}
