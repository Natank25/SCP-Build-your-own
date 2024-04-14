package io.github.natank25.scp_byo.entity.goals.scp_096;

import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.pathing.Path;

import java.util.EnumSet;

public class SCP096MoveToTargetGoal extends MeleeAttackGoal {
	
	private static final boolean pauseWhenMobIdle = false;
	private final double speed;
	private final Scp_096Entity scp096;
	private Path path = null;
	private double targetX = 0.0;
	private double targetY = 0.0;
	private double targetZ = 0.0;
	private int updateCountdownTicks = 0;
	private long lastUpdateTime = 0L;
	
	public SCP096MoveToTargetGoal(Scp_096Entity scp096, double speed) {
		super(scp096, speed, false);
		this.scp096 = scp096;
		this.speed = speed;
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
	}
	
	
	@Override
	public boolean canStart() {
		long l = this.scp096.world.getTime();
		if (l - this.lastUpdateTime < 20L) return false;
		
		this.lastUpdateTime = l;
		if (null == this.scp096.getTarget()) return false;
		if (!this.scp096.isIdling()) return false;
		
		LivingEntity livingEntity = this.scp096.getTarget();
		if (null == livingEntity) return false;
		if (!livingEntity.isAlive()) return false;
		if (this.scp096.distanceTo(this.scp096.getTarget()) < 50) return false;
		
		this.path = this.scp096.getNavigation().findPathTo(livingEntity, 0);
		return null != this.path;
		
	}
	
	@Override
	public boolean shouldContinue() {
		LivingEntity livingEntity = this.scp096.getTarget();
		
		if (null == livingEntity) return false;
		if (!livingEntity.isAlive()) return false;
		if (!this.scp096.isIdling()) return false;
		
		return !this.scp096.getNavigation().isIdle();
	}
	
	@Override
	public void start() {
		this.scp096.getNavigation().startMovingAlong(this.path, this.speed);
		this.scp096.setAttacking(true);
		this.scp096.setSCP_Pose(Scp_096Entity.SCP096Pose.IDLING);
		this.updateCountdownTicks = 0;
	}
	
	@Override
	public void tick() {
		LivingEntity target = this.scp096.getTarget();
		if (null != target) {
			this.scp096.getLookControl().lookAt(target, 30.0F, 30.0F);
			double d = this.scp096.getSquaredDistanceToAttackPosOf(target);
			this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
			if (this.scp096.getVisibilityCache().canSee(target) && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || target.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 50.0 || this.scp096.getRandom().nextFloat() < 0.05F)) {
				this.targetX = target.getX();
				this.targetY = target.getY();
				this.targetZ = target.getZ();
				this.updateCountdownTicks = 4 + this.scp096.getRandom().nextInt(7);
				if (d > 1024.0) {
					this.updateCountdownTicks += 10;
				} else if (d > 256.0) {
					this.updateCountdownTicks += 5;
				}
				
				if (!this.scp096.getNavigation().startMovingTo(target, this.speed)) {
					this.updateCountdownTicks += 15;
				}
				
				this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
			}
			
		}
	}
	
	
}
