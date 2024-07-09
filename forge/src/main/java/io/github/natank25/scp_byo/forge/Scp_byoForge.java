package io.github.natank25.scp_byo.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.block.entity.SlidingDoorBlockEntity;
import io.github.natank25.scp_byo.block.entity.client.SlidingDoorRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Scp_byo.MOD_ID)
public class Scp_byoForge {
    static IEventBus BUS = FMLJavaModLoadingContext.get().getModEventBus();
    public Scp_byoForge() {
        EventBuses.registerModEventBus(Scp_byo.MOD_ID, BUS);
        
        Scp_byo.init();
        
        // BUS.addGenericListener(EntityRenderersEvent, null);
    }
    
    @Mod.EventBusSubscriber(modid = Scp_byo.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents{
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BlockEntityRendererFactories.register(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
        }
    }
}