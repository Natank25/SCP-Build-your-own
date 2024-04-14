package io.github.natank25.scp_byo.persistent_data.cca.register;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import io.github.natank25.scp_byo.mutliblock.Multiblocks;
import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.util.Identifier;

public class ModWorldComponents implements WorldComponentInitializer {
    public static final ComponentKey<Multiblocks> MULTIBLOCKS = ComponentRegistry.getOrCreate(new Identifier(scp_byo.MOD_ID, "multiblocks"), Multiblocks.class);
    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(MULTIBLOCKS, Multiblocks::new);

    }
}
