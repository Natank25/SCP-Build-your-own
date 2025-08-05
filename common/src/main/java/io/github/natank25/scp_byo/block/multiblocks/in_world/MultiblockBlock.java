package io.github.natank25.scp_byo.block.multiblocks.in_world;

import com.mojang.serialization.MapCodec;
import io.github.natank25.scp_byo.block.multiblocks.MultiblockDefinition;
import io.github.natank25.scp_byo.sounds.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MultiblockBlock<BE extends MultiblockBE> extends Block implements BlockEntityProvider {

    private final MultiblockDefinition<BE> definition;

    protected MultiblockBlock(Settings settings, MultiblockDefinition<BE> definition) {
        super(settings);
        this.definition = definition;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return definition.createBlockEntity(pos, state);
    }
}
