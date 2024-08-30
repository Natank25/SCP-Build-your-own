package io.github.natank25.scp_byo.persistent_data.player;

import net.minecraft.nbt.NbtCompound;

public class PlayerData {
	private static final String hasSeenScpKey = "hasSeenScp";
	private boolean hasSeenScp = false;
	
	public static PlayerData getFromNbt(NbtCompound nbt) {
		PlayerData playerData = new PlayerData();
		playerData.setHasSeenScp(nbt.getBoolean(hasSeenScpKey));
		
		return playerData;
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
