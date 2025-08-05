package io.github.natank25.scp_byo;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.github.natank25.scp_byo.advancements.ModCriteria;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.block.multiblockBAK.ModMultiblocks;
import io.github.natank25.scp_byo.block.multiblockBAK.StructureMultiblock;
import io.github.natank25.scp_byo.commands.ModCommands;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.client.scp_096.Scp096Renderer;
import io.github.natank25.scp_byo.events.ModEvents;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.networking.GrantAdvancementPayload;
import io.github.natank25.scp_byo.networking.MultiblockStructurePayload;
import io.github.natank25.scp_byo.sounds.ModSounds;
import io.github.natank25.scp_byo.world.gen.ModWorldGeneration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Scp_byo {
    public static final String MOD_ID = "scp_byo";

    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        //region registering
        ModSounds.registerModSounds();

        ModBlocks.registerModBlocks();
        ModItems.registerItemGroups();
        ModItems.registerItems();

        ModEntities.registerEntities();
        ModBlocksEntities.registerAllBlockEntity();

        ModCriteria.registerModCriteria();

        ModCommands.registerCommands();
        ModGamerules.registerModGamerules();
        ModMultiblocks.registerMultiblocks();

        ModEvents.registerEvents();
        RegisterCommonNetwork();

        //endregion

        ModWorldGeneration.generateModWorldGen();

        EnvExecutor.runInEnv(Env.CLIENT, () -> Client::onInitializeClient);
        EnvExecutor.runInEnv(Env.SERVER, () -> Server::onInitializeServer);

        if (Platform.isDevelopmentEnvironment()) {
            LOGGER.info("Hi devs!");
        }
    }

    public static void RegisterCommonNetwork() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), GrantAdvancementPayload.ID, GrantAdvancementPayload.CODEC, (payload, context) -> {
            ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
            MinecraftServer server = player.getServer();
            if (server != null)
                server.execute(() -> player.getAdvancementTracker().grantCriterion(server.getAdvancementLoader().get(payload.id()), payload.criterion()));
        });
        /* TODO: Change to CODECS
        NetworkManager.registerReceiver(NetworkManager.c2s(), Networking.UPDATE_PLAYER_DATA, (buf, context) -> {
            ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
            PerPlayerData.getPlayerData(player).setHasSeenScp(true);
        });
         */
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        public static void RegisterClientNetwork() {
            NetworkManager.registerReceiver(NetworkManager.s2c(), MultiblockStructurePayload.PACKET_ID, MultiblockStructurePayload.CODEC, (value, context) -> {
                value.structures().forEach(multiblockStructureData ->
                        StructureMultiblock.addStructure(multiblockStructureData.id(), multiblockStructureData.template()));
            });
        }

        @Environment(EnvType.CLIENT)
        public static void onInitializeClient() {
            //Block Entities
            // BlockEntityRendererRegistry.register(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);


            // Non opaque blocks

            EntityRendererRegistry.register(ModEntities.SCP_096, Scp096Renderer::new);


            RegisterClientNetwork();

        }
    }

    @Environment(EnvType.SERVER)
    public static class Server {

        public static void RegisterServerNetwork() {
            NetworkManager.registerS2CPayloadType(MultiblockStructurePayload.PACKET_ID, MultiblockStructurePayload.CODEC);
        }

        @Environment(EnvType.SERVER)
        public static void onInitializeServer() {
            RegisterServerNetwork();

        }
    }
}
