package io.github.natank25.scp_byo.entity.custom;

import io.github.natank25.scp_byo.ModGamerules;
import io.github.natank25.scp_byo.ModGlobalEvents;
import io.github.natank25.scp_byo.entity.goals.scp_096.SCP096AttackTargetGoal;
import io.github.natank25.scp_byo.entity.goals.scp_096.SCP096BlockBreakingGoal;
import io.github.natank25.scp_byo.entity.goals.scp_096.SCP096MoveToTargetGoal;
import io.github.natank25.scp_byo.entity.goals.scp_096.SCP096StayLockedGoal;
import io.github.natank25.scp_byo.mutliblock.ModMultiblocks.SCP096Cage;
import io.github.natank25.scp_byo.persistent_data.DoesSCP096Exist;
import io.github.natank25.scp_byo.persistent_data.cca.register.ModWorldComponents;
import io.github.natank25.scp_byo.sounds.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;
import java.util.function.IntFunction;

//TODO: Add SCRAMBLE project (https://scp-wiki.wikidot.com/incident-096-1-a - log 096-1-B)
//(Too OP with just paper) Add paper bag feature (maybe with just a paper item and player needs to be closed) (https://scp-wiki.wikidot.com/scp-096 - Retrieval Incident #096-1-A)

//chase ? not moving animation : sitting animation (WIP) (need to re add SCP096Pose.SITTING)
public class Scp_096Entity extends ScpEntity implements GeoEntity {
	private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
	private static final TrackedData<Integer> SCP_POSE;
	private static final EntityAttributeModifier ATTACKING_SPEED_BOOST;
	private static final float STEP_HEIGHT = 4.0F;
	private static Scp_096Entity currentScp = null;
	
