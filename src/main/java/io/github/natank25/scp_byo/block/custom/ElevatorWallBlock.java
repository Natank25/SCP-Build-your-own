package io.github.natank25.scp_byo.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;

public class ElevatorWallBlock extends Block {

    public static final BooleanProperty STICKY = BooleanProperty.of("sticky");

    public ElevatorWallBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(STICKY, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STICKY);
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return state.get(STICKY) ? PistonBehavior.NORMAL : PistonBehavior.PUSH_ONLY;
    }
}
