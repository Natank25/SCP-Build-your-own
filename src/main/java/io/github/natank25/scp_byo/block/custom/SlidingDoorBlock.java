package io.github.natank25.scp_byo.block.custom;

import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.sounds.ModSounds;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.block.enums.DoubleBlockHalf.LOWER;
import static net.minecraft.block.enums.DoubleBlockHalf.UPPER;

public class SlidingDoorBlock extends Block {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    // EAST-WEST X->OFFSET  Y->HEIGHT Z->OPEN/CLOSE STATE

    // NORTH-SOUTH X->OPEN/CLOSE STATE  Y->HEIGHT Z->OFFSET
    protected static final VoxelShape EAST_LOWER = Block.createCuboidShape(6, 0, 0, 10, 32, 16);
    protected static final VoxelShape NORTH_LOWER = Block.createCuboidShape(0, 0, 6, 16, 32, 10);
    protected static final VoxelShape SOUTH_LOWER = Block.createCuboidShape(0, 0, 6, 16, 32, 10);
    protected static final VoxelShape WEST_LOWER = Block.createCuboidShape(6, 0, 0, 10, 32, 16);

    protected static final VoxelShape EAST_POWERED_LOWER = Block.createCuboidShape(6, 0, 15, 10, 32, 31);
    protected static final VoxelShape NORTH_POWERED_LOWER = Block.createCuboidShape(15, 0, 6, 31, 32, 10);
    protected static final VoxelShape SOUTH_POWERED_LOWER = Block.createCuboidShape(-15, 0, 6, 1, 32, 10);
    protected static final VoxelShape WEST_POWERED_LOWER = Block.createCuboidShape(6, 0, -15, 10, 32, 1);

    protected static final VoxelShape EAST_UPPER = Block.createCuboidShape(6, -16, 0, 10, 16, 16);
    protected static final VoxelShape NORTH_UPPER = Block.createCuboidShape(0, -16, 6, 16, 16, 10);
    protected static final VoxelShape SOUTH_UPPER = Block.createCuboidShape(0, -16, 6, 16, 16, 10);
    protected static final VoxelShape WEST_UPPER = Block.createCuboidShape(6, -16, 0, 10, 16, 16);

    protected static final VoxelShape EAST_POWERED_UPPER = Block.createCuboidShape(6, -16, 15, 10, 16, 31);
    protected static final VoxelShape NORTH_POWERED_UPPER = Block.createCuboidShape(15, -16, 6, 31, 16, 10);
    protected static final VoxelShape SOUTH_POWERED_UPPER = Block.createCuboidShape(-15, -16, 6, 1, 16, 10);
    protected static final VoxelShape WEST_POWERED_UPPER = Block.createCuboidShape(6, -16, -15, 10, 16, 1);
    public SlidingDoorBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getDefaultState().with(HALF, LOWER).with(POWERED, false).with(OPEN, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED,OPEN, HALF);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HALF) == LOWER) {
            if (state.get(OPEN)) {
                return switch (state.get(FACING)) {
                    case EAST -> EAST_POWERED_LOWER;
                    case WEST -> WEST_POWERED_LOWER;
                    case SOUTH -> SOUTH_POWERED_LOWER;
                    default -> NORTH_POWERED_LOWER;
                };
            } else {
                return switch (state.get(FACING)) {
                    case WEST -> WEST_LOWER;
                    case EAST -> EAST_LOWER;
                    case SOUTH -> SOUTH_LOWER;
                    default -> NORTH_LOWER;
                };
            }
        } else {
            if (state.get(OPEN)) {
                return switch (state.get(FACING)) {
                    case WEST -> WEST_POWERED_UPPER;
                    case EAST -> EAST_POWERED_UPPER;
                    case SOUTH -> SOUTH_POWERED_UPPER;
                    default -> NORTH_POWERED_UPPER;
                };
            } else {
                return switch (state.get(FACING)) {
                    case WEST -> WEST_UPPER;
                    case EAST -> EAST_UPPER;
                    case SOUTH -> SOUTH_UPPER;
                    default -> NORTH_UPPER;
                };
            }
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(state.get(HALF) == UPPER) return;
        world.setBlockState(pos.up(), state.with(HALF, UPPER),3);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(state.get(HALF) == LOWER){
            world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 3);
            super.onBreak(world, pos, state, player);
            super.onBreak(world, pos.up(), state, player);

        }else{
            world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(),3);
            super.onBreak(world, pos, state, player);
            super.onBreak(world, pos.down(), state, player);
        }
    }



    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState;

        if(ctx.getWorld().getBlockState(ctx.getBlockPos().down()) == Blocks.AIR.getDefaultState()){
            return null;
        }

        if (ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos())){
            blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(POWERED, true).with(OPEN, true).with(HALF, LOWER);
        }else{
            blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(POWERED, false).with(OPEN, false).with(HALF, LOWER);
        }
        if (blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())){
            return blockState;
        }
        return null;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.getBlockState(sourcePos).getBlock() == ModBlocks.SLIDING_DOOR || ModBlocks.SLIDING_DOOR == sourceBlock) {
            return;
        }
        if (world.getBlockState(pos.down()) == Blocks.AIR.getDefaultState() && world.getBlockState(pos).get(HALF) == LOWER) {
            world.breakBlock(pos, true);
            world.breakBlock(pos.up(), false);
            return;
        }else if (world.getBlockState(pos.down()) == Blocks.AIR.getDefaultState() && world.getBlockState(pos).get(HALF) == UPPER){
            world.breakBlock(pos, false);
            world.breakBlock(pos.down(), true);
            return;
        }

        if (world.getBlockState(pos).get(HALF) == LOWER) {
            if (world.isReceivingRedstonePower(pos)) {
                world.setBlockState(pos, state.with(POWERED, true).with(OPEN, true), 2);
                world.setBlockState(pos.up(), state.with(POWERED, true).with(OPEN, true).with(HALF, UPPER), 2);
                world.playSound(null, pos, ModSounds.SLIDING_DOOR_OPEN, SoundCategory.BLOCKS, 1f, 1f);
            } else {
                world.setBlockState(pos, state.with(POWERED, false).with(OPEN, false), 2);
                world.setBlockState(pos.up(), state.with(POWERED, false).with(OPEN, false).with(HALF, UPPER), 2);
            }
        } else {
            if (world.isReceivingRedstonePower(pos)) {
                world.setBlockState(pos, state.with(POWERED, true).with(OPEN, true), 2);
                world.setBlockState(pos.down(), state.with(POWERED, true).with(OPEN, true).with(HALF, LOWER), 2);
                world.playSound(null, pos, ModSounds.SLIDING_DOOR_OPEN, SoundCategory.BLOCKS, 1f, 1f);
            } else {
                world.setBlockState(pos, state.with(POWERED, false).with(OPEN, false), 2);
                world.setBlockState(pos.down(), state.with(POWERED, false).with(OPEN, false).with(HALF, LOWER), 2);
            }
        }
    }

}
