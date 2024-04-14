package io.github.natank25.scp_byo.block.entity;

import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.scp_byo;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public enum ModBlocksEntities {
    ;

    public static void registerAllBlockEntity() {
        scp_byo.LOGGER.debug("Registered block entities for SCP: BYO");
    }

    public static final BlockEntityType<SlidingDoorBlockEntity> SLIDING_DOOR_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(scp_byo.MOD_ID, "sliding_door_entity"), FabricBlockEntityTypeBuilder.create(SlidingDoorBlockEntity::new, ModBlocks.SLIDING_DOOR).build());


}
