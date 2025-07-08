package io.github.natank25.scp_byo.fabric.data;

import dev.architectury.platform.Mod;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.utils.Utils;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.FOUNDATION_GLASS.get());
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.TILE_FLOOR.get());
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.ELEVATOR_WALL.get());
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.CONCRETE_FLOOR.get());
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DIRTY_METAL.get());
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.ELEVATOR_FLOOR.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.KEYCARD_1.get(), Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_2.get(), Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_3.get(), Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_4.get(), Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_5.get(), Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_6.get(), Models.GENERATED);
        itemModelGenerator.register(ModItems.WRENCH.get(), Models.GENERATED);
        itemModelGenerator.register(ModItems.SCP_096_SPAWN_EGG.get(), Models.GENERATED);

    }
}
