package io.github.natank25.scp_byo.entity.goals.scp_096;

import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SCP096StayLockedGoal extends Goal {
	private final Scp_096Entity scp096;
	
	public SCP096StayLockedGoal(Scp_096Entity scp096) {
		this.scp096 = scp096;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
	}
	
	@Override
	public boolean canStart() {
		return !this.scp096.isRaging() && !this.scp096.isChasing() && this.scp096.isInCage();
	}
}
