package io.github.natank25.scp_byo.block.custom;

import io.github.natank25.scp_byo.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ElevatorFloorBlock extends Block {

    public static final BooleanProperty STICKY = BooleanProperty.of("sticky");

    public ElevatorFloorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(STICKY, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STICKY);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.isHolding(ModItems.WRENCH)) {
            if (state.get(STICKY)) {
                player.sendMessage(Text.literal("Block at " + pos.toShortString() + " is now non-sticky."), true);
            } else {
                player.sendMessage(Text.literal("Block at " + pos.toShortString() + " is now sticky."), true);
            }
            world.setBlockState(pos, state.with(STICKY, !state.get(STICKY)), 2);
            return ActionResult.success(world.isClient());
        }
        return ActionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return state.get(STICKY) ? PistonBehavior.NORMAL : PistonBehavior.PUSH_ONLY;
    }
}
