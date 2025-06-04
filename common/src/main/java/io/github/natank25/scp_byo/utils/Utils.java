package io.github.natank25.scp_byo.utils;

import io.github.natank25.scp_byo.Scp_byo;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;

import java.util.stream.IntStream;

public enum Utils {
	;
	
	public static Identifier newIdentifier(String path) {
		return Identifier.of(Scp_byo.MOD_ID, path);
	}


	public static IntStream Vec3IToStream(Vec3i vec) {
		return IntStream.of(vec.getX(), vec.getY(), vec.getZ());
	}

	public static int[] Vec3IToArray(Vec3i dimensions) {
        return new int[]{dimensions.getX(), dimensions.getY(), dimensions.getZ()};
	}
}

