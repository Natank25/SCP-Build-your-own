package io.github.natank25.scp_byo.block.multiblocks;

import io.github.natank25.scp_byo.block.multiblocks.in_world.MultiblockBE;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class MultiblockDefinition<BE extends MultiblockBE> {
    final Identifier id;
    final BEFactory<BE> beFactory;

    private final static Map<Identifier, MultiblockDefinition<?>> REGISTRY = new HashMap<>();


    public MultiblockDefinition(Identifier id, BEFactory<BE> beFactory) {
        this.id = id;
        this.beFactory = beFactory;
    }

    public abstract boolean canAssemble(World world, BlockPos pos, Direction triggerDirection, PlayerEntity player);

    public abstract boolean assemble(World world, BlockPos pos, Direction triggerDirection, PlayerEntity player);

    public @Nullable BE createBlockEntity(BlockPos pos, BlockState state) {
        return beFactory.apply(pos, state);
    }

    public interface BEFactory<BE extends MultiblockBE> {
        BE apply(BlockPos pos, BlockState state);
    }

    public static <B extends MultiblockBE> MultiblockDefinition<B> register(Identifier id, MultiblockDefinition<B> multiblockDefinition) {
        REGISTRY.put(id, multiblockDefinition);
        return multiblockDefinition;
    }
    
    public static Collection<MultiblockDefinition<?>> getRegistry() {
        return REGISTRY.values();
    }
}
