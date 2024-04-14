package io.github.natank25.scp_byo.world.gen;

import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;

public enum ModWorldGeneration {
    ;

    public static void generateModWorldGen() {

        addSpawns();
    }


    private static void addSpawns() {
        BiomeModifications.addSpawn((selector) -> true, SpawnGroup.AMBIENT, ModEntities.SCP_096, 50, 1, 1);
        SpawnRestriction.register(ModEntities.SCP_096, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Scp_096Entity::isValidNaturalSpawn);

    }
}