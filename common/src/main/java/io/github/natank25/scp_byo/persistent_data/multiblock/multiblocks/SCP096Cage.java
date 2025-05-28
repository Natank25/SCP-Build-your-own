package io.github.natank25.scp_byo.persistent_data.multiblock.multiblocks;

import io.github.natank25.scp_byo.advancements.ModCriterions;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.persistent_data.multiblock.Multiblock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//TODO: change how random block is obtained so that it looks more like a crack than a random block
public class SCP096Cage extends Multiblock {
	
	private final Box insideBox;
	private final Map<BlockPos, Integer> blockPosToProgress = new HashMap<>();
	private final Random random;
	private int totalDamage = 0;
	private int updateCooldown = 0;
	
	public SCP096Cage(BlockPattern.@NotNull Result result, World world) {
		super(result, world);
		this.insideBox = this.getShape().getBoundingBox().contract(1);
		this.random = Random.create(this.getFrontTopLeftPos().asLong());
	}
	
	public static BlockPattern getBlockPattern() {
		return BlockPatternBuilder.start().aisle("iiiii", "iiiii", "iiiii", "iiiii", "iiiii").aisle("iiiii", "iaaai", "iaaai", "iaaai", "iiiii").aisle("iiiii", "iaaai", "iaaai", "iaaai", "iiiii").aisle("iiiii", "iaaai", "iaaai", "iaaai", "iiiii").aisle("iiiii", "iiiii", "iiiii", "iiiii", "iiiii").where('i', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('a', CachedBlockPosition.matchesBlockState(AbstractBlock.AbstractBlockState::isAir)).build();
	}
	
	private static boolean isNotInside(int x, int y, int z) {
		return x >= 1 && x < 4 && y >= 1 && y < 4 && z >= 1 && z < 4;
	}
	
	@Override
	public void applyGenericUpdatePacket(PacketByteBuf buf) {
		BlockPos pos = buf.readBlockPos();
		this.damageBlock(pos);
	}
	
	public boolean containsScp() {
		return this.world.getEntitiesByClass(Scp_096Entity.class, this.insideBox.offset(this.globalBottomLeftPos.add(1, 1, 1)), scp096Entity -> true).stream().findFirst().isPresent();
	}
	
	@Override
	public void create() {
		if (!this.getWorld().isClient() && this.containsScp()) {
			for (PlayerEntity player : this.world.getEntitiesByClass(PlayerEntity.class, this.insideBox.offset(this.centerBlockPos).expand(15), player -> true)) {
				ModCriterions.TRAP_SCP.trigger((ServerPlayerEntity) player, ModEntities.SCP_096.get());
			}
		}
	}
	
	@Override
	public void destroy() {
		for (BlockPos blockPos : this.blockPosToProgress.keySet()) {
			setBlockBreakingInfo(blockPos, -1);
		}
		
		this.blockPosToProgress.clear();
		super.destroy();
	}
	
	public Box getInsideBox() {
		return this.insideBox;
	}
	
	@Override
	public NbtCompound getNbt() {
		NbtCompound nbt = super.getNbt();
		
		int counter = 0;
		for (Map.Entry<BlockPos, Integer> entry : this.blockPosToProgress.entrySet()) {
			NbtCompound blockNbt = new NbtCompound();
			BlockPos pos = entry.getKey(); //TODO optimize -> 1 nbt compound per damage, each nbt compound have list of blocks pos
			blockNbt.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
			blockNbt.putInt("damage", entry.getValue());
			
			nbt.put("block" + counter, blockNbt);
			counter++;
		}
		
		return nbt;
	}
	
	public Optional<Scp_096Entity> getScp() {
		return this.world.getEntitiesByClass(Scp_096Entity.class, this.insideBox.offset(this.globalBottomLeftPos.add(1, 1, 1)), scp096Entity -> true).stream().findFirst();
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		
		for (int i = 0; i < nbt.getSize(); i++) {
			NbtCompound blockNbt = nbt.getCompound("block" + i);
			int damage = blockNbt.getInt("damage");
			this.totalDamage += damage;
			int[] posAsArray = blockNbt.getIntArray("pos");
			BlockPos pos = new BlockPos(posAsArray[0], posAsArray[1], posAsArray[2]);
			
			this.blockPosToProgress.put(pos, damage);
		}
		
		super.readFromNbt(nbt);
	}
	
	public boolean repair(PlayerEntity player) {
		int requiredIron = this.totalDamage / 9;
		
		if (requiredIron == 0) {
			player.sendMessage(Text.literal("This containment cell doesn't need to be repaired"), true);
			return false;
		}
		
		if (player.getInventory().contains(new ItemStack(Items.IRON_BLOCK))) {
			int totalIronBlocks = player.getInventory().main.stream().filter(stack -> stack.getItem() == Items.IRON_BLOCK).mapToInt(ItemStack::getCount).sum();
			
			if (totalIronBlocks >= requiredIron) {
				for (BlockPos blockPos : this.blockPosToProgress.keySet()) {
					setBlockBreakingInfo(blockPos, -1);
				}
				this.blockPosToProgress.clear();
				player.getInventory().remove(itemStack -> itemStack.isItemEqual(new ItemStack(Items.IRON_BLOCK)), requiredIron, player.playerScreenHandler.getCraftingInput());
				this.world.playSoundAtBlockCenter(this.centerBlockPos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.5f, 1, true);
				this.totalDamage = 0;
				return true;
			}
		}
		
		player.sendMessage(Text.literal("You need " + requiredIron + " iron " + (requiredIron == 1 ? "block" : "blocks") + " to repair this containment cell"), true);
		return false;
	}
	
	@Override
	public void tick() {
		if (this.world.isClient()) return;
		this.updateCooldown++;
		if (this.updateCooldown % 3 == 0) { // 20
			this.updateCooldown = 0;
			if (this.containsScp() && this.random.nextInt(10) == 0) { // 60
				BlockPos pos = this.getRandomBlock();
				
				this.damageBlock(pos);
				
				PacketByteBuf buf = this.getEmptyUpdatePacket();
				buf.writeBlockPos(pos);
				
				this.sendGenericUpdatePacket(buf);
			}
		}
		
		if (this.containsScp()) {
			for (Map.Entry<BlockPos, Integer> entry : this.blockPosToProgress.entrySet()) {
				BlockPos pos = entry.getKey();
				setBlockBreakingInfo(pos, entry.getValue());
			}
		}
	}
	
	private void damageBlock(BlockPos pos) {
		if (!this.blockPosToProgress.containsKey(pos)) this.blockPosToProgress.put(pos, -1);
		
		int newProgress = this.blockPosToProgress.get(pos) + 1;
		
		this.blockPosToProgress.put(pos, newProgress);
		setBlockBreakingInfo(pos, newProgress);
		this.totalDamage++;
		
		if (newProgress == 10) {
			this.getWorld().updateNeighbors(pos, this.getWorld().getBlockState(pos).getBlock());
			this.world.breakBlock(pos, true);
			this.selfDisassemble();
		}
	}
	
	private BlockPos getRandomBlock() {
		
		int x = this.random.nextInt(5);
		int y = this.random.nextInt(5);
		int z = this.random.nextInt(5);
		
		while (isNotInside(x, y, z)) {
			
			x = this.random.nextInt(5);
			y = this.random.nextInt(5);
			z = this.random.nextInt(5);
		}
		
		return this.globalBottomLeftPos.add(x, y, z);
	}
	
	private void setBlockBreakingInfo(BlockPos blockPos, int progress) {
		if (this.world.isClient) return;
		this.world.setBlockBreakingInfo(blockPos.hashCode(), blockPos, progress);
	}
}
