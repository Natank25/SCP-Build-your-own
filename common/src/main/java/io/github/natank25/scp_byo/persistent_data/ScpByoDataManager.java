package io.github.natank25.scp_byo.persistent_data;

import dev.architectury.networking.NetworkManager;
import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblocks;
import io.github.natank25.scp_byo.persistent_data.player.PerPlayerData;
import io.github.natank25.scp_byo.utils.ModConstants;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class ScpByoDataManager extends PersistentState {
	
	// TODO class that allows an easier sync
	
	private static final String DataManagerKey = "scp_byo_data";
	private static final String doesSCP096ExistsKey = "doesSCP096Exists";
	private static final String multiblocksKey = "multiblocks";
	private static final String PerPlayerDataKey = "per_player_data";
	private DoesSCP096Exist doesSCP096Exists;
	private Multiblocks multiblocks;
	private PerPlayerData playerData;
	
	
	public ScpByoDataManager(World world) {
		doesSCP096Exists = new DoesSCP096Exist();
		multiblocks = new Multiblocks(world);
		playerData = new PerPlayerData();
	}
	
	//region Data getters
	
	public static ScpByoDataManager createFromNbt(NbtCompound nbt, World world) {
		ScpByoDataManager dataManager = new ScpByoDataManager(world);
		
		dataManager.multiblocks = Multiblocks.createFromNbt(nbt.getCompound(multiblocksKey), world);
		
		if (world.getRegistryKey() != World.OVERWORLD) return dataManager;
		
		dataManager.doesSCP096Exists = DoesSCP096Exist.createFromNbt(nbt.getCompound(doesSCP096ExistsKey));
		dataManager.playerData = PerPlayerData.createFromNbt(nbt.getCompound(PerPlayerDataKey));
		
		return dataManager;
	}
	
	public static ScpByoDataManager getOrCreate(ServerWorld world) {
		PersistentStateManager stateManager = world.getPersistentStateManager();
		
		ScpByoDataManager dataManager = stateManager.getOrCreate(nbtCompound -> createFromNbt(nbtCompound, world), () -> new ScpByoDataManager(world), DataManagerKey);
		
		dataManager.markDirty();
		
		return dataManager;
	}
	
	public DoesSCP096Exist getDoesSCP096Exists() {
		return doesSCP096Exists;
	}
	
	//endregion
	
	public Multiblocks getMultiblocks() {
		return multiblocks;
	}
	
	public PerPlayerData getPerPlayerData() {
		return playerData;
	}
	
	@Override
	public void markDirty() {
		this.doesSCP096Exists.markDirty();
		this.multiblocks.markDirty();
		this.playerData.markDirty();
		
		super.markDirty();
	}
	
	public void sendLoginPacket(ServerPlayerEntity player) {
		
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeNbt(this.writeNbt(new NbtCompound()));
		
		NetworkManager.sendToPlayer(player, ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, buf);
	}
	
	public void update(NbtCompound nbt, World world) {
		ScpByoDataManager dataManager = createFromNbt(nbt, world);
		
		this.multiblocks = dataManager.multiblocks;
		this.doesSCP096Exists = dataManager.doesSCP096Exists;
		this.playerData = dataManager.playerData;
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		
		nbt.put(multiblocksKey, multiblocks.writeNbt(new NbtCompound()));
		nbt.put(doesSCP096ExistsKey, doesSCP096Exists.writeNbt(new NbtCompound()));
		nbt.put(PerPlayerDataKey, playerData.writeNbt(new NbtCompound()));
		
		return nbt;
	}
	
}

