package io.github.natank25.scp_byo.persistent_data;

import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Objects;

public class ScpByoDataManager extends PersistentState {
	
	private DoesSCP096Exist doesSCP096Exists;
	private static final String doesSCP096ExistsKey = "doesSCP096Exists";
	
	public DoesSCP096Exist getDoesSCP096Exists() {
		return doesSCP096Exists;
	}
	private Multiblocks multiblocks;
	private static final String multiblocksKey = "multiblocks";
	private static ScpByoDataManager instance;
	
	
	public Multiblocks getMultiblocks() {
		return multiblocks;
	}
	
	public ScpByoDataManager(World world) {
		doesSCP096Exists = new DoesSCP096Exist();
		multiblocks = new Multiblocks(world);
	}
	
	private static ScpByoDataManager createFromNbt(NbtCompound nbt, World world){
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
	
	public static ScpByoDataManager getInstance(MinecraftServer server, World world) {
		if (instance != null) {
			return instance;
		}
		
		
		ServerWorld serverWorld = server.getWorld(world.getRegistryKey());
		PersistentStateManager persistentStateManager = Objects.requireNonNull(serverWorld).getPersistentStateManager();
		ScpByoDataManager dataManager = persistentStateManager.getOrCreate((nbtCompound) -> ScpByoDataManager.createFromNbt(nbtCompound, serverWorld), () -> new ScpByoDataManager(world), "scp_byo");
		
		dataManager.markDirty();
		
		instance = dataManager;
		
		return dataManager;
	}

}

