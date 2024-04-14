package io.github.natank25.scp_byo;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public enum ModGamerules {
    ;

    public static final GameRules.Key<GameRules.BooleanRule> CAN_SCP096_SPAWN = registerBooleanRule("can096Spawn", GameRules.Category.MOBS, true);

    public static void registerModGamerules() {
        scp_byo.LOGGER.debug("Registering Mod Gamerules for " + scp_byo.MOD_ID);
    }

    private static GameRules.Key<GameRules.BooleanRule> registerBooleanRule(String id, GameRules.Category category, boolean defaultValue) {
        return GameRuleRegistry.register(id, category, GameRuleFactory.createBooleanRule(defaultValue));
    }
}
