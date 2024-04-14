package io.github.natank25.scp_byo.mutliblock.ModMultiblocks;

import io.github.natank25.scp_byo.mutliblock.Multiblock;
import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class FullIronBeacon extends Multiblock {
	public FullIronBeacon(BlockPattern pattern, BlockPattern.@NotNull Result result, World world) {
		super(pattern, result, world);
		scp_byo.LOGGER.info("Created a FullIronBeacon");
	}
	
	@Override
	public void tick() {
		this.getWorld().getRandom();
	}
}
