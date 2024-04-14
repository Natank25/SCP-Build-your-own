package io.github.natank25.scp_byo.mutliblock.ModMultiblocks;

import io.github.natank25.scp_byo.advancements.ModCriterions;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.entity.custom.Scp_096Entity;
import io.github.natank25.scp_byo.mutliblock.Multiblock;
import io.github.natank25.scp_byo.persistent_data.cca.register.ModWorldComponents;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
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
	
	public SCP096Cage(BlockPattern pattern, BlockPattern.@NotNull Result result, World world) {
		super(pattern, result, world);
		this.insideBox = this.getShape().getBoundingBox().contract(1);
		this.random = Random.create(this.getFrontTopLeftPos().asLong());
	}
	
	private static boolean isNotInside(int x, int y, int z) {
		return x >= 1 && x < 4 && y >= 1 && y < 4 && z >= 1 && z < 4;
	}
	
	public boolean containsScp() {
		return this.world.getEntitiesByClass(Scp_096Entity.class, this.insideBox.offset(this.globalBottomLeftPos.add(1, 1, 1)), scp096Entity -> true).stream().findFirst().isPresent();
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
			BlockPos pos = entry.getKey();
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
	public void create() {
		if (!this.getWorld().isClient() && this.containsScp()){
			for (PlayerEntity player : this.world.getEntitiesByClass(PlayerEntity.class, this.insideBox.offset(this.centerBlockPos).expand(15), player -> true)) {
				ModCriterions.TRAP_SCP.trigger((ServerPlayerEntity) player, ModEntities.SCP_096);
			}
		}
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
					this.world.setBlockBreakingInfo(blockPos.hashCode(), blockPos, -1);
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
		this.updateCooldown++;
		if (this.updateCooldown % 20 == 0) {
			this.updateCooldown = 0;
			if (this.random.nextInt(this.containsScp() ? 50 : 400) == 0) {
				BlockPos pos = this.getRandomBlock();
				
				if (!this.blockPosToProgress.containsKey(pos)) this.blockPosToProgress.put(pos, -1);
				
				int newProgress = this.blockPosToProgress.get(pos) + 1;
				
				this.blockPosToProgress.put(pos, newProgress);
				this.world.setBlockBreakingInfo(pos.hashCode(), pos, newProgress);
				this.totalDamage++;
				if (newProgress == 10) {
					this.getWorld().updateNeighbors(pos, this.getWorld().getBlockState(pos).getBlock());
					BlockState state = this.world.getBlockState(pos);
					this.world.breakBlock(pos, true);
					this.world.getComponent(ModWorldComponents.MULTIBLOCKS).tryDisassemble(pos);
					this.getWorld().updateNeighbors(pos, this.getWorld().getBlockState(pos).getBlock());
				}
			}
			
		}
		
		
		for (Map.Entry<BlockPos, Integer> entry : this.blockPosToProgress.entrySet()) {
			BlockPos pos = entry.getKey();
			this.world.setBlockBreakingInfo(pos.hashCode(), pos, entry.getValue());
		}
	}
	
	@Override
	protected void destroy() {
		for (BlockPos blockPos : this.blockPosToProgress.keySet()) {
			this.world.setBlockBreakingInfo(blockPos.hashCode(), blockPos, -1);
		}
		
		this.blockPosToProgress.clear();
		super.destroy();
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
}
