package io.github.natank25.scp_byo.persistent_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.networking.NetworkManager;
import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblocks;
import io.github.natank25.scp_byo.persistent_data.player.PerPlayerData;
import io.github.natank25.scp_byo.utils.ModConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.HashMap;

public class ScpByoDataManager extends PersistentState {

	// TODO class that allows an easier sync

	private static final String DataManagerKey = "scp_byo_data";
	private static final String doesSCP096ExistsKey = "doesSCP096Exists";
	private static final String multiblocksKey = "multiblocks";
	private static final String PerPlayerDataKey = "per_player_data";
	private DoesSCP096Exist doesSCP096Exists;
	private Multiblocks multiblocks;
	private PerPlayerData playerData;

    private static final Codec<ScpByoDataManager> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    DoesSCP096Exist.CODEC.fieldOf(doesSCP096ExistsKey).forGetter(data -> data.doesSCP096Exists),
                    PerPlayerData.CODEC.fieldOf(PerPlayerDataKey).forGetter(data -> data.playerData),
                    Multiblocks.CODEC.fieldOf(multiblocksKey).forGetter(data -> data.multiblocks)
            ).apply(instance, ScpByoDataManager::new)
    );

    private static final PersistentStateType<ScpByoDataManager> type = new PersistentStateType<>(
            DataManagerKey,
            context -> new ScpByoDataManager(context.world()),
            context -> CODEC,
            null
    );

    public ScpByoDataManager(DoesSCP096Exist doesSCP096Exists, Multiblocks multiblocks, PerPlayerData playerData){
        this.doesSCP096Exists = doesSCP096Exists;
        this.multiblocks = multiblocks;
        this.playerData = playerData;
    }

	public ScpByoDataManager(World world) {
		doesSCP096Exists = new DoesSCP096Exist(false);
		multiblocks = new Multiblocks(world);
		playerData = new PerPlayerData(new HashMap<>());
	}

	//region Data getters

	public static ScpByoDataManager createFromNbt(NbtCompound nbt, World world) {
		ScpByoDataManager dataManager = new ScpByoDataManager(world);

		dataManager.multiblocks = Multiblocks.createFromNbt(nbt.getCompoundOrEmpty(multiblocksKey), world);

		if (world.getRegistryKey() != World.OVERWORLD) return dataManager;

		dataManager.doesSCP096Exists = DoesSCP096Exist.createFromNbt(nbt.getCompoundOrEmpty(doesSCP096ExistsKey));
		dataManager.playerData = PerPlayerData.createFromNbt(nbt.getCompoundOrEmpty(PerPlayerDataKey));

		return dataManager;
	}

	public static ScpByoDataManager getOrCreate(ServerWorld world) {
		PersistentStateManager stateManager = world.getPersistentStateManager();

		ScpByoDataManager dataManager = stateManager.getOrCreate(type);

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

		NetworkManager.sendToPlayer(player, ModConstants.Networking.ALL_WORLD_DATA_PACKET_ID, buf);
	}

	public void update(NbtCompound nbt, World world) {
		ScpByoDataManager dataManager = createFromNbt(nbt, world);

		this.multiblocks = dataManager.multiblocks;
		this.doesSCP096Exists = dataManager.doesSCP096Exists;
		this.playerData = dataManager.playerData;
	}
}

