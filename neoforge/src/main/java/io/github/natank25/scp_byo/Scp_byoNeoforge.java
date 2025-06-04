package io.github.natank25.scp_byo;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Scp_byo.MOD_ID)
public class Scp_byoNeoforge {
    public Scp_byoNeoforge(IEventBus modBus, ModContainer container) {
        Scp_byo.init();
    }
}
