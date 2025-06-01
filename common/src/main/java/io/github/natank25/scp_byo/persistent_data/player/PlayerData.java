package io.github.natank25.scp_byo.persistent_data.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.natank25.scp_byo.persistent_data.DoesSCP096Exist;
import net.minecraft.nbt.NbtCompound;

public class PlayerData {
	private static final String hasSeenScpKey = "hasSeenScp";
	private boolean hasSeenScp = false;

    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.fieldOf(hasSeenScpKey).forGetter(data -> data.hasSeenScp)
            ).apply(instance, PlayerData::new)
    );

	public static PlayerData getFromNbt(NbtCompound nbt) {
		PlayerData playerData = new PlayerData();
		playerData.setHasSeenScp(nbt.getBoolean(hasSeenScpKey));
		
		return playerData;
	}

    public PlayerData(boolean hasSeenScp){
        this.hasSeenScp = hasSeenScp;
    }
	
	public boolean hasSeenScp() {
		return hasSeenScp;
	}
	
	public void setHasSeenScp(boolean hasSeenScp) {
		this.hasSeenScp = hasSeenScp;
	}
	
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putBoolean(hasSeenScpKey, hasSeenScp);
		return nbt;
	}
}
