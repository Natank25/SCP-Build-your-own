package io.github.natank25.scp_byo.persistent_data.multiblock;

import dev.architectury.networking.NetworkManager;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.utils.ModConstants;
import io.netty.buffer.Unpooled;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Multiblocks extends PersistentState {
	
	private final List<Multiblock> ingame_multiblocks = new ArrayList<>();
	private final List<Multiblock> toRemoveMultiblocks = new ArrayList<>(); // Will maybe cause a bug if server stop on remove
	private final World world;
	
	public Multiblocks(World world) {
		this.world = world;
		
	}
	
	
	private static boolean isContaining(BlockBox box, double x, double y, double z) {
		return x >= box.getMinX() && x <= box.getMaxX() && z >= box.getMinZ() && z <= box.getMaxZ() && y >= box.getMinY() && y <= box.getMaxY();
	}
	
	private static boolean isContaining(BlockBox box, Vec3d pos) {
		return isContaining(box, pos.x, pos.y, pos.z);
	}
	
	public static Multiblocks createFromNbt(NbtCompound nbt, World world) {
		Multiblocks multiblocks = new Multiblocks(world);
		NbtCompound multiblocksNbt = nbt.getCompound("Multiblocks");
		int multiblocksLength = multiblocksNbt.getSize();
		
		for (int i = 1; i <= multiblocksLength; i++) {
			NbtCompound multiblockNbt = multiblocksNbt.getCompound("Multiblock" + i);
			int[] frontTopLeftArr = multiblockNbt.getIntArray("GlobalPos");
			BlockPos globalPos = new BlockPos(frontTopLeftArr[0], frontTopLeftArr[1], frontTopLeftArr[2]);
			
			for (int x = 0; x <= 1; x++) {
				for (int y = 0; y <= 1; y++) {
					BlockPos pos = globalPos.add(16 * x, 0, 16 * y);
					Scp_byo.LOGGER.warn(world.isChunkLoaded(pos.getX(), pos.getZ()));
					world.updateNeighbors(pos, world.getBlockState(pos).getBlock()); // Update the neighboring chunks so the scanning can work
					
				}
			}
			
			//TODO remove this
			// Pattern, result, world
			//Multiblock newMultiblock = new Multiblock(, , world);
			multiblocks.tryAssemble(globalPos, Direction.SOUTH, Direction.DOWN).ifPresent(multiblock -> multiblock.readFromNbt(multiblockNbt.getCompound("Nbt")));
		}
		return multiblocks;
	}
	
	/*
	transfer packet S2C when:
		player joins					X
		new multiblock 					Y
		removed multiblock 				Y
		updated multiblock (e.g. tick)  ?
	
	transfer packet C2S when:
		#should only receive
	
	 */
	
	public <T extends Multiblock> void add(T multiblock) {
		this.ingame_multiblocks.add(multiblock);
		
		
		if (!this.getWorld().isClient()) {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			
			buf.writeRegistryKey(this.world.getRegistryKey());
			buf.writeByte(0b01);
			buf.writeBlockPos(multiblock.globalBottomLeftPos);
			
			this.syncWithAllClients(buf);
		}
	}
	
	public <M extends Multiblock> void remove(M multiblock) {
		this.toRemoveMultiblocks.add(multiblock);
		
		
		if (!this.getWorld().isClient()) {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			
			buf.writeRegistryKey(this.world.getRegistryKey());
			buf.writeByte(0b10);
			buf.writeBlockPos(multiblock.globalBottomLeftPos);
			
			this.syncWithAllClients(buf);
		}
	}
	
	
	//region Sync
	public void sync() {
		//TODO when login (all data transferred) if (!this.world.isClient()) syncWithAllClients(new PacketByteBuf(Unpooled.EMPTY_BUFFER));
	}
	
	public void syncWithAllClients(PacketByteBuf buf) {
		if (this.world.isClient) {
			Scp_byo.LOGGER.error("world cannot be client");
			return;
		}
		//noinspection DataFlowIssue
		this.syncWithClients(this.world.getServer().getPlayerManager().getPlayerList(), buf);
	}
	
	public void syncWithClient(ServerPlayerEntity player, PacketByteBuf buf) {
		if (this.world.isClient) {
			Scp_byo.LOGGER.error("world cannot be client");
			return;
		}
		NetworkManager.sendToPlayer(player, ModConstants.Networking.MULTIBLOCK_UPDATE_PACKET_ID, buf);
	}
	
	public void syncWithClients(Iterable<ServerPlayerEntity> players, PacketByteBuf buf) {
		if (this.world.isClient) {
			Scp_byo.LOGGER.error("world cannot be client");
			return;
		}
		NetworkManager.sendToPlayers(players, ModConstants.Networking.MULTIBLOCK_UPDATE_PACKET_ID, buf);
	}
	//endregion
	
	
	public List<Multiblock> getList() {
		return this.ingame_multiblocks;
	}
	
	public Optional<? extends Multiblock> getMultiblock(BlockPos pos) {
		return this.getMultiblock(pos.toCenterPos());
	}
	
	public Optional<? extends Multiblock> getMultiblock(Vec3d pos) {
		for (Multiblock multiblock : this.ingame_multiblocks) {
			if (isContaining(multiblock.getBox(), pos)) {
				return Optional.of(multiblock);
			}
		}
		return Optional.empty();
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public void tick() {
		for (Multiblock multiblock : this.toRemoveMultiblocks) {
			multiblock.destroy();
			this.ingame_multiblocks.remove(multiblock);
			
		}
		
		if (!this.toRemoveMultiblocks.isEmpty()) this.toRemoveMultiblocks.clear();
		this.ingame_multiblocks.forEach(Multiblock::tick);
	}
	
	
	public Optional<Multiblock> tryAssemble(BlockPos pos) {
		if (this.getMultiblock(pos).isPresent()) return Optional.empty();
		
		for (BlockPattern pattern : BlockPatternsRegistry.getAll()) {
			BlockPattern.Result result = pattern.searchAround(this.world, pos);
			if (null != result) {
				Optional<Multiblock> multiblock = this.assembleMultiblock(pattern, result);
				multiblock.ifPresent(Multiblock::create);
				return multiblock;
			}
		}
		return Optional.empty();
	}
	
	public Optional<Multiblock> tryAssemble(BlockPos pos, Direction forward, Direction up) {
		if (this.getMultiblock(pos).isPresent()) return Optional.empty();
		
		for (BlockPattern pattern : BlockPatternsRegistry.getAll()) {
			BlockPattern.Result result = pattern.testTransform(this.world, pos, forward, up);
			if (null != result) {
				return this.assembleMultiblock(pattern, result);
			}
		}
		return Optional.empty();
	}
	
	public void tryDisassemble(BlockPos pos) {
		
		var multiblock = this.getMultiblock(pos);
		multiblock.ifPresent(this::remove);
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtCompound multiblocksNbt = new NbtCompound();
		int multiblockNumber = 0;
		for (Multiblock multiblock : this.ingame_multiblocks) {
			multiblockNumber++;
			NbtCompound multiblockNbt = new NbtCompound();
			multiblockNbt.putIntArray("GlobalPos", multiblock.getGlobalBottomLeftPosAsArray());
			
			multiblockNbt.put("Nbt", multiblock.getNbt());
			multiblocksNbt.put("Multiblock" + multiblockNumber, multiblockNbt);
			
		}
		
		nbt.put("Multiblocks", multiblocksNbt);
		return nbt;
	}
	
	
	private Optional<Multiblock> assembleMultiblock(BlockPattern pattern, BlockPattern.Result result) {
		Optional<Class<? extends Multiblock>> mblockOptionalClass = BlockPatternsRegistry.getMultiblockForBlockPattern(pattern);
		Class<? extends Multiblock> mblockClass = mblockOptionalClass.orElseThrow(() -> new RuntimeException("Class for block pattern " + pattern + " not found"));
		try {
			
			Constructor<? extends Multiblock> mblockConstructor = mblockClass.getDeclaredConstructor(BlockPattern.class, BlockPattern.Result.class, World.class);
			Multiblock newMultiblock = mblockConstructor.newInstance(pattern, result, this.world);
			mblockClass.cast(newMultiblock);
			this.add(newMultiblock);
			return Optional.of(newMultiblock);
			
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
				 InvocationTargetException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
