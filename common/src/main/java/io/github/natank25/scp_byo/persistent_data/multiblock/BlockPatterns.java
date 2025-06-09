package io.github.natank25.scp_byo.persistent_data.multiblock;

import dev.architectury.platform.Platform;
import io.github.natank25.scp_byo.persistent_data.multiblock.multiblocks.FullIronBeacon;
import io.github.natank25.scp_byo.persistent_data.multiblock.multiblocks.SCP096Cage;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum BlockPatterns {
	;
	
	private static final List<ScpBYOBlockPattern> BLOCK_PATTERNS = new ArrayList<>();
	
	public static final ScpBYOBlockPattern SCP_096_CHAMBER = register(Utils.newIdentifier("scp_096_multiblock"), SCP096Cage.getBlockPattern(), SCP096Cage.class);
	public static ScpBYOBlockPattern TEST;
	
	
	public static <M extends Multiblock> ScpBYOBlockPattern register(Identifier id, BlockPattern blockPattern, Class<M> multiblockClass) {
		ScpBYOBlockPattern newBlockPattern = new ScpBYOBlockPattern(id, blockPattern, multiblockClass);
		BLOCK_PATTERNS.add(newBlockPattern);
		return newBlockPattern;
	}
	
	public static Optional<Class<? extends Multiblock>> getMultiblockClassForId(Identifier id) {
		return BLOCK_PATTERNS.stream().filter(scpBYOBlockPattern -> scpBYOBlockPattern.id().equals(id)).findFirst().map(ScpBYOBlockPattern::multiblockClass);
	}

	public static Optional<Identifier> getIdForMultiblockClass(Class<? extends Multiblock> multiblockClass) {
		for (ScpBYOBlockPattern pattern : BLOCK_PATTERNS) {
			if (pattern.multiblockClass().equals(multiblockClass)) {
				return Optional.of(pattern.id());
			}
		}
		return Optional.empty();
	}
	
	public static List<ScpBYOBlockPattern> getAll() {
		return BLOCK_PATTERNS;
	}

	public static void registerBlockPatterns(){
		if (Platform.isDevelopmentEnvironment()){
			TEST = register(Utils.newIdentifier("iron_beacon_multiblock"), BlockPatternBuilder.start().aisle("iii", "aaa").aisle("iii", "aia").aisle("iii", "aaa").where('i', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('a', CachedBlockPosition.matchesBlockState(AbstractBlock.AbstractBlockState::isAir)).build(), FullIronBeacon.class); //TODO try to replace FullIronBeacon.class to Multiblock.class
		}
	}
	
}
