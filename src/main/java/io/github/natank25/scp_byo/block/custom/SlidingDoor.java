package io.github.natank25.scp_byo.block.custom;

import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.block.entity.SlidingDoorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class SlidingDoor extends BlockWithEntity {
	
	public static final EnumProperty<DoubleBlockHalf> HALF;
	public static final BooleanProperty OPEN;
	private static final DirectionProperty FACING;
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
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return switch (type) {
			case WATER -> false;
			case LAND, AIR -> state.get(OPEN);
		};
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
		boolean bl = state.get(POWERED);
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
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}
	
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState;
		
		if (ctx.getWorld().getBlockState(ctx.getBlockPos().down()) == Blocks.AIR.getDefaultState()) return null;
		
		if (ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()))
			blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(POWERED, true).with(OPEN, true).with(HALF, DoubleBlockHalf.LOWER);
		else
			blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(POWERED, false).with(OPEN, false).with(HALF, DoubleBlockHalf.LOWER);
		
		return blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? blockState : null;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	
	@Override
	public long getRenderingSeed(BlockState state, BlockPos pos) {
		return MathHelper.hashCode(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
	}
	
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return state;/*
		DoubleBlockHalf half = state.get(HALF);
		if (direction.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER && (direction == Direction.UP)) {
			// Si on est le bloc du bas et que l'update viens de dessus
			if (neighborState.isOf(this) && neighborState.get(HALF) == DoubleBlockHalf.UPPER)
				// Si l'autre bloc est une porte et que c'est le haut
				return state.with(FACING, neighborState.get(FACING)).with(OPEN, neighborState.get(OPEN)).with(POWERED, neighborState.get(POWERED)); // TODO: remove with(FACING [...]= bc its useless (to test)
				// Alors Renvoie le state de nous mais avec l'ouverture de l'autre
			return Blocks.AIR.getDefaultState();
				// Sinon revoie de l'air car cela veut dire que l'autre a été cassé
		} else {
			// Sinon (si on est le block du haut OU que l'update viens d'autre part)
			if (half == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos))
				// Si on est le block du bas, que l'update viens du bas et qu'on ne peut pas etre placé là ou on est
				return Blocks.AIR.getDefaultState();
				// Alors Renvoie de l'air car cela veut dire que le bloc en dessous de nous à été cassé
			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
				// Sinon renvoie le state de nous
		}*/
	}
	
	
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return mirror == BlockMirror.NONE ? state : state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		if (world.getBlockState(sourcePos).getBlock() == ModBlocks.SLIDING_DOOR || ModBlocks.SLIDING_DOOR == sourceBlock) {
			return;
		}
		if (world.getBlockState(pos.down()) == Blocks.AIR.getDefaultState() && world.getBlockState(pos).get(HALF) == DoubleBlockHalf.LOWER) {
			world.breakBlock(pos, true);
			world.breakBlock(pos.up(), false);
			return;
		} else if (world.getBlockState(pos.down()) == Blocks.AIR.getDefaultState() && world.getBlockState(pos).get(HALF) == DoubleBlockHalf.UPPER) {
			world.breakBlock(pos, false);
			world.breakBlock(pos.down(), true);
			return;
		}
		
		boolean open = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.offset(state.get(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		
		if (!this.getDefaultState().isOf(sourceBlock) && open != state.get(POWERED)) {
			this.setOpen(state, world, pos, open);
		}
	}
	
	//region Door Methods
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBreak(world, pos, state, player);
		BlockPos otherPos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
		world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), 3);
		super.onBreak(world, otherPos, state, player);
		
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (state.get(HALF) == DoubleBlockHalf.UPPER) return;
		world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
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
	
	private void playOpenCloseSound(WorldAccess world, BlockPos pos, boolean open) {
		world.playSound(null, pos, open ? this.openSound : this.closeSound, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
	}
	
	private void setOpen(BlockState state, World world, BlockPos pos, boolean open) {
		BlockState newState = state.with(POWERED, open).with(OPEN, open);
		world.setBlockState(pos, newState, 2);
		
		boolean lower = world.getBlockState(pos).get(HALF) == DoubleBlockHalf.LOWER;
		BlockPos otherPos = lower ? pos.up() : pos.down();
		
		world.setBlockState(otherPos, newState.with(HALF, lower ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER), 2);
		
		this.playOpenCloseSound(world, pos, open);
		
		world.getBlockEntity(pos, ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY).orElseGet(null).setOpen(open);
		world.getBlockEntity(otherPos, ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY).orElseGet(null).setOpen(open);
	}
	//endregion
	
}
