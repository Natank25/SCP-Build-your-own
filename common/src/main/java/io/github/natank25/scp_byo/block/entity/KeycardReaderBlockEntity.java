package io.github.natank25.scp_byo.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
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
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.hasOwner = nbt.getBoolean("hasOwner");
		
		if (this.hasOwner) this.owerUUID = nbt.getUuid("ownerUUID");
	}
	
	public void setOwnerUUID(UUID uuid) {
		if (this.hasOwner) return;
		this.owerUUID = uuid;
		this.hasOwner = true;
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return this.createNbt();
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putBoolean("hasOwner", this.hasOwner);
		
		if (this.hasOwner) nbt.putUuid("ownerUUID", this.owerUUID);
	}
}
