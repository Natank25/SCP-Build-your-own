package io.github.natank25.scp_byo.utils;

import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.entity.custom.ScpEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.io.File;

public enum Utils {
	;
	
	public static Identifier newIdentifier(String path) {
		return new Identifier(Scp_byo.MOD_ID, path);
	}
	
}

