package io.github.natank25.scp_byo.utils;

import io.github.natank25.scp_byo.persistent_data.ScpByoDataManager;
import org.jetbrains.annotations.NotNull;

public interface ScpByoDataStorage {
	@NotNull
	ScpByoDataManager scp_byoGetDataManager();
}
