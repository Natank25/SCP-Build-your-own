package io.github.natank25.scp_byo;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.github.natank25.scp_byo.advancements.ModCriteria;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.commands.ModCommands;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.client.scp_096.Scp096Renderer;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.sounds.ModSounds;
import io.github.natank25.scp_byo.world.gen.ModWorldGeneration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

        RegisterModEvents();
        RegisterCommonNetwork();


        //endregion

        ModWorldGeneration.generateModWorldGen();

        EnvExecutor.runInEnv(Env.CLIENT, () -> Client::onInitializeClient);

        if (Platform.isDevelopmentEnvironment())
                Scp_byo.LOGGER.debug("Hi devs!");
    }

    private static void RegisterModEvents() {
        //TODO WorldSyncCallback.EVENT.register((player, world) -> world.scp_byoGetDataManager().sendLoginPacket(player));
    }

    public static void RegisterCommonNetwork() {
        /* TODO: Change to CODECS
        NetworkManager.registerReceiver(NetworkManager.c2s(), Networking.GRANT_ADVANCEMENT_PACKET_ID, (buf, context) -> {

            String criterionName = buf.readString();
            Identifier advancementId = buf.readIdentifier();
            ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
            MinecraftServer server = player.getServer();
            if (server != null)
                server.execute(() -> player.getAdvancementTracker().grantCriterion(server.getAdvancementLoader().get(advancementId), criterionName));
        });

        NetworkManager.registerReceiver(NetworkManager.c2s(), Networking.UPDATE_PLAYER_DATA, (buf, context) -> {
            ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
            PerPlayerData.getPlayerData(player).setHasSeenScp(true);
        });
         */
    }

    public static void RegisterClientNetwork() {
        /* TODO: Change to CODECS
        NetworkManager.registerReceiver(NetworkManager.s2c(), Networking.MULTIBLOCK_UPDATE_PACKET_ID, ((buf, context) -> {
            RegistryKey<World> worldRegistryKey = buf.readRegistryKey(RegistryKeys.WORLD);
            PlayerEntity player = context.getPlayer();

            if (player.getWorld().getRegistryKey() != worldRegistryKey) return;

            byte updateType = buf.readByte();

            World world = context.getPlayer().getWorld();

            switch (updateType) {
                case Networking.Multiblocks.ADD_MULTIBLOCK: { // Add multiblock
                    BlockPos pos = buf.readBlockPos();
                    world.scp_byoGetDataManager().getMultiblocks().tryAssemble(pos);
                    break;
                }
                case Networking.Multiblocks.REMOVE_MULTIBLOCK: { // Remove multiblock
                    BlockPos pos = buf.readBlockPos();
                    world.scp_byoGetDataManager().getMultiblocks().tryDisassemble(pos);
                    break;
                }
                case Networking.Multiblocks.UPDATE_MULTIBLOCK: { // Generic multiblock update
                    BlockPos pos = buf.readBlockPos();
                    Multiblocks.get(world).getMultiblock(pos).orElseThrow().applyGenericUpdatePacket(buf);
                    break;
                }
            }
        }));

        NetworkManager.registerReceiver(NetworkManager.s2c(), Networking.ALL_WORLD_DATA_PACKET_ID, (buf, context) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            NbtCompound nbt = buf.readNbt();
            client.execute(() -> {
                assert client.world != null;
                client.world.scp_byoGetDataManager().update(nbt, client.world);
            });
        });
        */
    }

    @Environment(EnvType.CLIENT)
    public static class Client {


        @Environment(EnvType.CLIENT)
        public static void onInitializeClient() {
            //Block Entities
            // BlockEntityRendererRegistry.register(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);


            // Non opaque blocks

            EntityRendererRegistry.register(ModEntities.SCP_096, Scp096Renderer::new);


            RegisterClientNetwork();

        }
    }
}
