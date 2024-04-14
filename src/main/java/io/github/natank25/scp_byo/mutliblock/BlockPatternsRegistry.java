package io.github.natank25.scp_byo.mutliblock;

import io.github.natank25.scp_byo.mutliblock.ModMultiblocks.FullIronBeacon;
import io.github.natank25.scp_byo.mutliblock.ModMultiblocks.SCP096Cage;
import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.predicate.block.BlockStatePredicate;

import java.util.*;

public enum BlockPatternsRegistry {
    ;
    private static final List<BlockPattern> BLOCK_PATTERNS = new ArrayList<>();
    private static final Map<Class<? extends Multiblock>, BlockPattern> classBlockPatternMap = new HashMap<>();
    
    public static final BlockPattern SCP_096_CHAMBER = register(BlockPatternBuilder.start().aisle("iiiii","iiiii","iiiii","iiiii","iiiii").aisle("iiiii", "iaaai", "iaaai","iaaai","iiiii").aisle("iiiii", "iaaai", "iaaai","iaaai","iiiii").aisle("iiiii", "iaaai", "iaaai","iaaai","iiiii").aisle("iiiii","iiiii","iiiii","iiiii","iiiii").where('i', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('a', CachedBlockPosition.matchesBlockState(AbstractBlock.AbstractBlockState::isAir)).build(), SCP096Cage.class);
    public static final BlockPattern TEST = register(BlockPatternBuilder.start().aisle("iii","aaa").aisle("iii", "aia").aisle("iii", "aaa").where('i', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('a', CachedBlockPosition.matchesBlockState(AbstractBlock.AbstractBlockState::isAir)).build(), FullIronBeacon.class);
    
    

    public static <M extends io.github.natank25.scp_byo.mutliblock.Multiblock> BlockPattern register(BlockPattern blockPattern, Class<M> multiblockClass){
        BLOCK_PATTERNS.add(blockPattern);
        classBlockPatternMap.put(multiblockClass, blockPattern);
        return blockPattern;
    }
    
    public static Optional<Class<? extends Multiblock>> getMultiblockForBlockPattern(BlockPattern pattern) {
        for (Map.Entry<Class<? extends Multiblock>, BlockPattern> entry : classBlockPatternMap.entrySet()) {
            if (entry.getValue().equals(pattern)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    public static List<BlockPattern> getAll(){
        return BLOCK_PATTERNS;
    }


    public static void registerModBlockPatterns() {
        scp_byo.LOGGER.debug("Registering BlockPatterns for " + scp_byo.MOD_ID);
    }


}
