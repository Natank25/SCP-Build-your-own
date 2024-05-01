package io.github.natank25.scp_byo.fabric;

import io.github.natank25.scp_byo.Scp_byo;
import net.fabricmc.api.ModInitializer;

public class Scp_byoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Scp_byo.init();
    }
}