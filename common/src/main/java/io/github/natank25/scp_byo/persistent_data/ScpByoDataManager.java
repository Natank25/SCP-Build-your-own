package io.github.natank25.scp_byo.persistent_data;

import dev.architectury.networking.NetworkManager;
import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblocks;
import io.github.natank25.scp_byo.persistent_data.player.PerPlayerData;
import io.github.natank25.scp_byo.utils.ModConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.HashMap;

public class ScpByoDataManager {

	private static final String MOD_DATA_FOLDER_KEY = "scp_byo";
	public static final String DOES_SCP_096_EXISTS_KEY = "doesSCP096Exists";
	public static final String MULTIBLOCKS_KEY = "multiblocks";
	public static final String PER_PLAYER_DATA_KEY = "per_player_data";
	private final DoesSCP096Exist doesSCP096Exists;
	private final Multiblocks multiblocks;
	private final PerPlayerData perPlayerData;

	public ScpByoDataManager(World world) {
		doesSCP096Exists = new DoesSCP096Exist(false);
		multiblocks = new Multiblocks(world);
		perPlayerData = new PerPlayerData(new HashMap<>());
	}

	public static String getFullKey(String key){
		return MOD_DATA_FOLDER_KEY + "/" + key;
	}

	//region Data getters


	public DoesSCP096Exist getDoesSCP096Exists() {
		return doesSCP096Exists;
	}

	//endregion

	public Multiblocks getMultiblocks() {
		return multiblocks;
	}

	public PerPlayerData getPerPlayerData() {
		return perPlayerData;
	}

	public void sendLoginPacket(ServerPlayerEntity player) {

		// TODO (or not) NetworkManager.sendToPlayer(player, ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, buf);
	}
}

