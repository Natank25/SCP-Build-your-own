package io.github.natank25.scp_byo.data;

import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.FONDATION_GLASS);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.TILE_FLOOR);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.ELEVATOR_WALL);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.CONCRETE_FLOOR);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DIRTY_METAL);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.ELEVATOR_FLOOR);
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.KEYCARD_READER, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(ModBlocks.KEYCARD_READER))).coordinate(BlockStateModelGenerator.createSouthDefaultHorizontalRotationStates()));
        
        blockStateModelGenerator.registerParentedItemModel(ModItems.SCP_096_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
    }
    
    

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.KEYCARD_1, Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_2, Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_3, Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_4, Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_5, Models.GENERATED);
        itemModelGenerator.register(ModItems.KEYCARD_6, Models.GENERATED);
        itemModelGenerator.register(ModItems.WRENCH, Models.GENERATED);
    }
}
