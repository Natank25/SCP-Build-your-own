package io.github.natank25.scp_byo.block.custom;

import com.mojang.serialization.MapCodec;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.block.entity.SlidingDoorBlockEntity;
import io.github.natank25.scp_byo.sounds.ModSounds;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class SlidingDoor extends BlockWithEntity {

    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final BooleanProperty OPEN;
    public static final MapCodec<SlidingDoor> CODEC;
    private static final EnumProperty<Direction> FACING;
    private static final BooleanProperty POWERED;
    //region Shapes
    private static final VoxelShape EAST_SHAPE;
    private static final VoxelShape EAST_OPEN_SHAPE;
    private static final VoxelShape NORTH_SHAPE;
    private static final VoxelShape NORTH_OPEN_SHAPE;
    private static final VoxelShape WEST_OPEN_SHAPE;
    private static final VoxelShape SOUTH_OPEN_SHAPE;
    //endregion

    static {
        EAST_SHAPE = Block.createCuboidShape(6, 0, 0, 10, 16, 16);
        EAST_OPEN_SHAPE = Block.createCuboidShape(6, 0, 15, 10, 16, 31);
        NORTH_SHAPE = Block.createCuboidShape(0, 0, 6, 16, 16, 10);
        NORTH_OPEN_SHAPE = Block.createCuboidShape(15, 0, 6, 31, 16, 10);
        WEST_OPEN_SHAPE = Block.createCuboidShape(6, 0, -15, 10, 16, 1);
        SOUTH_OPEN_SHAPE = Block.createCuboidShape(-15, 0, 6, 1, 16, 10);


        FACING = HorizontalFacingBlock.FACING;
        OPEN = Properties.OPEN;
        POWERED = Properties.POWERED;
        HALF = Properties.DOUBLE_BLOCK_HALF;
        CODEC = createCodec((settings) -> new SlidingDoor(settings, ModSounds.SLIDING_DOOR_OPEN.get(), ModSounds.SLIDING_DOOR_OPEN.get()));
    }

    private final SoundEvent closeSound;
    private final SoundEvent openSound;

    public SlidingDoor(Settings settings, SoundEvent closeSound, SoundEvent openSound) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false).with(POWERED, false).with(HALF, DoubleBlockHalf.LOWER));
        this.closeSound = closeSound;
        this.openSound = openSound;

    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return state.get(OPEN);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        return state.get(HALF) == DoubleBlockHalf.LOWER ? blockState.isSideSolidFullSquare(world, blockPos, Direction.UP) : blockState.isOf(this);
    }

    //region Block Entity
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SlidingDoorBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        boolean bl = state.get(OPEN);
        if (bl) {
            switch (state.get(FACING)) {
                case EAST -> {
                    return EAST_OPEN_SHAPE;
                }
                case SOUTH -> {
                    return SOUTH_OPEN_SHAPE;
                }
                case WEST -> {
                    return WEST_OPEN_SHAPE;
                }
                default -> {
                    return NORTH_OPEN_SHAPE;
                }
            }
        } else {
            switch (state.get(FACING)) {
                case WEST, EAST -> {
                    return EAST_SHAPE;
                }
                default -> {
                    return NORTH_SHAPE;
                }
            }
        }
    }

    //endregion

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        if (world.getBlockState(ctx.getBlockPos().down()) == Blocks.AIR.getDefaultState()) return null;
        if (pos.getY() >= world.getTopYInclusive() - 1) return null;
        if (!world.getBlockState(pos.up()).canReplace(ctx)) return null;

        Direction facing = this.getFacing(ctx);
        boolean powered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        boolean open = powered;

        if (world.getBlockState(pos.offset(facing.rotateYCounterclockwise())).isOf(this) && world.getBlockState(pos.offset(facing.rotateYCounterclockwise())).get(FACING) == facing.getOpposite() && world.getBlockState(pos.offset(facing.rotateYCounterclockwise())).get(POWERED))
            open = true;
        return this.getDefaultState().with(FACING, facing).with(POWERED, powered).with(OPEN, open).with(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public long getRenderingSeed(BlockState state, BlockPos pos) {
        return MathHelper.hashCode(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);

        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            if (!neighborState.isOf(this) || neighborState.get(HALF) == doubleBlockHalf)
                return Blocks.AIR.getDefaultState();
            this.setOpen(world, pos, neighborState.get(OPEN));
            return state.with(FACING, neighborState.get(FACING)).with(OPEN, neighborState.get(OPEN)).with(POWERED, neighborState.get(POWERED));
        }
        if (direction.getAxis().isHorizontal() && neighborState.isOf(this) && neighborState.get(HALF) == doubleBlockHalf && neighborState.get(FACING).getOpposite() == state.get(FACING) && !state.get(POWERED)) {
            this.setOpen(world, pos, neighborState.get(OPEN));
            return state.with(OPEN, neighborState.get(OPEN));
        }
        return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.offset(state.get(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
        boolean open = powered;

        if (world.getBlockState(pos.offset(state.get(FACING).rotateYCounterclockwise())).isOf(this) && world.getBlockState(pos.offset(state.get(FACING).rotateYCounterclockwise())).get(FACING) == state.get(FACING).getOpposite() && world.getBlockState(pos.offset(state.get(FACING).rotateYCounterclockwise())).get(POWERED))
            open = true;

        if (!this.getDefaultState().isOf(sourceBlock) && powered != state.get(POWERED)) {
            if (open != state.get(OPEN)) {
                this.playOpenCloseSound(world, pos, open);
                world.emitGameEvent(null, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                this.setOpen(world, pos, open);
            }
            world.setBlockState(pos, (state.with(POWERED, powered)).with(OPEN, open), 2);
        }
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return mirror == BlockMirror.NONE ? state : state.rotate(mirror.getRotation(state.get(FACING)));
    }

    //region Door Methods
    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            DoubleBlockHalf doubleBlockHalf = state.get(HALF);
            if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
                BlockPos blockPos = pos.down();
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
                    BlockState blockState2 = blockState.getFluidState().isOf(Fluids.WATER) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
                    world.setBlockState(blockPos, blockState2, Block.SKIP_DROPS | Block.NOTIFY_ALL);
                    world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
                }
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return ActionResult.PASS;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING, OPEN, POWERED);
    }

    private Direction getFacing(ItemPlacementContext ctx) {
        BlockView world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction direction = ctx.getHorizontalPlayerFacing();
        BlockPos upPos = blockPos.up();
        Direction directionCCW = direction.rotateYCounterclockwise();
        BlockPos blockPosCCW = blockPos.offset(directionCCW);
        BlockState blockStateCCW = world.getBlockState(blockPosCCW);
        BlockPos upBlockPosCCW = upPos.offset(directionCCW);
        BlockState upBlockStateCCW = world.getBlockState(upBlockPosCCW);
        Direction directionCW = direction.rotateYClockwise();
        BlockPos blockPosCW = blockPos.offset(directionCW);
        BlockState blockStateCW = world.getBlockState(blockPosCW);
        BlockPos upBlockPosCW = upPos.offset(directionCW);
        BlockState upBlockStateCW = world.getBlockState(upBlockPosCW);
        int sidePriority = (blockStateCCW.isFullCube(world, blockPosCCW) ? -1 : 0) + (upBlockStateCCW.isFullCube(world, upBlockPosCCW) ? -1 : 0) + (blockStateCW.isFullCube(world, blockPosCW) ? 1 : 0) + (upBlockStateCW.isFullCube(world, upBlockPosCW) ? 1 : 0);
        boolean isCCWLower = blockStateCCW.isOf(this) && blockStateCCW.get(HALF) == DoubleBlockHalf.LOWER;
        boolean isCWLower = blockStateCW.isOf(this) && blockStateCW.get(HALF) == DoubleBlockHalf.LOWER;
        if ((isCCWLower && !isCWLower) || sidePriority > 0) {
            return direction;
        }

        if ((isCWLower && !isCCWLower) || sidePriority != 0) {
            return direction.getOpposite();
        }

        int xDirection = direction.getOffsetX();
        int zDirection = direction.getOffsetZ();
        Vec3d hitPos = ctx.getHitPos();
        double xDistFromCenter = hitPos.x - (double) blockPos.getX();
        double zDistFromCenter = hitPos.z - (double) blockPos.getZ();
        return (xDirection >= 0 || !(zDistFromCenter < 0.5))
                && (xDirection <= 0 || !(zDistFromCenter > 0.5))
                && (zDirection >= 0 || !(xDistFromCenter > 0.5))
                && (zDirection <= 0 || !(xDistFromCenter < 0.5)) ? direction.getOpposite() : direction;


    }

    private void playOpenCloseSound(WorldAccess world, BlockPos pos, boolean open) {
        world.playSound(null, pos, open ? this.openSound : this.closeSound, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    private void setOpen(WorldView world, BlockPos pos, boolean open) {
        world.getBlockEntity(pos, ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY.get()).orElseThrow().setOpen(open);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
    //endregion

}
