package io.github.natank25.scp_byo.data;

import io.github.natank25.scp_byo.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {

    public ModLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        this.addDrop(ModBlocks.CONCRETE_FLOOR);
        this.addDrop(ModBlocks.CONCRETE_WALL);
        this.addDrop(ModBlocks.DIRTY_METAL);
        this.addDrop(ModBlocks.ELEVATOR_FLOOR);
        this.addDrop(ModBlocks.ELEVATOR_WALL);
        this.addDrop(ModBlocks.FONDATION_GLASS);
        this.addDrop(ModBlocks.KEYCARD_READER);
        this.addDrop(ModBlocks.OFFICE_WALL);
        this.addDrop(ModBlocks.TILE_FLOOR);
        this.addDrop(ModBlocks.WHITE_WALL);

        this.addDrop(ModBlocks.SLIDING_DOOR, this.doorDrops(ModBlocks.SLIDING_DOOR));
    }
}
