package io.github.natank25.scp_byo.fabric.data;

import io.github.natank25.scp_byo.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {

    public ModLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        this.addDrop(ModBlocks.CONCRETE_FLOOR.get());
        this.addDrop(ModBlocks.CONCRETE_WALL.get());
        this.addDrop(ModBlocks.DIRTY_METAL.get());
        this.addDrop(ModBlocks.ELEVATOR_FLOOR.get());
        this.addDrop(ModBlocks.ELEVATOR_WALL.get());
        this.addDrop(ModBlocks.FONDATION_GLASS.get());
        this.addDrop(ModBlocks.KEYCARD_READER.get());
        this.addDrop(ModBlocks.OFFICE_WALL.get());
        this.addDrop(ModBlocks.TILE_FLOOR.get());
        this.addDrop(ModBlocks.WHITE_WALL.get());

        this.addDrop(ModBlocks.SLIDING_DOOR.get(), this.doorDrops(ModBlocks.SLIDING_DOOR.get()));
    }
}
