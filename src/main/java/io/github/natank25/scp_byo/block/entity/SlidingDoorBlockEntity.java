package io.github.natank25.scp_byo.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SlidingDoorBlockEntity extends BlockEntity implements GeoBlockEntity {
	private static final RawAnimation OPEN = RawAnimation.begin().thenPlayAndHold("open");
	private static final RawAnimation CLOSE = RawAnimation.begin().thenPlayAndHold("close");
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private boolean open;
	
	public boolean isOpen() {
		return this.open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public SlidingDoorBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY, pos, state);
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		controllerRegistrar.add(new AnimationController<>(this,"controller",this::animationPredicate)); //TODO: fix this from crashing the game when breaking the door
	}
	
	private PlayState animationPredicate(AnimationState<SlidingDoorBlockEntity> state) {
		return state.setAndContinue(this.open ? OPEN : CLOSE);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.open = nbt.getBoolean("open");
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		nbt.putBoolean("open", this.open);
		super.writeNbt(nbt);
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return this.createNbt();
	}
}