	static {
		SCP_POSE = DataTracker.registerData(Scp_096Entity.class, TrackedDataHandlerRegistry.INTEGER);
		SCPHealth = DataTracker.registerData(Scp_096Entity.class, TrackedDataHandlerRegistry.FLOAT);
		ATTACKING_SPEED_BOOST = new EntityAttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.20000000596046448, EntityAttributeModifier.Operation.ADDITION);
	}
	
	
	private final AnimatableInstanceCache animatableInstanceCache = new SingletonAnimatableInstanceCache(this);
	private final Collection<SoundEvent> playingSounds = new HashSet<>();
	public double scp096Speed = 0.0;
	private boolean idling = true;
	private boolean raging = false;
	private boolean chasing = false;
	private long startChaseTick = 0L;
	private boolean hasPlayerSeen = false;
	private static final TrackedData<Float> SCPHealth;
	private boolean isInCage = false;
	
	public Scp_096Entity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		this.stepHeight = STEP_HEIGHT;
		this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
		this.setPathfindingPenalty(PathNodeType.LAVA, 0.0F);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
		this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
		this.setPathfindingPenalty(PathNodeType.RAIL, 0.0F);
		this.setPersistent();
		currentScp = this;
		
		
	}
	
	
	public static boolean isValidNaturalSpawn(EntityType<? extends Scp_096Entity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		if (!world.getServer().getGameRules().getBoolean(ModGamerules.CAN_SCP096_SPAWN)) return false;
		if (((World) world).getTime() < 20*60*20) return false;
		DoesSCP096Exist doesSCP096Exist = DoesSCP096Exist.getServerState(Objects.requireNonNull(world.getServer()));
		
		if (doesSCP096Exist.doesSCP096Exists) return false;
		
		BlockState blockState = world.getBlockState(pos.down());
		if (!blockState.isIn(BlockTags.ANIMALS_SPAWNABLE_ON)) return false;
		
		doesSCP096Exist.doesSCP096Exists = true;
		return true;
	}
	
	public static DefaultAttributeContainer.Builder setAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, Float.POSITIVE_INFINITY).add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896);
	}
	
	@Override
	public boolean canWalkOnFluid(FluidState state) {
		return !state.isOf(Fluids.EMPTY);
	}
	
	@Override
	public boolean cannotDespawn() {
		return true;
	}
	
	
	@Override
	public EntityNavigation createNavigation(World world) {
		return new Scp_096Entity.Navigation(this, world);
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.animatableInstanceCache;
	}
	
	@Override
	public int getMinAmbientSoundDelay() {
		return 700;
	} // should be 35 secs
	
	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}
	
	public boolean isChasing() {
		return this.chasing;
	}
	
	public void setChasing(boolean chasing) {
		this.chasing = chasing;
	}
	
	public boolean isHasPlayerSeen() {
		return this.hasPlayerSeen;
	}
	
	public void setHasPlayerSeen(boolean hasPlayerSeen) {
		this.hasPlayerSeen = hasPlayerSeen;
	}
	
	public boolean isIdling() {
		return this.idling;
	}
	
	public void setIdling(boolean idling) {
		this.idling = idling;
	}
	
	private Optional<SCP096Cage> getCage(){
		Optional optional = this.getWorld().getComponent(ModWorldComponents.MULTIBLOCKS).getMultiblock(this.getBlockPos());
		if (optional.isEmpty()) return Optional.empty();
		
		if (!(optional.get() instanceof SCP096Cage cage)) return Optional.of((SCP096Cage) optional.get());
		
		return Optional.empty();
	}
	
	private boolean isTrulyInCage() {
		Optional optional = this.getWorld().getComponent(ModWorldComponents.MULTIBLOCKS).getMultiblock(this.getBlockPos());
		if (optional.isEmpty()) return false;
		
		if (!(optional.get() instanceof SCP096Cage cage)) return false;
		
		Optional<Scp_096Entity> scp = cage.getScp();
		return scp.isPresent() && scp.get().equals(this);
		
	}
	
	public boolean isInCage(){
		return this.isInCage;
	}
	
	@Override
	public boolean isOnFire() {
		return false;
	}
	
	public boolean isRaging() {
		return this.raging;
	}
	
	public void setRaging(boolean raging) {
		this.raging = raging;
	}
	
	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		super.onTrackedDataSet(data);
	}
	
	@Override
	public void playSound(SoundEvent sound, float volume, float pitch) {
		
		if (this.world.isClient || Objects.equals(sound.getId().getNamespace(), "minecraft")) return;
		
		if (!this.playingSounds.isEmpty()) this.stopAllSounds(this.getWorld());
		
		this.playingSounds.add(sound);
		
		Packet<ClientPlayPacketListener> packet = new PlaySoundFromEntityS2CPacket(Registries.SOUND_EVENT.getEntry(sound), SoundCategory.HOSTILE, this, volume, pitch, this.random.nextLong());
		this.getWorld().getServer().getPlayerManager().sendToAll(packet);
		
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setSCP_Pose(SCP096Pose.byId(nbt.getInt("SCP_Pose")));
		this.setSCPHealth(nbt.getInt("SCP_health"));
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		controllerRegistrar.add(new AnimationController<>(this, "controller", 2, this::predicate));
	}
	
	@Override
	public void remove(RemovalReason reason) {
		this.stopAllSounds(this.getWorld());
		if (!this.world.isClient()) {
			DoesSCP096Exist.getServerState(Objects.requireNonNull(this.getServer())).doesSCP096Exists = false;
			currentScp = null;
		}
		
		
		super.remove(reason);
	}
	
	
	
	@Override
	public boolean shouldRender(double distance) {
		return true;
	}
	
	@Override
	public void tick() {
		
		if (!this.world.isClient) {
			if (null == this.getTarget() && !this.getWorld().getPlayers().isEmpty()) this.setTarget();
			
			if(this.age % 20 == 0) {
				this.isInCage = this.isTrulyInCage();
			}
			
			this.setPoseWhenIdle();
			
			this.SetChaseIfPlayerSeeing();
			
			this.StartChaseAfterRage();
			
			this.loadChunks();
			this.regenerate();
			this.updateSpeed();
			
			this.checkBlockCollision();
		}
		
		super.tick();
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("SCP_Pose", this.getSCP_Pose().getId());
		nbt.putFloat("SCP_health", this.getSCPHealth());
	}
	
	@Override
	protected void applyDamage(DamageSource source, float amount) {
		
		if (source.isSourceCreativePlayer() || source.isOutOfWorld()) {
			super.applyDamage(source, amount);
		} else {
			float f = amount;
			amount = Math.max(amount, 0.0F);
			float g = f - amount;
			if (g > 0.0F && g < 3.4028235E37F && source.getAttacker() instanceof ServerPlayerEntity) {
				((PlayerEntity) source.getAttacker()).increaseStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(g * 10.0F));
			}
			
			
			this.getDamageTracker().onDamage(source, this.getHealth(), 0);
			this.emitGameEvent(GameEvent.ENTITY_DAMAGE);
			
			
			float newHealth = Math.max(this.getSCPHealth() - amount, 0);
			this.setSCPHealth(newHealth);
		}
	}
	
	@Override
	protected boolean canStartRiding(Entity entity) {
		return false;
	}
	
	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return this.idling ? ModSounds.SCP096_IDLE : null;
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(SCP_POSE, 0);
		this.dataTracker.startTracking(SCPHealth, 100.0f);
	}
	
	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SCP096StayLockedGoal(this));
		this.goalSelector.add(1, new SCP096AttackTargetGoal(this));
		this.goalSelector.add(1, new SCP096BlockBreakingGoal(this, true));
		this.goalSelector.add(2, new SCP096MoveToTargetGoal(this, 0.37));
		this.goalSelector.add(2, new SCP096BlockBreakingGoal(this, false));
		
	}
	
	private void SetChaseIfPlayerSeeing() {
		if (this.idling) {
			this.setPitch(46.0f);
			boolean result = false;
			for (PlayerEntity p : this.getWorld().getPlayers()) {
				if (!p.canTakeDamage()) {
					break;
				}
				result = this.isPlayerSeeing(p);
				
				if (result) {
					this.setSCP_Pose(SCP096Pose.RAGING);
					this.raging = true;
					this.startChaseTick = this.world.getTime() + 500;
					this.idling = false;
					this.setTarget(p);
					break;
				}
			}
			
			if (result) {
				this.getWorld().syncGlobalEvent(ModGlobalEvents.SCP096_TRIGGER, this.getBlockPos(), 0);
				this.playSound(ModSounds.SCP096_RAGE, 5, 1);
			}
		}
	}
	
	private void StartChaseAfterRage() {
		if (this.world.getTime() == this.startChaseTick) {
			if (!this.world.isClient()) {
				this.playSound(this.random.nextBoolean() ? ModSounds.SCP096_RAGE_CHASE : ModSounds.SCP096_CHASE, 5, 1);
			}
			this.raging = false;
			this.chasing = true;
			this.setSCP_Pose(SCP096Pose.CHASING);
		}
	}
	
	private SCP096Pose getSCP_Pose() {
		return SCP096Pose.byId(this.dataTracker.get(SCP_POSE));
	}
	
	public void setSCP_Pose(SCP096Pose pose) {
		this.dataTracker.set(SCP_POSE, pose.getId());
	}
	
	private void setSCPHealth(float newHealth){
		this.dataTracker.set(SCPHealth, newHealth);
	}
	
	private Float getSCPHealth(){
		return this.dataTracker.get(SCPHealth);
	}
	
	private boolean isPlayerSeeing(PlayerEntity player) {
		
		Vec3d vec3d = player.getRotationVec(1.0F).normalize(); // Rotation fo the player between 0-1
		Vec3d vec3d2 = new Vec3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());  // Distance between player and 096 in blocks of each axis
		double d = vec3d2.length(); // Distance between player and 096
		vec3d2 = vec3d2.normalize();  // Distance between player and 096 in blocks of each axis between 0-1
		double e = vec3d.dotProduct(vec3d2); // The closer to 1 this var is, the more the player is looking at 096
		boolean b1 = e > 0.35 && player.canSee(this); // 1.0 - 0.025 / d_scp is modified for precision
		
		
		Vec3d vec3DScp = this.getRotationVec(1.0F).normalize();
		Vec3d vec3D2Scp = new Vec3d(player.getX() - this.getX(), player.getEyeY() - this.getEyeY(), player.getZ() - this.getZ());
		double dScp = vec3D2Scp.length();
		vec3D2Scp = vec3D2Scp.normalize();
		double eScp = vec3DScp.dotProduct(vec3D2Scp);
		boolean b2 = eScp > 0.05 && this.canSee(player);
		
		return b1 && b2;
	}
	
	private boolean isWalking() {
		return (this.getVelocity().x > -0.01 && this.getVelocity().x < 0.01) && (this.getVelocity().z > -0.01 && this.getVelocity().z < 0.01) && this.idling;
	}
	
	private void loadChunks() {
		if (this.world.isClient()) return;
		
		ChunkPos pos = this.getChunkPos();
		ServerWorld server = ((ServerWorld) this.getWorld());
		Collection<ChunkPos> loadedChunks = new ArrayList<>();
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				loadedChunks.add(new ChunkPos(pos.x + x, pos.z + z));
			}
		}
		
		Collection<ChunkPos> unloadedChunks = new ArrayList<>();
		for (int x = -2; x < 3; x++) {
			for (int z = -2; z < 3; z++) {
				if (loadedChunks.contains(new ChunkPos(pos.x + x, pos.z + z))) continue;
				unloadedChunks.add(new ChunkPos(pos.x + x, pos.z + z));
			}
		}
		
		for (ChunkPos currentChunk : loadedChunks) {
			this.getWorld().getChunkManager().setChunkForced(currentChunk, true);
		}
		
		for (ChunkPos currentChunk : unloadedChunks) {
			this.getWorld().getChunkManager().setChunkForced(currentChunk, false);
		}
	}
	
	private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
		String animName = switch (this.getSCP_Pose()) {
			case IDLING -> "idling";
			case RAGING -> "rage";
			case GETTING_UP -> "getting_up";
			case NOT_MOVING -> "not_moving";
			case CHASING -> "chase";
		};
		
		Animation.LoopType loopType = switch (this.getSCP_Pose()) {
			case IDLING, CHASING, RAGING, NOT_MOVING -> Animation.LoopType.LOOP;
			case GETTING_UP -> Animation.LoopType.HOLD_ON_LAST_FRAME;
		};
		
		
		AnimationController<T> controller = tAnimationState.getController();
		controller.setAnimation(RawAnimation.begin().then("animation.scp_096." + animName, loopType));
		return PlayState.CONTINUE;
	}
	
	private void regenerate() {
		if (this.getSCPHealth() < this.getMaxHealth() && this.age % (Math.round(this.getSCPHealth()) + (Math.round(this.getSCPHealth()) == 0 ? 1 : 0)) == 0) {
			this.setSCPHealth(this.getSCPHealth()+1);
		}
	}
	
	private void setPoseWhenIdle() {
		if (this.isWalking()) {
			this.setSCP_Pose(SCP096Pose.NOT_MOVING);
		} else {
			if (this.idling) {
				this.setSCP_Pose(SCP096Pose.IDLING);
			}
		}
	}
	
	private void setTarget() {
		Iterator<? extends PlayerEntity> possibleTargets = this.getWorld().getPlayers().stream().iterator();
		
		while (possibleTargets.hasNext()) {
			PlayerEntity possibleTarget = possibleTargets.next();
			if (possibleTarget.isAlive() && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(possibleTarget)) {
				this.setTarget(possibleTarget);
				this.idling = true;
				break;
			}
		}
		
		EntityAttributeInstance entityAttribute = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
		if (null == this.getTarget()) {
			entityAttribute.removeModifier(ATTACKING_SPEED_BOOST);
		} else {
			if (!entityAttribute.hasModifier(ATTACKING_SPEED_BOOST)) {
				entityAttribute.addTemporaryModifier(ATTACKING_SPEED_BOOST);
			}
		}
	}
	
	private void stopAllSounds(WorldAccess world) {
		
		
		this.playingSounds.clear();
	}
	
	private void updateSpeed() {
		this.scp096Speed = (this.getSCPHealth() / 100);
	}
	
	private static class Navigation extends MobNavigation {
		Navigation(Scp_096Entity entity, World world) {
			super(entity, world);
		}
		
		@Override
		public boolean canWalkOnPath(PathNodeType pathType) {
			return pathType == PathNodeType.RAIL || pathType == PathNodeType.LAVA || pathType == PathNodeType.WATER || pathType == PathNodeType.DAMAGE_FIRE || pathType == PathNodeType.DANGER_FIRE || super.canWalkOnPath(pathType);
		}
		
		@Override
		public PathNodeNavigator createPathNodeNavigator(int range) {
			this.nodeMaker = new LandPathNodeMaker();
			this.nodeMaker.setCanEnterOpenDoors(false);
			this.nodeMaker.setCanOpenDoors(true);
			this.nodeMaker.setCanWalkOverFences(true);
			this.nodeMaker.setCanSwim(true);
			this.setAvoidSunlight(false);
			return new PathNodeNavigator(this.nodeMaker, range);
		}
		
		@Override
		public boolean isValidPosition(BlockPos pos) {
			return !this.world.getFluidState(pos).isOf(Fluids.EMPTY) || super.isValidPosition(pos);
		}
	}
	
	public enum SCP096Pose {
		NOT_MOVING(4), CHASING(3), GETTING_UP(1), RAGING(2), IDLING(0);
	
		private static final IntFunction<SCP096Pose> BY_ID = ValueLists.createIdToValueFunction(SCP096Pose::getId, values(), ValueLists.OutOfBoundsHandling.ZERO);
		private final int id;
	
		SCP096Pose(int id) {
			this.id = id;
		}
	
		static SCP096Pose byId(int id) {
			return BY_ID.apply(id);
		}
	
		int getId() {
			return this.id;
		}
	}
}
