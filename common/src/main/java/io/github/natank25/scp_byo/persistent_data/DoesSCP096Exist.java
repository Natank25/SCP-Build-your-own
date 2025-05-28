package io.github.natank25.scp_byo.persistent_data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

public class DoesSCP096Exist extends PersistentState {
	
	private static final String DOES_SCP_096_EXIST_KEY = "DoesSCP096Exist";
	private Boolean doesSCP096Exists = false;
	
	public static DoesSCP096Exist createFromNbt(NbtCompound tag) {
		DoesSCP096Exist state = new DoesSCP096Exist();
		state.doesSCP096Exists = tag.getBoolean(DOES_SCP_096_EXIST_KEY);
		return state;
	}
	
	public static DoesSCP096Exist get(World world) {
		return world.scp_byoGetDataManager().getDoesSCP096Exists();
	}
	
	public boolean getDoesSCP096Exist() {
		return this.doesSCP096Exists;
	}
	
	public void setDoesSCP096Exists(Boolean doesSCP096Exists) {
		this.doesSCP096Exists = doesSCP096Exists;
		this.markDirty();
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putBoolean(DOES_SCP_096_EXIST_KEY, this.doesSCP096Exists);
		return nbt;
	}
	
	
}
