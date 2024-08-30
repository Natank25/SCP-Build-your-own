package io.github.natank25.scp_byo.persistent_data.multiblock;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.Identifier;

public class ScpBYOBlockPattern {
	private final Identifier id;
	private final BlockPattern blockPattern;
	private final Class<? extends Multiblock> multiblockClass;
	
	
	public ScpBYOBlockPattern(Identifier id, BlockPattern blockPattern, Class<? extends Multiblock> multiblockClass) {
		this.id = id;
		this.blockPattern = blockPattern;
		this.multiblockClass = multiblockClass;
		
	}
	
	public BlockPattern getBlockPattern() {
		return this.blockPattern;
	}
	
	public Identifier getId() {
		return this.id;
	}
	
	public Class<? extends Multiblock> getMultiblockClass() {
		return this.multiblockClass;
	}
	
	
}
