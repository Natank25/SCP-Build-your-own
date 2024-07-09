package io.github.natank25.scp_byo.utils;

import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.entity.custom.ScpEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.io.File;

public enum Utils {
	;
	
	public static Identifier newIdentifier(String path){
		return new Identifier(Scp_byo.MOD_ID, path);
	}
	
	
	public static boolean isScp(Entity entity){
		return entity.getClass().isAssignableFrom(ScpEntity.class);
	}
	
	public static File toModFile(File file){
		
		String file_name = file.getName();
		String file_path = file.getParent();
		
		String folder = "scp_byo";
		
		return new File(file_path + File.separator + folder + File.separator + file_name);
	}
}
