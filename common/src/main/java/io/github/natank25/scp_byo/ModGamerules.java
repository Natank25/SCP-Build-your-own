package io.github.natank25.scp_byo;

import net.minecraft.world.GameRules;

public enum ModGamerules {
	;
	
	public static final GameRules.Key<GameRules.BooleanRule> CAN_SCP096_SPAWN = registerBooleanRule("can096Spawn", GameRules.Category.MOBS, true);
	
	public static void registerModGamerules() {
	}
	
	private static GameRules.Key<GameRules.BooleanRule> registerBooleanRule(String id, GameRules.Category category, boolean defaultValue) {
		return GameRules.register(id, category, GameRules.BooleanRule.create(defaultValue));
	}
}
