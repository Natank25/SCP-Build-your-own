package io.github.natank25.scp_byo.forge;

import io.github.natank25.scp_byo.Scp_byo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import dev.architectury.platform.forge.EventBuses;

import static io.github.natank25.scp_byo.Scp_byo.MOD_ID;

@Mod(MOD_ID)
public class Scp_byoForge {
    public Scp_byoForge() {
        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Scp_byo.init();
        
    }
}