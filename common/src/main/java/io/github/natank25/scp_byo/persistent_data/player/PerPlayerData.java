package io.github.natank25.scp_byo.persistent_data.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.natank25.scp_byo.persistent_data.DoesSCP096Exist;
import io.github.natank25.scp_byo.persistent_data.ScpByoDataManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PerPlayerData extends PersistentState {
	final Map<UUID, PlayerData> playerData;
    public static final Codec<Map<UUID, PlayerData>> PER_PLAYER_DATA_CODEC =
            Codec.unboundedMap(Codec.STRING.xmap(UUID::fromString, UUID::toString), PlayerData.CODEC);
    public static final Codec<PerPlayerData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    PER_PLAYER_DATA_CODEC.fieldOf("per_player_data").forGetter(perPlayerData -> perPlayerData.playerData)
            ).apply(instance, PerPlayerData::new)
    );

    private static final PersistentStateType<PerPlayerData> PER_PLAYER_DATA_TYPE = new PersistentStateType<>(
            ScpByoDataManager.getFullKey(ScpByoDataManager.PER_PLAYER_DATA_KEY),
            PerPlayerData::new,
            CODEC,
            null
    );

    public PerPlayerData(){
        this.playerData = new HashMap<>();
    }

    public PerPlayerData(Map<UUID, PlayerData> data){
        this.playerData = data;
    }

	public static PerPlayerData get(World world) {
		if (world.isClient())
            return null;
        return get((ServerWorld) world);
	}
	public static PerPlayerData get(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(PER_PLAYER_DATA_TYPE);
	}
	
	public static PlayerData getPlayerData(PlayerEntity player) {
		return get(player.getWorld()).playerData.computeIfAbsent(player.getUuid(), uuid -> new PlayerData(false));
	}
}
