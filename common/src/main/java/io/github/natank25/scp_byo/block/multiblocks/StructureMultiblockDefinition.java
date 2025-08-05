package io.github.natank25.scp_byo.block.multiblocks;

import io.github.natank25.scp_byo.block.multiblocks.in_world.MultiblockBE;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class StructureMultiblockDefinition<BE extends MultiblockBE> extends MultiblockDefinition<BE> {

    public StructureMultiblockDefinition(Identifier id, BEFactory<BE> beFactory) {
        super(id, beFactory);
    }

    @Override
    public boolean canAssemble(World world, BlockPos pos, Direction triggerDirection, PlayerEntity player) {
        return false;
    }

    @Override
    public boolean assemble(World world, BlockPos pos, Direction triggerDirection, PlayerEntity player) {
        return false;
    }
}
