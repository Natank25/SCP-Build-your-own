package io.github.natank25.scp_byo.entity.goals.scp_096;

import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.sounds.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class SCP096AttackTargetGoal extends Goal {
	
	private final Scp_096Entity scp096;
	
	private Path path = null;
	private double targetX = 0.0;
	private double targetY = 0.0;
	private double targetZ = 0.0;
	private int updateCountdownTicks = 0;
	private long lastUpdateTime = 0L;
	private boolean killed = false;
	
	public SCP096AttackTargetGoal(Scp_096Entity scp096) {
		this.scp096 = scp096;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
	}
	
	@Override
	public boolean canStart() {
		if (null == this.scp096.getTarget()) return false;
		
		long l = this.scp096.world.getTime();
		
		if (!this.scp096.isChasing()) return false;
		if (l - this.lastUpdateTime < 20L) return false;
		
		this.lastUpdateTime = l;
		LivingEntity livingEntity = this.scp096.getTarget();
		
		if (null == livingEntity) return false;
		if (!livingEntity.isAlive()) return false;
		
		this.path = this.scp096.getNavigation().findPathTo(livingEntity, 0);
		return null != this.path;
	}
	
	@Override
	public boolean shouldContinue() {
		LivingEntity livingEntity = this.scp096.getTarget();
		
		if (null == livingEntity) return false;
		if (!livingEntity.isAlive()) return false;
		if (!this.scp096.isChasing()) return false;
		
		return !this.scp096.getNavigation().isIdle();
		
	}
	
	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}
	
	@Override
	public void start() {
		this.scp096.getNavigation().startMovingAlong(this.path, this.scp096.scp096Speed);
		this.scp096.setSCP_Pose(Scp_096Entity.SCP096Pose.CHASING);
		this.scp096.setAttacking(true);
		this.updateCountdownTicks = 0;
	}
	
	@Override
	public void stop() {
		LivingEntity livingEntity = this.scp096.getTarget();
		if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
			this.scp096.setTarget(null);
		}
		
		this.scp096.setAttacking(false);
		this.scp096.getNavigation().stop();
	}
	
	@Override
	public void tick() {
		LivingEntity target = this.scp096.getTarget();
		if (null != target) {
			this.scp096.setSCP_Pose(this.scp096.getNavigation().isIdle() || this.scp096.getVelocity().length() < 0.1 ? Scp_096Entity.SCP096Pose.NOT_MOVING : Scp_096Entity.SCP096Pose.CHASING);
			this.scp096.getLookControl().lookAt(target, 30.0F, 30.0F);
			double d = this.scp096.getSquaredDistanceToAttackPosOf(target);
			this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
			//noinspection OverlyComplexBooleanExpression
			if (this.scp096.getVisibilityCache().canSee(target) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || target.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.scp096.getRandom().nextFloat() < 0.05F)) {
				this.targetX = target.getX();
				this.targetY = target.getY();
				this.targetZ = target.getZ();
				this.updateCountdownTicks = 4 + this.scp096.getRandom().nextInt(7);
				if (d > 1024.0) {
					this.updateCountdownTicks += 10;
				} else if (d > 256.0) {
					this.updateCountdownTicks += 5;
				}
				
				if (!this.scp096.getNavigation().startMovingTo(target, this.scp096.scp096Speed)) {
					this.updateCountdownTicks += 15;
				}
				
				this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
			}
			
			this.attack((PlayerEntity) target, d);
		}
	}
	
	private void attack(PlayerEntity target, double squaredDistance) {
		if (this.killed) return;
		double d = this.getSquaredMaxAttackDistance(target);
		if (squaredDistance <= d) {
			this.scp096.swingHand(Hand.MAIN_HAND);
			
			this.scp096.tryAttack(target);
			this.scp096.playSound(ModSounds.SCP096_KILL, 2, 1);
			
			this.killed = false;
			this.scp096.setIdling(true);
			this.scp096.setRaging(false);
			this.scp096.setChasing(false);
			this.scp096.setTarget(null);
			this.scp096.setSCP_Pose(Scp_096Entity.SCP096Pose.IDLING);
		}
		
	}
	
	private double getSquaredMaxAttackDistance(LivingEntity entity) {
		return this.scp096.getWidth() * 2.0F * this.scp096.getWidth() * 2.0F + entity.getWidth();
	}
}
