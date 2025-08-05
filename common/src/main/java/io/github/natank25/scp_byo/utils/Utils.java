package io.github.natank25.scp_byo.utils;

import com.mojang.serialization.Codec;
import io.github.natank25.scp_byo.Scp_byo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public enum Utils {
	;
	
	public static Identifier newIdentifier(String path) {
		return Identifier.of(Scp_byo.MOD_ID, path);
	}
	public static String getPersistentStatePath(String path) {
		return Scp_byo.MOD_ID + "_" +  path;
	}

	public static <T extends PersistentState> PersistentStateType<T> createPersistentStateType(String key, Function<PersistentState.Context, T> constructor, Function<PersistentState.Context, Codec<T>> codec){
		return new PersistentStateType<>(getPersistentStatePath(key), constructor, codec, null);
	}

	public static <T extends PersistentState> PersistentStateType<T> createPersistentStateType(String key, Supplier<T> constructor, Codec<T> codec){
		return new PersistentStateType<>(getPersistentStatePath(key), context -> constructor.get(), context -> codec, null);
	}

	public static IntStream Vec3IToStream(Vec3i vec) {
		return IntStream.of(vec.getX(), vec.getY(), vec.getZ());
	}

	public static int[] Vec3IToArray(Vec3i dimensions) {
        return new int[]{dimensions.getX(), dimensions.getY(), dimensions.getZ()};
	}

	public static void debugToPlayers(World world, Object text){
		for (PlayerEntity player : world.getPlayers()) {
			player.sendMessage(Text.of(text.toString()), false);
		}
	}
}

