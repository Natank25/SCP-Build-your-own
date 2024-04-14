package io.github.natank25.scp_byo.utils;

import io.github.natank25.scp_byo.entity.custom.ScpEntity;
import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public enum Utils {
	;
	
	public static Identifier newIdentifier(String path){
		return new Identifier(scp_byo.MOD_ID, path);
	}
	
	
	public static boolean isScp(Entity entity){
		return entity.getClass().isAssignableFrom(ScpEntity.class);
	}
}
