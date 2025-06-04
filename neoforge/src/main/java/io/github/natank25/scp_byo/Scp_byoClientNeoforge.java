package io.github.natank25.scp_byo;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(value = Scp_byo.MOD_ID, dist = Dist.CLIENT)
public class Scp_byoClientNeoforge {

    public Scp_byoClientNeoforge(FMLModContainer container, IEventBus modBus, Dist dist) {
        Scp_byoClient.init();
    }
}
