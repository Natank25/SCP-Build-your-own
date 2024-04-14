package io.github.natank25.scp_byo.item.custom;

import io.github.natank25.scp_byo.block.custom.ElevatorWallBlock;
import io.github.natank25.scp_byo.block.custom.ExtendableBlock;
import io.github.natank25.scp_byo.mutliblock.ModMultiblocks.SCP096Cage;
import io.github.natank25.scp_byo.mutliblock.Multiblock;
import io.github.natank25.scp_byo.persistent_data.cca.register.ModWorldComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class Wrench extends Item {
    public Wrench(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        //region Easier variable creation
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        Block block = state.getBlock();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        //endregion

        if (block instanceof ElevatorWallBlock) {
            if (state.get(ElevatorWallBlock.STICKY)) {
                player.sendMessage(Text.literal("Block at " + pos.toShortString() + " is now non-sticky."), true);
            } else {
                player.sendMessage(Text.literal("Block at " + pos.toShortString() + " is now sticky."), true);
            }
            world.setBlockState(pos, state.with(ElevatorWallBlock.STICKY, !state.get(ElevatorWallBlock.STICKY)), 2);
            return ActionResult.success(world.isClient());
        } else if (block instanceof ExtendableBlock) {
            world.setBlockState(pos, state.cycle(ExtendableBlock.PLACE).with(ExtendableBlock.FORCE_STATE, true), 3);
            return ActionResult.SUCCESS;
        }

        if(ModWorldComponents.MULTIBLOCKS.get(world).tryAssemble(pos).isPresent()) return ActionResult.SUCCESS;
        
        Optional<? extends Multiblock> potentialMultiblock = ModWorldComponents.MULTIBLOCKS.get(world).getMultiblock(pos);
        if(potentialMultiblock.isPresent()){
            
            if(potentialMultiblock.get() instanceof SCP096Cage multiblock){
                if(multiblock.repair(player)) return ActionResult.SUCCESS;
			}
        
        }

        return ActionResult.CONSUME_PARTIAL;
    }


    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return this.getDefaultStack();
    }
}
