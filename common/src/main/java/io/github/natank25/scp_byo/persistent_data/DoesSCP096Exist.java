package io.github.natank25.scp_byo.persistent_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

public class DoesSCP096Exist extends PersistentState {
	public static final String KEY = "does_scp096_exist";
	public static final Codec<DoesSCP096Exist> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Codec.BOOL.fieldOf(KEY).forGetter(DoesSCP096Exist::doesSCP096Exist)
			).apply(instance, DoesSCP096Exist::new));

	public static final PersistentStateType<DoesSCP096Exist> TYPE = Utils.createPersistentStateType(KEY,
			() -> new DoesSCP096Exist(false),
			CODEC);

	private Boolean doesSCP096Exists;

	public DoesSCP096Exist(boolean doesSCP096Exists) {
		this.doesSCP096Exists = doesSCP096Exists;
	}

	public void setDoesSCP096Exist(Boolean doesSCP096Exists) {
		this.doesSCP096Exists = doesSCP096Exists;
		this.markDirty();
	}

	public boolean doesSCP096Exist() {
		return this.doesSCP096Exists;
	}

	public static DoesSCP096Exist get(MinecraftServer server){
		return server.getOverworld().getPersistentStateManager().getOrCreate(TYPE);
	}

	public static DoesSCP096Exist get(ServerWorld world){
		return world.getPersistentStateManager().getOrCreate(TYPE);
	}

}
