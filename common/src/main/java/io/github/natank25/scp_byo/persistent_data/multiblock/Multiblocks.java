package io.github.natank25.scp_byo.persistent_data.multiblock;

import dev.architectury.networking.NetworkManager;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.utils.ModConstants;
import io.netty.buffer.Unpooled;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
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
	
	//region Static isContaining
	
	private static boolean isContaining(BlockBox box, double x, double y, double z) {
		return x >= box.getMinX() && x <= box.getMaxX() && z >= box.getMinZ() && z <= box.getMaxZ() && y >= box.getMinY() && y <= box.getMaxY();
	}
	
	private static boolean isContaining(BlockBox box, Vec3d pos) {
		return isContaining(box, pos.x, pos.y, pos.z);
	}
	
	//endregion
	
	//region Adding a multiblock
	
	public static Multiblocks get(World world) {
		return world.scp_byoGetDataManager().getMultiblocks();
	}
	
	public static Multiblocks createFromNbt(NbtCompound nbt, World world) {
		Multiblocks multiblocks = new Multiblocks(world);
		NbtCompound multiblocksNbt = nbt.getCompound("Multiblocks");
		int multiblocksLength = multiblocksNbt.getSize();
		
		for (int i = 1; i <= multiblocksLength; i++) {
			NbtCompound multiblockNbt = multiblocksNbt.getCompound("Multiblock" + i);
			
			Identifier id = new Identifier(multiblockNbt.getString("Identifier"));
			int[] frontTopLeftArr = multiblockNbt.getIntArray("GlobalBottomLeftPos");
			int[] size = multiblockNbt.getIntArray("Size");
			BlockPos frontTopLeftPos = new BlockPos(frontTopLeftArr[0], frontTopLeftArr[1], frontTopLeftArr[2]);
			
			BlockPattern.Result result = new BlockPattern.Result(frontTopLeftPos, Direction.SOUTH, Direction.DOWN, BlockPattern.makeCache(world, false), size[0], size[1], size[2]);
			
			multiblocks.assembleMultiblock(id, result).ifPresent(multiblock -> multiblock.readFromNbt(multiblockNbt.getCompound("Nbt")));
		}
		return multiblocks;
	}
	
	public <T extends Multiblock> void add(T multiblock) {
		this.ingame_multiblocks.add(multiblock);
		
		
		if (!this.getWorld().isClient()) {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			
			buf.writeRegistryKey(this.world.getRegistryKey());
			buf.writeByte(ModConstants.Networking.Multiblocks.ADD_MULTIBLOCK);
			buf.writeBlockPos(multiblock.globalBottomLeftPos);
			
			this.syncWithAllClients(buf);
		}
	}
	
	public List<Multiblock> getList() {
		return this.ingame_multiblocks;
	}
	
	//endregion
	
	public Optional<? extends Multiblock> getMultiblock(BlockPos pos) {
		return this.getMultiblock(pos.toCenterPos());
	}
	
	//region Syncing a multiblock
	
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
	
	public <M extends Multiblock> void remove(M multiblock) {
		this.toRemoveMultiblocks.add(multiblock);
		
		
		if (!this.getWorld().isClient()) {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			
			buf.writeRegistryKey(this.world.getRegistryKey());
			buf.writeByte(ModConstants.Networking.Multiblocks.REMOVE_MULTIBLOCK);
			buf.writeBlockPos(multiblock.globalBottomLeftPos);
			
			this.syncWithAllClients(buf);
		}
	}
	
	//endregion
	
	//region Removing a multiblock
	
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
	
	//endregion
	
	//region Getters
	
	public void syncWithClients(Iterable<ServerPlayerEntity> players, PacketByteBuf buf) {
		if (this.world.isClient) {
			Scp_byo.LOGGER.error("world cannot be client");
			return;
		}
		NetworkManager.sendToPlayers(players, ModConstants.Networking.MULTIBLOCK_UPDATE_PACKET_ID, buf);
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
		
		for (ScpBYOBlockPattern pattern : BlockPatterns.getAll()) {
			BlockPattern blockPattern = pattern.getBlockPattern();
			BlockPattern.Result result = blockPattern.searchAround(this.world, pos);
			if (null != result) {
				Optional<Multiblock> multiblock = this.assembleMultiblock(pattern.getId(), result);
				multiblock.ifPresent(Multiblock::create);
				return multiblock;
			}
		}
		return Optional.empty();
	}
	
	public Optional<Multiblock> tryAssemble(BlockPos pos, Direction forward, Direction up) {
		if (this.getMultiblock(pos).isPresent()) return Optional.empty();
		
		for (ScpBYOBlockPattern pattern : BlockPatterns.getAll()) {
			BlockPattern blockPattern = pattern.getBlockPattern();
			BlockPattern.Result result = blockPattern.testTransform(this.world, pos, forward, up);
			if (null != result) {
				return this.assembleMultiblock(pattern.getId(), result);
			}
		}
		return Optional.empty();
	}
	
	public void tryDisassemble(BlockPos pos) {
		
		var multiblock = this.getMultiblock(pos);
		multiblock.ifPresent(this::remove);
	}
	
	//endregion
	
	//region Nbt management
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtCompound multiblocksNbt = new NbtCompound();
		int multiblockNumber = 0;
		for (Multiblock multiblock : this.ingame_multiblocks) {
			multiblockNumber++;
			NbtCompound multiblockNbt = new NbtCompound();
			multiblockNbt.putString("Identifier", BlockPatterns.getIdForMultiblockClass(multiblock.getClass()).orElseThrow().toString());
			multiblockNbt.putIntArray("GlobalBottomLeftPos", multiblock.getGlobalBottomLeftPosAsArray());
			Vec3i size = multiblock.box.getDimensions();
			multiblockNbt.putIntArray("Size", List.of(size.getX(), size.getY(), size.getZ()));
			
			multiblockNbt.put("Nbt", multiblock.getNbt());
			multiblocksNbt.put("Multiblock" + multiblockNumber, multiblockNbt);
			
		}
		
		nbt.put("Multiblocks", multiblocksNbt);
		return nbt;
	}
	
	private Optional<Multiblock> assembleMultiblock(Identifier id, BlockPattern.Result result) {
		Optional<Class<? extends Multiblock>> mblockOptionalClass = BlockPatterns.getMultiblockClassForId(id);
		Class<? extends Multiblock> mblockClass = mblockOptionalClass.orElseThrow(() -> new RuntimeException("Class for block pattern id " + id + " not found"));
		try {
			
			Constructor<? extends Multiblock> mblockConstructor = mblockClass.getDeclaredConstructor(BlockPattern.Result.class, World.class);
			Multiblock newMultiblock = mblockConstructor.newInstance(result, this.world);
			mblockClass.cast(newMultiblock);
			this.add(newMultiblock);
			return Optional.of(newMultiblock);
			
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
				 InvocationTargetException e) {
			Scp_byo.LOGGER.error(e);
		}
		return Optional.empty();
	}
	
	//endregion
}
