package io.github.natank25.scp_byo.mutliblock;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import io.github.natank25.scp_byo.persistent_data.cca.interfaces.ListComponent;
import io.github.natank25.scp_byo.persistent_data.cca.register.ModWorldComponents;
import io.github.natank25.scp_byo.utils.ModConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Multiblocks<M extends Multiblock> implements ListComponent<M>, AutoSyncedComponent, CommonTickingComponent {
	
	private final List<M> ingame_multiblocks = new ArrayList<>();
	private final List<M> toRemoveMultiblocks = new ArrayList<>();
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
	
	@Override
	public void add(M multiblock) {
		this.ingame_multiblocks.add(multiblock);
	}
	
	@Override
	public List<M> getList() {
		return this.ingame_multiblocks;
	}
	
	public Optional<M> getMultiblock(BlockPos pos) {
		return this.getMultiblock(pos.toCenterPos());
	}
	
	public Optional<M> getMultiblock(Vec3d pos) {
		for (M multiblock : this.ingame_multiblocks) {
			if (isContaining(multiblock.getBox(), pos)) {
				return Optional.of(multiblock);
			}
		}
		return Optional.empty();
	}
	
	public World getWorld() {
		return this.world;
	}
	
	@Override
	public void readFromNbt(@NotNull NbtCompound tag) {
		
		NbtCompound multiblocksNbt = tag.getCompound("Multiblocks");
		int multiblocksLength = multiblocksNbt.getSize();
		
		for (int i = 1; i <= multiblocksLength; i++) {
			NbtCompound multiblockNbt = multiblocksNbt.getCompound("Multiblock" + i);
			int[] frontTopLeftArr = multiblockNbt.getIntArray("GlobalPos");
			BlockPos globalPos = new BlockPos(frontTopLeftArr[0], frontTopLeftArr[1], frontTopLeftArr[2]);
			
			for (int x = 0; x <= 1; x++) {
				for (int y = 0; y <= 1; y++) {
					BlockPos pos = globalPos.add(16*x,0,16*y);
					this.world.updateNeighbors(pos, this.world.getBlockState(pos).getBlock()); // Update the neighboring chunks so the scanning can work
					
				}
			}
			
			
			
			
			this.tryAssemble(globalPos, Direction.SOUTH, Direction.DOWN).ifPresent(multiblock -> multiblock.readFromNbt(multiblockNbt.getCompound("Nbt")));
		}
	}
	
	@Override
	public void remove(M value) {
		this.toRemoveMultiblocks.add(value);
	}
	
	public void syncDisassembleWithClient(BlockPos pos) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(pos);
		
		for (ServerPlayerEntity player : PlayerLookup.world((ServerWorld) this.world)) {
			ServerPlayNetworking.send(player, ModConstants.Networking.DESTROY_MULTIBLOCK_PACKET_ID, buf);
		}
	}
	
	@Override
	public void tick() {
		for (M multiblock : this.toRemoveMultiblocks) {
			multiblock.destroy();
			this.ingame_multiblocks.remove(multiblock);
		}
		if (!this.toRemoveMultiblocks.isEmpty()) this.toRemoveMultiblocks.clear();
		this.ingame_multiblocks.forEach(Multiblock::tick);
		this.world.syncComponent(ModWorldComponents.MULTIBLOCKS);
	}
	
	public Optional<Multiblock> tryAssemble(BlockPos pos) {
		if (this.getMultiblock(pos).isPresent()) return Optional.empty();
		
		for (BlockPattern pattern : BlockPatternsRegistry.getAll()) {
			BlockPattern.Result result = pattern.searchAround(this.world, pos);
			if (null != result) {
				Optional<? extends Multiblock> multiblock = this.assembleMultiblock(pattern, result);
				multiblock.ifPresent(Multiblock::create);
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
		multiblock.ifPresent(value -> {
			this.remove(value);
			
			if (!this.world.isClient) {
				this.syncDisassembleWithClient(pos);
			}
		});
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		NbtCompound multiblocksNbt = new NbtCompound();
		int multiblockNumber = 0;
		for (Multiblock multiblock : this.ingame_multiblocks) {
			multiblockNumber++;
			NbtCompound multiblockNbt = new NbtCompound();
			multiblockNbt.putIntArray("GlobalPos", multiblock.getGlobalBottomLeftPosAsArray());
			
			multiblockNbt.put("Nbt", multiblock.getNbt());
			multiblocksNbt.put("Multiblock" + multiblockNumber, multiblockNbt);
			
		}
		
		tag.put("Multiblocks", multiblocksNbt);
	}
	
	@Nullable
	private Optional<Multiblock> assembleMultiblock(BlockPattern pattern, BlockPattern.Result result) {
		Optional<Class<? extends Multiblock>> mblockOptionalClass = BlockPatternsRegistry.getMultiblockForBlockPattern(pattern);
		Class<? extends Multiblock> mblockClass = mblockOptionalClass.orElseThrow(() -> new RuntimeException("Class for block pattern " + pattern + " not found"));
		try {
			
			Constructor<? extends Multiblock> mblockConstructor = mblockClass.getDeclaredConstructor(BlockPattern.class, BlockPattern.Result.class, World.class);
			Multiblock newMultiblock = mblockConstructor.newInstance(pattern, result, this.world);
			mblockClass.cast(newMultiblock);
			this.add((M) newMultiblock);
			return Optional.of(newMultiblock);
			
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
				 InvocationTargetException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
}
