package io.github.natank25.scp_byo.world.gen;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.SpawnSettings;

public enum ModWorldGeneration {
    ;

    public static void generateModWorldGen() {

        addSpawns();
    }


    private static void addSpawns() {
        
        BiomeModifications.addProperties((biomeContext, mutable) -> {
            mutable.getSpawnProperties().addSpawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(ModEntities.SCP_096.get(), 50,1,1));
        });
        
        SpawnPlacementsRegistry.register(ModEntities.SCP_096, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Scp_096Entity::isValidNaturalSpawn);

    }
}