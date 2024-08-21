package io.github.natank25.scp_byo.persistent_data.multiblock;

import io.github.natank25.scp_byo.persistent_data.multiblock.ModMultiblocks.FullIronBeacon;
import io.github.natank25.scp_byo.persistent_data.multiblock.ModMultiblocks.SCP096Cage;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.Identifier;

import java.util.*;

public enum BlockPatternsRegistry {
    ;
    
    //public static final RegistryKey<Registry<BlockPattern>> BLOCK_PATTERN = RegistryKey.ofRegistry(Utils.newIdentifier("block_patterns"));
    
    private static final List<BlockPattern> BLOCK_PATTERNS = new ArrayList<>();
    //public static final DeferredRegister<BlockPattern> BLOCK_PATTERNS = DeferredRegister.create(Scp_byo.MOD_ID, BLOCK_PATTERN);
    private static final Map<Class<? extends Multiblock>, BlockPattern> classBlockPatternMap = new HashMap<>();
    //private static final Map<Identifier, Class<? extends Multiblock>> idToClassMap = new HashMap<>();
    
    
    public static final BlockPattern SCP_096_CHAMBER = register(Utils.newIdentifier("scp_096_multiblock"), SCP096Cage.getBlockPattern(), SCP096Cage.class);
    public static final BlockPattern TEST = register(Utils.newIdentifier("iron_beacon_multiblock"), BlockPatternBuilder.start().aisle("iii", "aaa").aisle("iii", "aia").aisle("iii", "aaa").where('i', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('a', CachedBlockPosition.matchesBlockState(AbstractBlock.AbstractBlockState::isAir)).build(), FullIronBeacon.class); //TODO try to replace FullIronBeacon.class to Multiblock.class
    
    
    public static <M extends io.github.natank25.scp_byo.persistent_data.multiblock.Multiblock> BlockPattern register(Identifier id, BlockPattern blockPattern, Class<M> multiblockClass) {
        BLOCK_PATTERNS.add(blockPattern);
        classBlockPatternMap.put(multiblockClass, blockPattern);
        //idToClassMap.put(id, multiblockClass);
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
}
