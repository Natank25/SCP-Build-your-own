package io.github.natank25.scp_byo.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class KeycardReaderBlockEntity extends BlockEntity {
	
	private UUID owerUUID;
	private boolean hasOwner = false;

	public KeycardReaderBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlocksEntities.KEYCARD_READER_BLOCK_ENTITY.get(), pos, state);
	}
	
	public UUID getOwner() {
		return this.owerUUID;
	}
	
	public boolean hasOwner() {
		return this.hasOwner;
	}
	
	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.hasOwner = nbt.getBoolean("hasOwner", false);

		if (this.hasOwner) this.owerUUID = UUID.fromString(nbt.getString("ownerUUID", ""));
	}
	
	public void setOwnerUUID(UUID uuid) {
		if (this.hasOwner) return;
		this.owerUUID = uuid;
		this.hasOwner = true;
	}


	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		return this.createNbt(registryLookup);
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.putBoolean("hasOwner", this.hasOwner);
		if (this.hasOwner) nbt.putString("ownerUUID", this.owerUUID.toString());
	}
}
