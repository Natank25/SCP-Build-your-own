package io.github.natank25.scp_byo.block.entity.neoforge;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class ModBlocksEntitiesImpl {
    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityType.BlockEntityFactory<? extends T> factory, Block block) {
        return new BlockEntityType<>(factory, block);
    }
}
