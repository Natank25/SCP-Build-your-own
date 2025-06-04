package io.github.natank25.scp_byo.advancements;

import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.advancement.criterion.Criteria;

public class ModCriteria {
	public static final TrapScpCriterion TRAP_SCP = Criteria.register(Utils.newIdentifier("trap_scp").toString(), new TrapScpCriterion());
	
	public static void registerModCriteria() {
	}
}
