package io.github.natank25.scp_byo.neoforge;

import dev.architectury.platform.forge.EventBuses;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.block.entity.client.SlidingDoorRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Scp_byo.MOD_ID)
public class Scp_byoForge {
    static final IEventBus BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public Scp_byoForge() {
        EventBuses.registerModEventBus(Scp_byo.MOD_ID, BUS);

        Scp_byo.init();
    }

    @Mod.EventBusSubscriber(modid = Scp_byo.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BlockEntityRendererFactories.register(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
        }
    }
}