package io.github.natank25.scp_byo.persistent_data.multiblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.utils.ModConstants;
import io.github.natank25.scp_byo.utils.Utils;
import io.netty.buffer.Unpooled;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Multiblocks extends PersistentState {

    private final List<Multiblock> ingame_multiblocks = new ArrayList<>();
    private final List<Multiblock> toRemoveMultiblocks = new ArrayList<>(); // Will maybe cause a bug if server stop on remove: store in the file
    private final World world;
    static private final String WORLD_NOT_SERVER_ERR = "World cannot be client";

	private static final Function<Context, Codec<Multiblocks>> CODEC_GETTER = context -> RecordCodecBuilder.create(
			instance -> instance.group(
					Multiblock.MultiblockData.CODEC.listOf().fieldOf("mutliblocks").forGetter(Multiblocks::pack)
			).apply(instance, multiblockData -> new Multiblocks(context.world(), multiblockData))
	);

	private static final PersistentStateType<Multiblocks> MULTIBLOCKS_TYPE = new PersistentStateType<>(
			"scp_byo/myKey",
			context -> new Multiblocks(context.getWorldOrThrow()),
			CODEC_GETTER,
			null
	);

	private List<Multiblock.MultiblockData> pack() {
		List<Multiblock.MultiblockData> list = new ArrayList<>();
		this.ingame_multiblocks.forEach(multiblock -> {
			Multiblock.MultiblockData data = new Multiblock.MultiblockData(
					BlockPatterns.getIdForMultiblockClass(multiblock.getClass()).orElseThrow(),
					Arrays.stream(multiblock.getFrontTopLeftPosAsArray()),
					Utils.Vec3IToStream(multiblock.box.getDimensions()),
					multiblock.getNbt());
		});
		return list;
	}

	public Multiblocks(World world, List<Multiblock.MultiblockData> multiblocks){
		this.world = world;

		multiblocks.forEach(data -> {
			Identifier id = data.id();
			int[] frontTopLeftArr = data.pos().toArray();
			int[] size = data.size().toArray();
			BlockPos frontTopLeftPos = new BlockPos(frontTopLeftArr[0], frontTopLeftArr[1], frontTopLeftArr[2]);

			BlockPattern.Result result = new BlockPattern.Result(frontTopLeftPos, Direction.SOUTH, Direction.DOWN, BlockPattern.makeCache(world, false), size[0], size[1], size[2]);

			this.assembleMultiblock(id, result).ifPresent(multiblock -> multiblock.readFromNbt(data.data()));

		});
	}

	public Multiblocks(World world) {
		this.world = world;
	}

	private static boolean isContaining(BlockBox box, double x, double y, double z) {
		return x >= box.getMinX() && x <= box.getMaxX() && z >= box.getMinZ() && z <= box.getMaxZ() && y >= box.getMinY() && y <= box.getMaxY();
	}
	
	private static boolean isContaining(BlockBox box, Vec3d pos) {
		return isContaining(box, pos.x, pos.y, pos.z);
	}

	public static Multiblocks get(World world) {
		if (world.isClient())
			return null;
		return ((ServerWorld) world).getPersistentStateManager().getOrCreate(MULTIBLOCKS_TYPE);
	}

	public static Multiblocks get(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(MULTIBLOCKS_TYPE);
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

	public void syncWithAllClients(PacketByteBuf buf) {
		if (this.world.isClient) {
			Scp_byo.LOGGER.error(WORLD_NOT_SERVER_ERR);
			return;
		}
		if (this.world.getServer() != null)
			this.syncWithClients(this.world.getServer().getPlayerManager().getPlayerList(), buf);
	}
	
	public void syncWithClients(Iterable<ServerPlayerEntity> players, PacketByteBuf buf) {
		if (this.world.isClient) {
			Scp_byo.LOGGER.error(WORLD_NOT_SERVER_ERR);
			return;
		}
		//TODO: NetworkManager.sendToPlayers(players, ModConstants.Networking.MULTIBLOCK_UPDATE_PACKET_ID, buf);
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
			BlockPattern blockPattern = pattern.blockPattern();
			BlockPattern.Result result = blockPattern.searchAround(this.world, pos);
			if (null != result) {
				Optional<Multiblock> multiblock = this.assembleMultiblock(pattern.id(), result);
				multiblock.ifPresent(Multiblock::create);
				return multiblock;
			}
		}
		return Optional.empty();
	}
	
	public Optional<Multiblock> tryAssemble(BlockPos pos, Direction forward, Direction up) {
		if (this.getMultiblock(pos).isPresent()) return Optional.empty();
		
		for (ScpBYOBlockPattern pattern : BlockPatterns.getAll()) {
			BlockPattern blockPattern = pattern.blockPattern();
			BlockPattern.Result result = blockPattern.testTransform(this.world, pos, forward, up);
			if (null != result) {
				return this.assembleMultiblock(pattern.id(), result);
			}
		}
		return Optional.empty();
	}
	
	public void tryDisassemble(BlockPos pos) {
		
		var multiblock = this.getMultiblock(pos);
		multiblock.ifPresent(this::remove);
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
}
