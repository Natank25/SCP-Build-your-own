package io.github.natank25.scp_byo.block.custom;

import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

public enum BlockPlace implements StringIdentifiable {
    UPPER, LOWER, BETWEEN;

    public String toString() {
        return this.asString();
    }

    @Override
    public @Nullable String asString() {
		return switch (this) {
			case UPPER -> "upper";
			case LOWER -> "lower";
			case BETWEEN -> "between";
		};
	}

}
