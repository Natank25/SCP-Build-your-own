package io.github.natank25.scp_byo.entity.goals.scp_096;

import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class SCP096BlockBreakingGoal extends Goal {
	
	private final Scp_096Entity scp096;
	private final boolean chase;
	private final List<BlockPos> targetBlocks = new ArrayList<>();
	private @Nullable LivingEntity target;
	private int tickToBreak = 0;
	private int breakingTick = 0;
	private @Nullable BlockState blockState = null;
	private int prevBreakProgress = 0;
	private @Nullable Vec3d lastPosition = null;
	private int lastPositionTickstamp = 0;
	
	private @Nullable Path path = null;
	
	public SCP096BlockBreakingGoal(Scp_096Entity scp_096, boolean chase) {
		this.scp096 = scp_096;
		this.chase = chase;
		this.setControls(EnumSet.of(Control.LOOK, Control.MOVE));
	}
	
	
	@Override
	public boolean canStart() {
		
		if (null == this.scp096.getTarget()) return false;
		if (!this.chase && !this.scp096.isIdling()) return false;
		if (this.chase && !this.scp096.isChasing()) return false;
		
		return this.isStuck() && (this.scp096.distanceTo(this.scp096.getTarget()) > 0.5d || !this.scp096.canSee(this.scp096.getTarget()));
	}
	
	
	@Override
	public boolean shouldContinue() {
		if (this.targetBlocks.isEmpty()) return false;
		if (null != this.blockState && !this.canBreakBlock()) return false;
		if (null == this.target || !this.target.isAlive()) return false;
		
		return this.targetBlocks.get(0).getSquaredDistance(this.scp096.getBlockPos()) < 16.0 && this.scp096.getNavigation().isIdle() && !this.scp096.getWorld().getBlockState(this.targetBlocks.get(0)).isAir() && null != this.path && (this.path.getManhattanDistanceFromTarget() > 1.5d || !this.scp096.canSee(this.target));
	}
	
	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}
	
	@Override
	public void start() {
		this.target = this.scp096.getTarget();
		if (null == this.target) return;
		
		this.fillTargetBlocks();
		
		if (this.targetBlocks.isEmpty()) return;
		
		this.initBlockBreak();
		this.scp096.setAttacking(true);
	}
	
	@Override
	public void stop() {
		this.target = null;
		if (!this.targetBlocks.isEmpty()) {
			this.scp096.getWorld().setBlockBreakingInfo(this.scp096.getId(), this.targetBlocks.get(0), -1);
			this.targetBlocks.clear();
		}
		this.tickToBreak = 0;
		this.breakingTick = 0;
		this.blockState = null;
		this.prevBreakProgress = 0;
		this.lastPosition = null;
		this.path = null;
		this.scp096.setAttacking(false);
	}
	
	@Override
	public void tick() {
		if (this.targetBlocks.isEmpty()) return;
		BlockPos pos = this.targetBlocks.get(0);
		if (null != this.blockState && !this.canBreakBlock()) return;
		this.breakingTick++;
		this.scp096.getLookControl().lookAt(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);
		if (this.prevBreakProgress != (int) ((this.breakingTick / (float) this.tickToBreak) * 10)) {
			this.prevBreakProgress = (int) ((this.breakingTick / (float) this.tickToBreak) * 10);
			this.scp096.getWorld().setBlockBreakingInfo(this.scp096.getId(), pos, this.prevBreakProgress);
		}
		if (this.breakingTick % 6 == 0) {
			this.scp096.swingHand(Hand.MAIN_HAND);
		}
		if (this.breakingTick % 4 == 0) {
			BlockSoundGroup soundType = this.blockState.getSoundGroup();
			this.scp096.getWorld().playSound(null, pos, soundType.getHitSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F);
		}
		if (this.breakingTick >= this.tickToBreak && this.scp096.getWorld() instanceof ServerWorld level) {
			BlockEntity blockentity = this.blockState.hasBlockEntity() ? this.scp096.getWorld().getBlockEntity(pos) : null;
			LootContext.Builder lootparamsBuilder = (new LootContext.Builder(level)).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, this.scp096.getOffHandStack()).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockentity).optionalParameter(LootContextParameters.THIS_ENTITY, this.scp096);
			this.blockState.onStacksDropped(level, pos, this.scp096.getOffHandStack(), true);
			this.blockState.getDroppedStacks(lootparamsBuilder).forEach((itemStack) -> level.spawnEntity(new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, itemStack)));
			this.scp096.getWorld().breakBlock(pos, false, this.scp096);
			this.scp096.getWorld().setBlockBreakingInfo(this.scp096.getId(), pos, -1);
			this.targetBlocks.remove(0);
			if (!this.targetBlocks.isEmpty()) this.initBlockBreak();
			else if (this.scp096.distanceTo(this.target) > 2.0d && !this.scp096.getVisibilityCache().canSee(this.target))
				this.start();
		}
	}
	
	private boolean canBreakBlock() {
		if (!this.chase && this.scp096.distanceTo(this.scp096.getTarget()) < 16.0) return false;
		if (!this.blockState.getFluidState().isOf(Fluids.EMPTY)) return false;
		return this.scp096.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
		
	}
	
	private int computeTickToBreak() {
		return MathHelper.ceil((1.0f / this.getDigSpeed() / 0.06666666666666667));
	}
	
	private void fillTargetBlocks() {
		int mobHeight = MathHelper.ceil(this.scp096.getHeight());
		for (int i = 0; i < mobHeight; i++) {
			BlockHitResult rayTraceResult = this.scp096.getWorld().raycast(new RaycastContext(this.scp096.getPos().add(0, i + 0.5d, 0), this.target.getEyePos().add(0, i, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.scp096));
			if (rayTraceResult.getType() == HitResult.Type.MISS || this.targetBlocks.contains(rayTraceResult.getBlockPos()))
				continue;
			
			double distance = this.scp096.squaredDistanceTo(rayTraceResult.getPos());
			if (distance > 16) continue;
			
			BlockState state = this.scp096.getWorld().getBlockState(rayTraceResult.getBlockPos());
			
			if (state.hasBlockEntity()) continue;
			
			
			this.targetBlocks.add(rayTraceResult.getBlockPos());
		}
		Collections.reverse(this.targetBlocks);
	}
	
	private float getDigSpeed() {
		float digSpeed = 10;
		int efficiencyLevel = EnchantmentHelper.getEfficiency(this.scp096);
		ItemStack itemstack = this.scp096.getOffHandStack();
		if (efficiencyLevel > 0 && !itemstack.isEmpty()) {
			digSpeed += (efficiencyLevel * efficiencyLevel + 1);
		}
		
		if (StatusEffectUtil.hasHaste(this.scp096)) {
			digSpeed *= 1.0F + (StatusEffectUtil.getHasteAmplifier(this.scp096) + 1) * 0.2F;
		}
		
		if (this.scp096.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
			float miningFatigueAmplifier = switch (this.scp096.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
				case 0 -> 0.3F;
				case 1 -> 0.09F;
				case 2 -> 0.0027F;
				default -> 8.1E-4F;
			};
			
			digSpeed *= miningFatigueAmplifier;
		}
		
		if (this.scp096.isSubmergedInWater() && !EnchantmentHelper.hasAquaAffinity(this.scp096)) {
			digSpeed /= 5.0F;
		}
		
		return digSpeed;
	}
	
	private void initBlockBreak() {
		this.blockState = this.scp096.getWorld().getBlockState(this.targetBlocks.get(0));
		this.tickToBreak = this.computeTickToBreak();
		this.breakingTick = 0;
		this.path = this.scp096.getNavigation().findPathTo(this.target, 1);
	}
	
	/**
	 * Returns true if the scp096 has been stuck in the same spot (radius 1.5 blocks) for more than 3 seconds
	 */
	private boolean isStuck() {
		if (null == this.scp096.getTarget()) return false;
		
		if (null == this.lastPosition || this.scp096.squaredDistanceTo(this.lastPosition) > 2.25d) {
			this.lastPosition = this.scp096.getPos();
			this.lastPositionTickstamp = this.scp096.age;
		}
		return this.scp096.getNavigation().isIdle() || this.scp096.age - this.lastPositionTickstamp >= 60;
	}
}
