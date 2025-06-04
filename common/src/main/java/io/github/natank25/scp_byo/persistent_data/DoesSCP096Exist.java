package io.github.natank25.scp_byo.persistent_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class DoesSCP096Exist extends PersistentState {
	
	private static final String DOES_SCP_096_EXIST_KEY = "DoesSCP096Exist";
	private Boolean doesSCP096Exists;
    public static final Codec<DoesSCP096Exist> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.fieldOf(DOES_SCP_096_EXIST_KEY).forGetter(doesSCP096Exist -> doesSCP096Exist.doesSCP096Exists)
            ).apply(instance, DoesSCP096Exist::new)
    );

	private static final PersistentStateType<DoesSCP096Exist> DOES_SCP_096_EXIST_TYPE = new PersistentStateType<>(
			ScpByoDataManager.getFullKey(ScpByoDataManager.DOES_SCP_096_EXISTS_KEY),
            () -> new DoesSCP096Exist(false),
			CODEC,
			null
	);

    public DoesSCP096Exist(boolean doesSCP096Exists){
        this.doesSCP096Exists = doesSCP096Exists;
    }

	public static DoesSCP096Exist get(World world) {
		if (world.isClient())
			return null;
		return get((ServerWorld) world);
	}
	public static DoesSCP096Exist get(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(DOES_SCP_096_EXIST_TYPE);
	}
	
	public boolean getDoesSCP096Exist() {
		return this.doesSCP096Exists;
	}
	
	public void setDoesSCP096Exists(Boolean doesSCP096Exists) {
		this.doesSCP096Exists = doesSCP096Exists;
		this.markDirty();
	}
}
