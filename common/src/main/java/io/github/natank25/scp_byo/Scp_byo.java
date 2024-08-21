package io.github.natank25.scp_byo;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.github.natank25.scp_byo.advancements.ModCriterions;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.block.entity.ModBlocksEntities;
import io.github.natank25.scp_byo.commands.ModCommands;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.client.scp_096.Scp096Renderer;
import io.github.natank25.scp_byo.events.WorldSyncCallback;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.sounds.ModSounds;
import io.github.natank25.scp_byo.utils.ModConstants;
import io.github.natank25.scp_byo.world.gen.ModWorldGeneration;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;

public class Scp_byo
{
	public static final String MOD_ID = "scp_byo";
	
	public static final Logger LOGGER = LogManager.getLogger();

	public static void init() {
		//region registering
		
		ModSounds.registerModSounds();
		
		ModBlocks.registerModBlocks();
		ModItems.registerItems();
		
		ModEntities.registerEntities();
		ModBlocksEntities.registerAllBlockEntity();
		ModCriterions.registerModCriterions();
		ModCommands.registerCommands();
		//endregion
		
		ModWorldGeneration.generateModWorldGen();
		ModGamerules.registerModGamerules();
		
		EnvExecutor.runInEnv(Env.CLIENT, () -> Client::onInitializeClient);
		
		GeckoLib.initialize();
		/*
		PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
			LOGGER.warn("PLAYER_CLONE");
			
			NetworkManager.sendToServer(ModConstants.Networking.REQUEST_WORLD_DATA_PACKET_ID, new PacketByteBuf(Unpooled.EMPTY_BUFFER));
		});
		
		NetworkManager.registerReceiver(NetworkManager.c2s(), ModConstants.Networking.REQUEST_WORLD_DATA_PACKET_ID, (buf, context) -> {
			LOGGER.warn("REQUEST_WORLD_DATA_PACKET_ID");
			ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
			NbtCompound tag = ((World) player.getWorld()).scp_byoGetDataManager().writeNbt(new NbtCompound());
			buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeNbt(tag);
			Scp_byo.LOGGER.warn(player.getWorld().isChunkLoaded(player.getBlockX(), player.getBlockZ()));
			player.sendMessage(Text.literal("Send update packet 2"));
			NetworkManager.sendToPlayer(player, ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, buf);
		});*/
		
		PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			
			NbtCompound tag = ((World) player.getWorld()).scp_byoGetDataManager().writeNbt(new NbtCompound());
			buf.writeNbt(tag);
			Scp_byo.LOGGER.warn("Send update packet from change dim");
			NetworkManager.sendToPlayer(player, ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, buf);
			
		});
		
		PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			
			NbtCompound tag = ((World) newPlayer.getWorld()).scp_byoGetDataManager().writeNbt(new NbtCompound());
			buf.writeNbt(tag);
			Scp_byo.LOGGER.warn("Send update packet from player clone");
			NetworkManager.sendToPlayer(newPlayer, ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, buf);
		});
		
		PlayerEvent.PLAYER_RESPAWN.register((newPlayer, conqueredEnd) -> {
			
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			
			NbtCompound tag = ((World) newPlayer.getWorld()).scp_byoGetDataManager().writeNbt(new NbtCompound());
			buf.writeNbt(tag);
			Scp_byo.LOGGER.warn("Send update packet from player respawn");
			NetworkManager.sendToPlayer(newPlayer, ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, buf);
		});
		
		WorldSyncCallback.EVENT.register((player, world) -> {
			
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			
			NbtCompound tag = ((World) world).scp_byoGetDataManager().writeNbt(new NbtCompound());
			buf.writeNbt(tag);
			Scp_byo.LOGGER.warn("Send update packet");
			//player.networkHandler.sendPacket(new CustomPayloadS2CPacket(ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, buf));
			//NetworkManager.sendToPlayer(player, ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, buf);
		});
	}
	
	public static void InitializeNetwork() {
	
	}
	
	public static void InitializeClientNetwork() {

		/*
        
        ClientPlayNetworking.registerGlobalReceiver(ModConstants.Networking.DESTROY_MULTIBLOCK_PACKET_ID, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            client.execute(() ->
                    ModWorldComponents.MULTIBLOCKS.get(client.world).tryDisassemble(pos));
        });
        */
		
		NetworkManager.registerReceiver(NetworkManager.s2c(), ModConstants.Networking.MULTIBLOCK_UPDATE_PACKET_ID, ((buf, context) -> {
			RegistryKey<World> worldRegistryKey = buf.readRegistryKey(RegistryKeys.WORLD);
			PlayerEntity player = context.getPlayer();
			
			if (player.getWorld().getRegistryKey() != worldRegistryKey) return;
			
			byte updateType = buf.readByte();
			
			World world = context.getPlayer().getWorld();
			player.sendMessage(Text.literal(String.format("%2s", Integer.toBinaryString(updateType)).replace(' ', '0')));
			
			switch (updateType) {
				case 0b01: { // Add multiblock
					player.sendMessage(Text.literal("Add"));
					BlockPos pos = buf.readBlockPos();
					world.scp_byoGetDataManager().getMultiblocks().tryAssemble(pos);
					break;
				}
				case 0b10: { // Remove multiblock
					player.sendMessage(Text.literal("Remove"));
					BlockPos pos = buf.readBlockPos();
					world.scp_byoGetDataManager().getMultiblocks().tryDisassemble(pos);
					break;
				}
				case 0b11: { // Generic multiblock update
					player.sendMessage(Text.literal("Generic Update"));
					break;
				}
			}
		}));
		
		NetworkManager.registerReceiver(NetworkManager.s2c(), ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, (buf, context) -> {
			ClientWorld world = MinecraftClient.getInstance().world;
			((World) world).scp_byoGetDataManager().update(buf.readNbt(), world);
		});
		
	}
	
	@Environment(EnvType.CLIENT)
	public static class Client{
		
		
		@Environment(EnvType.CLIENT)
		public static void onInitializeClient() {
			//Block Entities
			// BlockEntityRendererRegistry.register(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
			
			
			// Non opaque blocks
			
			EntityRendererRegistry.register(ModEntities.SCP_096, Scp096Renderer::new);
			
			
			InitializeClientNetwork();
			
		}
	}
	
}
