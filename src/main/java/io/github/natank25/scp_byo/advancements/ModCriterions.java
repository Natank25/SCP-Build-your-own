package io.github.natank25.scp_byo.advancements;

import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.advancement.criterion.Criteria;

public class ModCriterions {
	public static final TrapScpCriterion TRAP_SCP = Criteria.register(new TrapScpCriterion());
	
	public static void registerModCriterions(){
		scp_byo.LOGGER.debug("Registering ModAdvancements for " + scp_byo.MOD_ID);
	}
}
