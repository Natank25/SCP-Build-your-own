package io.github.natank25.scpfondation.block.custom;

import io.github.natank25.scpfondation.block.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ExtendableBlock extends Block {
    public static final IntProperty POS = IntProperty.of("pos", 1, 25);
    public static final EnumProperty<BlockPlace> PLACE = EnumProperty.of("place", BlockPlace.class);
    private final int maxHeight;

    public ExtendableBlock(AbstractBlock.Settings properties, int maxHigh) {
        super(properties);
        this.maxHeight = maxHigh;
        this.setDefaultState(this.getDefaultState().with(POS, 1).with(PLACE, BlockPlace.LOWER));
    }


    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.getBlockState(pos.down()).getBlock() != state.getBlock()) { // If block down isn't same block -> set block to LOWER OR UPPER if block down is SlidingDoor
            if (world.getBlockState(pos.down()).getBlock() == ModBlocks.SLIDING_DOOR) { // If block down is sliding door -> set block to UPPER != set block to LOWER
                world.setBlockState(pos, state.with(POS, 1).with(PLACE, BlockPlace.UPPER), 3);
            } else {
                world.setBlockState(pos, state.with(POS, 1).with(PLACE, BlockPlace.LOWER), 3);
            }

        } else if (world.getBlockState(pos.down()).get(POS) >= 1 && world.getBlockState(pos.down()).get(POS) != maxHeight && world.getBlockState(pos.up()).getBlock() == state.getBlock() && state.get(POS) != maxHeight) { // If block down POS is >= 1 AND block down isn't max height AND block above isn't max height AND pos of this block isn't max height -> set block to BETWEEN
            world.setBlockState(pos, state.with(POS, world.getBlockState(pos.down()).get(POS) + 1).with(PLACE, BlockPlace.BETWEEN), 3);
        } else {

            if (world.getBlockState(pos.down()).get(POS) == maxHeight) { // If block down is max height -> set block to LOWER != set block to UPPER
                world.setBlockState(pos, state.with(POS, 1).with(PLACE, BlockPlace.LOWER), 3);
            } else {
                world.setBlockState(pos, state.with(POS, world.getBlockState(pos.down()).get(POS) + 1).with(PLACE, BlockPlace.UPPER), 3);
            }
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockState(pos.down()).getBlock() != state.getBlock()) { // If block down isn't same block -> set block to LOWER OR UPPER if block down is SlidingDoor
            if (world.getBlockState(pos.down()).getBlock() == ModBlocks.SLIDING_DOOR) { // If block down is sliding door -> set block to UPPER != set block to LOWER
                world.setBlockState(pos, state.with(POS, 1).with(PLACE, BlockPlace.UPPER), 3);
            } else {
                world.setBlockState(pos, state.with(POS, 1).with(PLACE, BlockPlace.LOWER), 3);
            }

        } else if (world.getBlockState(pos.down()).get(POS) >= 1 && world.getBlockState(pos.down()).get(POS) != maxHeight && world.getBlockState(pos.up()).getBlock() == state.getBlock() && state.get(POS) != maxHeight) { // If block down POS is >= 1 AND block down isn't max height AND block above isn't max height AND pos of this block isn't max height -> set block to BETWEEN
            world.setBlockState(pos, state.with(POS, world.getBlockState(pos.down()).get(POS) + 1).with(PLACE, BlockPlace.BETWEEN), 3);
        } else {

            if (world.getBlockState(pos.down()).get(POS) == maxHeight) { // If block down is max height -> set block to LOWER != set block to UPPER
                world.setBlockState(pos, state.with(POS, 1).with(PLACE, BlockPlace.LOWER), 3);
            } else {
                world.setBlockState(pos, state.with(POS, world.getBlockState(pos.down()).get(POS) + 1).with(PLACE, BlockPlace.UPPER), 3);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POS, PLACE);
    }
}
