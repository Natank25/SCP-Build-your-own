package io.github.natank25.scp_byo.persistent_data;

import io.github.natank25.scp_byo.Scp_byo;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.Objects;

public class DoesSCP096Exist extends PersistentState {
	
	public static final Identifier DOES_SCP096_EXISTS = new Identifier(Scp_byo.MOD_ID, "does_scp096_exists");
	private static final String DOES_SCP_096_EXIST_KEY = "DoesSCP096Exist";
	public Boolean doesSCP096Exists = false;
	
	public static DoesSCP096Exist createFromNbt(NbtCompound tag) {
		DoesSCP096Exist state = new DoesSCP096Exist();
		state.doesSCP096Exists = tag.getBoolean(DOES_SCP_096_EXIST_KEY);
		return state;
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putBoolean(DOES_SCP_096_EXIST_KEY, this.doesSCP096Exists);
		return nbt;
	}
	
	
}
