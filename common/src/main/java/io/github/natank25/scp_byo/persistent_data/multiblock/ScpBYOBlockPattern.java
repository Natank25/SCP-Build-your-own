package io.github.natank25.scp_byo.persistent_data.multiblock;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.Identifier;

public record ScpBYOBlockPattern(Identifier id, BlockPattern blockPattern,
								 Class<? extends Multiblock> multiblockClass) {
}
