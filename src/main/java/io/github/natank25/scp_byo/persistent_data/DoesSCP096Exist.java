package io.github.natank25.scp_byo.persistent_data;

import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class DoesSCP096Exist extends PersistentState {

    public static final Identifier DOES_SCP096_EXISTS = new Identifier(scp_byo.MOD_ID, "does_scp096_exists");
    private static final String DOES_SCP_096_EXIST_KEY = "DoesSCP096Exist";
    public Boolean doesSCP096Exists = false;

    private static DoesSCP096Exist createFromNbt(NbtCompound tag) {
        DoesSCP096Exist state = new DoesSCP096Exist();
        state.doesSCP096Exists = tag.getBoolean(DOES_SCP_096_EXIST_KEY);
        return state;
    }

    public static DoesSCP096Exist getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        DoesSCP096Exist state = persistentStateManager.getOrCreate(DoesSCP096Exist::createFromNbt, DoesSCP096Exist::new, "scp_byo");

        state.markDirty();
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean(DOES_SCP_096_EXIST_KEY, this.doesSCP096Exists);
        return nbt;
    }


}
