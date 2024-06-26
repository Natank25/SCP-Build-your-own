package io.github.natank25.scp_byo.block.custom;

import io.github.natank25.scp_byo.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class KeycardReaderBlock extends ButtonBlock {
	
	private static final IntProperty KEYCARD_LEVEL = IntProperty.of("keycard_level", 1, 6);
	
	//region Shapes
	private static final VoxelShape SHAPENORTH = makeShapeNorth();
	private static final VoxelShape SHAPESOUTH = makeShapeSouth();
	private static final VoxelShape SHAPEEAST = makeShapeEast();
	private static final VoxelShape SHAPEWEST = makeShapeWest();
	//endregion
	
	public KeycardReaderBlock(Settings settings, int pressTicks, boolean wooden, SoundEvent clickOffSound, SoundEvent clickOnSound) {
		super(settings, pressTicks, wooden, clickOffSound, clickOnSound);
		
		this.setDefaultState(this.getStateManager().getDefaultState().with(KEYCARD_LEVEL, 1));
	}
	
	//region Shapes Creation
	private static VoxelShape makeShapeSouth() {
		return Stream.of(Block.createCuboidShape(6, 3, 0, 10, 7.75, 1), Block.createCuboidShape(6, 10.4375, 0, 10, 11, 1), Block.createCuboidShape(6, 7.75, 0, 6.5625, 10.4375, 1), Block.createCuboidShape(9.4375, 7.75, 0, 10, 10.4375, 1), Block.createCuboidShape(6.5625, 7.75, 0, 9.4375, 10.5625, 0.875)).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}
	
	private static VoxelShape makeShapeNorth() {
		return Stream.of(Block.createCuboidShape(6, 3, 15, 10, 7.75, 16), Block.createCuboidShape(6, 10.4375, 15, 10, 11, 16), Block.createCuboidShape(9.4375, 7.75, 15, 10, 10.4375, 16), Block.createCuboidShape(6, 7.75, 15, 6.5625, 10.4375, 16), Block.createCuboidShape(6.5625, 7.75, 15.125, 9.4375, 10.5625, 16)).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}
	
	private static VoxelShape makeShapeEast() {
		return Stream.of(Block.createCuboidShape(0, 3, 6, 1, 7.75, 10), Block.createCuboidShape(0, 10.4375, 6, 1, 11, 10), Block.createCuboidShape(0, 7.75, 9.4375, 1, 10.4375, 10), Block.createCuboidShape(0, 7.75, 6, 1, 10.4375, 6.5625), Block.createCuboidShape(0, 7.75, 6.5625, 0.875, 10.5625, 9.4375)).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}
	
	private static VoxelShape makeShapeWest() {
		return Stream.of(Block.createCuboidShape(15, 3, 6, 16, 7.75, 10), Block.createCuboidShape(15, 10.4375, 6, 16, 11, 10), Block.createCuboidShape(15, 7.75, 6, 16, 10.4375, 6.5625), Block.createCuboidShape(15, 7.75, 9.4375, 16, 10.4375, 10), Block.createCuboidShape(15.125, 7.75, 6.5625, 16, 10.5625, 9.4375)).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}
	//endregion
	

	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return canPlaceAt(world, pos, getDirection(state).getOpposite());
	}
	
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(FACING)) {
			case SOUTH -> SHAPESOUTH;
			case WEST -> SHAPEWEST;
			case EAST -> SHAPEEAST;
			
			default -> SHAPENORTH;
		};
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACE, WallMountLocation.WALL).with(FACING, ctx.getSide()).with(POWERED, false);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (state.get(POWERED)) return ActionResult.CONSUME;
		
		if (player.isHolding(ModItems.KEYCARD_1) || player.isHolding(ModItems.KEYCARD_2) || player.isHolding(ModItems.KEYCARD_3) || player.isHolding(ModItems.KEYCARD_4) || player.isHolding(ModItems.KEYCARD_5) || player.isHolding(ModItems.KEYCARD_6)) {
			if (player.isHolding(ModItems.KEYCARD_1) && state.get(KEYCARD_LEVEL) == 1) {
				return this.activate(state, world, pos, player);
			} else if (player.isHolding(ModItems.KEYCARD_2) && state.get(KEYCARD_LEVEL) <= 2) {
				return this.activate(state, world, pos, player);
			} else if (player.isHolding(ModItems.KEYCARD_3) && state.get(KEYCARD_LEVEL) <= 3) {
				return this.activate(state, world, pos, player);
			} else if (player.isHolding(ModItems.KEYCARD_4) && state.get(KEYCARD_LEVEL) <= 4) {
				return this.activate(state, world, pos, player);
			} else if (player.isHolding(ModItems.KEYCARD_5) && state.get(KEYCARD_LEVEL) <= 5) {
				return this.activate(state, world, pos, player);
			} else if (player.isHolding(ModItems.KEYCARD_6) && state.get(KEYCARD_LEVEL) <= 6) {
				return this.activate(state, world, pos, player);
			}
			player.sendMessage(Text.literal("You have inserted the keycard but nothing happened."), true);
		} else if (player.isHolding(ModItems.WRENCH)) {
			player.sendMessage(state.get(KEYCARD_LEVEL) == 6 ? Text.literal("Changed keycard level required to 1.") : Text.literal("Changed keycard level required to " + (state.get(KEYCARD_LEVEL) + 1) + "."), true);
			world.setBlockState(pos, state.cycle(KEYCARD_LEVEL), 3);
			
			return ActionResult.success(world.isClient());
		}
		return ActionResult.PASS;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(KEYCARD_LEVEL, FACING, POWERED, FACE);
	}
	
	private ActionResult activate(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		
		this.powerOn(state, world, pos); //TODO: fix this from not working as expected
		this.playClickSound(null, world, pos, true);
		world.emitGameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
		return ActionResult.success(world.isClient());
		
	}
	
	
}
