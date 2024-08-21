package io.github.natank25.scp_byo.persistent_data;

import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class ScpByoDataManager extends PersistentState {
	
	private DoesSCP096Exist doesSCP096Exists;
	private static final String doesSCP096ExistsKey = "doesSCP096Exists";
	
	public DoesSCP096Exist getDoesSCP096Exists() {
		return doesSCP096Exists;
	}
	private Multiblocks multiblocks;
	private static final String multiblocksKey = "multiblocks";
	private static final String DataManagerKey = "scp_byo_data";
	
	
	public Multiblocks getMultiblocks() {
		return multiblocks;
	}
	
	public ScpByoDataManager(World world) {
		doesSCP096Exists = new DoesSCP096Exist();
		multiblocks = new Multiblocks(world);
	}
	
	public static ScpByoDataManager createFromNbt(NbtCompound nbt, World world) {
		ScpByoDataManager dataManager = new ScpByoDataManager(world);
		
		dataManager.doesSCP096Exists = DoesSCP096Exist.createFromNbt(nbt.getCompound(doesSCP096ExistsKey));
		dataManager.multiblocks = Multiblocks.createFromNbt(nbt.getCompound(multiblocksKey), world);
		
		return dataManager;
	}
	
	@Override
	public void markDirty() {
		this.doesSCP096Exists.markDirty();
		this.multiblocks.markDirty();
		
		super.markDirty();
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put(doesSCP096ExistsKey, doesSCP096Exists.writeNbt(new NbtCompound()));
		nbt.put(multiblocksKey, multiblocks.writeNbt(new NbtCompound()));
		
		return nbt;
	}
	
	public static ScpByoDataManager getOrCreate(ServerWorld world) {
		PersistentStateManager stateManager = world.getPersistentStateManager();
		
		ScpByoDataManager dataManager = stateManager.getOrCreate(nbtCompound -> createFromNbt(nbtCompound, world), () -> new ScpByoDataManager(world), DataManagerKey);
		
		dataManager.markDirty();
		
		return dataManager;
	}
	
	public void update(NbtCompound nbt, World world) {
		ScpByoDataManager dataManager = createFromNbt(nbt, world);
		this.multiblocks = dataManager.multiblocks;
		this.doesSCP096Exists = dataManager.doesSCP096Exists;
	}
	
	
}

