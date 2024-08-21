package io.github.natank25.scp_byo.persistent_data.multiblock.ModMultiblocks;

import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class FullIronBeacon extends Multiblock {
	public FullIronBeacon(BlockPattern pattern, BlockPattern.@NotNull Result result, World world) {
		super(pattern, result, world);
	}
	
}
