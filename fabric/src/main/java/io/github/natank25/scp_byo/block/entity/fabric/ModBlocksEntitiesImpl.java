package io.github.natank25.scp_byo.block.entity.fabric;

import io.github.natank25.scp_byo.Scp_byo;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class ModBlocksEntitiesImpl {
    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityType.BlockEntityFactory<? extends T> factory, Block block) {
        FabricBlockEntityTypeBuilder.Factory<T> fabricFactory = factory::create;
        return FabricBlockEntityTypeBuilder.create(fabricFactory, block).build();
    }
}
