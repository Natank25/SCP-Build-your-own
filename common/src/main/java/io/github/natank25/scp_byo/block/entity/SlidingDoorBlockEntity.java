package io.github.natank25.scp_byo.block.entity;

import io.github.natank25.scp_byo.block.custom.SlidingDoor;
import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SlidingDoorBlockEntity extends BlockEntity implements GeoBlockEntity {
	private static final RawAnimation OPEN = RawAnimation.begin().thenPlayAndHold("open");
	private static final RawAnimation CLOSE = RawAnimation.begin().thenPlayAndHold("close");
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private boolean open;
	
	public SlidingDoorBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlocksEntities.SLIDING_DOOR_BLOCK_ENTITY.get(), pos, state);
		open = state.get(SlidingDoor.OPEN);
	}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this::animationPredicate));
    }

    @Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		super.readNbt(nbt, registries);
		this.open = nbt.getBoolean("open", false);
	}

	
	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
		return this.createNbt(registries);
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		nbt.putBoolean("open", this.open);
		super.writeNbt(nbt, registries);
	}
	
	protected  <E extends GeoAnimatable> PlayState animationPredicate(final AnimationTest<E> state) {
		return state.setAndContinue(this.open ? OPEN : CLOSE);
	}
}
