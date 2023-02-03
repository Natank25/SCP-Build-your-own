package io.github.natank25.scpfondation.block.custom;

import io.github.natank25.scpfondation.block.ModBlocks;
import io.github.natank25.scpfondation.sounds.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.block.enums.DoubleBlockHalf.LOWER;
import static net.minecraft.block.enums.DoubleBlockHalf.UPPER;

public class SlidingDoorBlock extends Block {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape EAST_LOWER = Block.createCuboidShape(6, 0, 0, 10, 32, 16);
    protected static final VoxelShape NORTH_LOWER = Block.createCuboidShape(0, 0, 6, 16, 32, 10);

    protected static final VoxelShape EAST_POWERED_LOWER = Block.createCuboidShape(6, 0, 15, 10, 32, 31);
    protected static final VoxelShape NORTH_POWERED_LOWER = Block.createCuboidShape(15, 0, 6, 31, 32, 10);

    protected static final VoxelShape EAST_UPPER = Block.createCuboidShape(6, -16, 0, 10, 16, 16);
    protected static final VoxelShape NORTH_UPPER = Block.createCuboidShape(0, -16, 6, 16, 16, 10);

    protected static final VoxelShape EAST_POWERED_UPPER = Block.createCuboidShape(6, -16, 15, 10, 16, 31);
    protected static final VoxelShape NORTH_POWERED_UPPER = Block.createCuboidShape(15, -16, 6, 31, 16, 10);
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
                    case WEST, EAST -> EAST_POWERED_LOWER;
                    default -> NORTH_POWERED_LOWER;
                };
            } else {
                return switch (state.get(FACING)) {
                    case WEST, EAST -> EAST_LOWER;
                    default -> NORTH_LOWER;
                };
            }
        } else {
            if (state.get(OPEN)) {
                return switch (state.get(FACING)) {
                    case WEST, EAST -> EAST_POWERED_UPPER;
                    default -> NORTH_POWERED_UPPER;
                };
            } else {
                return switch (state.get(FACING)) {
                    case WEST, EAST -> EAST_UPPER;
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
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if(state.get(HALF) == LOWER){
            world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 3);
            world.updateNeighbors(pos,Blocks.AIR);
        }else{
            world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(),3);
            world.updateNeighbors(pos.down(), Blocks.AIR);
        }
        super.onBroken(world, pos, state);
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
            world.breakBlock(pos.up(), true);
            dropStacks(state, world, pos);
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
