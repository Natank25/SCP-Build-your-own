package io.github.natank25.scp_byo.block.custom;

import io.github.natank25.scp_byo.block.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ExtendableBlock extends Block {
    public static final EnumProperty<BlockPlace> PLACE = EnumProperty.of("place", BlockPlace.class);
    public static final BooleanProperty FORCE_STATE = BooleanProperty.of("force_state");
    private static final IntProperty POS = IntProperty.of("pos", 1, 25);
    private final int maxHeight;

    public ExtendableBlock(AbstractBlock.Settings settings, int maxHigh) {
        super(settings);
        this.maxHeight = maxHigh;
        this.setDefaultState(this.getDefaultState().with(POS, 1).with(PLACE, BlockPlace.LOWER).with(FORCE_STATE, false));
    }


    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (state.get(FORCE_STATE)) return;
        this.isSameAsBlockBelow(state, world, pos);
    }

    private void isSameAsBlockBelow(BlockState state, World world, BlockPos pos) {
		if (world.getBlockState(pos.down()).getBlock() == state.getBlock())
			world.setBlockState(pos, this.isInMiddle(state, world, pos) ? state.with(POS, world.getBlockState(pos.down()).get(POS) + 1).with(PLACE, BlockPlace.BETWEEN) : world.getBlockState(pos.down()).get(POS) == this.maxHeight ? state.with(POS, 1).with(PLACE, BlockPlace.LOWER) : state.with(POS, world.getBlockState(pos.down()).get(POS) + 1).with(PLACE, BlockPlace.UPPER), 3);
		else
			world.setBlockState(pos, world.getBlockState(pos.down()).getBlock() == ModBlocks.SLIDING_DOOR ? state.with(POS, 1).with(PLACE, BlockPlace.UPPER) : state.with(POS, 1).with(PLACE, BlockPlace.LOWER), 3);
    }

    private boolean isInMiddle(BlockState state, BlockView world, BlockPos pos) {
        return world.getBlockState(pos.down()).get(POS) >= 1 && world.getBlockState(pos.down()).get(POS) != this.maxHeight && world.getBlockState(pos.up()).getBlock() == state.getBlock() && state.get(POS) != this.maxHeight;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        this.isSameAsBlockBelow(state, world, pos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POS, PLACE, FORCE_STATE);
    }
}
