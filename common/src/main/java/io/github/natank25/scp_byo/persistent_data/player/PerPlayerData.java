package io.github.natank25.scp_byo.persistent_data.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class PerPlayerData extends PersistentState {
	HashMap<UUID, PlayerData> playerData = new HashMap<>();
	
	public static PerPlayerData createFromNbt(NbtCompound nbt) {
		PerPlayerData perPlayerData = new PerPlayerData();
		
		nbt.getKeys().forEach(key -> {
			PlayerData playerData = PlayerData.getFromNbt(nbt.getCompound(key));
			
			UUID uuid = UUID.fromString(key);
			perPlayerData.playerData.put(uuid, playerData);
		});
		
		return perPlayerData;
	}
	
	public static PerPlayerData get(World world) {
		return world.scp_byoGetDataManager().getPerPlayerData();
	}
	
	public static PlayerData getPlayerData(PlayerEntity player) {
		return get(player.getWorld()).playerData.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		playerData.forEach((uuid, playerData1) -> nbt.put(uuid.toString(), playerData1.writeNbt(new NbtCompound())));
		return nbt;
	}
}
