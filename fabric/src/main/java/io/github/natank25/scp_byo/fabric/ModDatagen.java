package io.github.natank25.scp_byo.fabric;

import io.github.natank25.scp_byo.fabric.data.ModAdvancementsProvider;
import io.github.natank25.scp_byo.fabric.data.ModEnglishLangProvider;
import io.github.natank25.scp_byo.fabric.data.ModLootTableGenerator;
import io.github.natank25.scp_byo.fabric.data.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {

        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModEnglishLangProvider::new);
        pack.addProvider(ModLootTableGenerator::new);
        pack.addProvider(ModAdvancementsProvider::new);
        pack.addProvider(ModModelProvider::new);
    }
}
