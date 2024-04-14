package io.github.natank25.scp_byo.mutliblock;

import io.github.natank25.scp_byo.utils.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/*
    An instance of a BlockPattern in the world
 */
public class Multiblock {
	
	protected final BlockPos globalBottomLeftPos;
	protected final World world;
	protected final BlockPos centerBlockPos;
	protected final BlockBox box;
	private final BlockPattern pattern;
	private final Direction forward;
	private final Direction up;
	private final BlockPos frontTopLeftPos;
	private final Vec3d frontTopLeftVec;
	private final Vec3d oppositeVec;
	private final Vec3d globalBottomLeftVec;
	private final int width;
	private final int height;
	private final int depth;
	private final VoxelShape shape;
	private ParticleEffect spawnParticles = DustParticleEffect.DEFAULT;
	private ParticleEffect breakParticles = DustParticleEffect.DEFAULT;
	private double particleOffset = 0.15;
	
	public Multiblock(BlockPattern pattern, @NotNull BlockPattern.Result result, World world) {
		this.pattern = pattern;
		this.forward = result.getForwards();
		this.up = result.getUp();
		this.width = result.getWidth();
		this.height = result.getHeight();
		this.depth = result.getDepth();
		this.world = world;
		this.frontTopLeftPos = result.getFrontTopLeft();
		
		this.frontTopLeftVec = this.getVecFromPos(result.getFrontTopLeft());
		this.oppositeVec = this.add(this.frontTopLeftVec, this.width, this.height, this.depth);
		
		this.box = BlockBox.create(new Vec3i(net.minecraft.util.math.MathHelper.floor(this.frontTopLeftVec.x), net.minecraft.util.math.MathHelper.floor(this.frontTopLeftVec.y), net.minecraft.util.math.MathHelper.floor(this.frontTopLeftVec.z)), new Vec3i(net.minecraft.util.math.MathHelper.floor(this.oppositeVec.x), net.minecraft.util.math.MathHelper.floor(this.oppositeVec.y), net.minecraft.util.math.MathHelper.floor(this.oppositeVec.z)));
		
		this.globalBottomLeftPos = new BlockPos(this.box.getMinX(), this.box.getMinY(), this.box.getMinZ());
		this.globalBottomLeftVec = new Vec3d(this.box.getMinX(), this.box.getMinY(), this.box.getMinZ());
		
		this.centerBlockPos = this.globalBottomLeftPos.add(this.width / 2, this.height / 2, this.depth / 2);
		
		this.shape = Block.createCuboidShape(0, 0, 0, this.width << 4, this.height << 4, this.depth << 4);
		
		this.spawnOutlineParticles(this.spawnParticles);
	}
	
	public Vec3d add(Vec3d origin, double offsetLeft, double offsetDown, double offsetForwards) {
		return MathHelper.add(origin, this.forward, this.up, offsetLeft, offsetDown, offsetForwards);
	}
	
	public void create() {
	}
	
	public ParticleEffect getBreakParticles() {
		return this.breakParticles;
	}
	
	public void setBreakParticles(ParticleEffect breakParticles) {
		this.breakParticles = breakParticles;
	}
	
	public BlockPos getCenterBlockPos() {
		return this.centerBlockPos;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public Direction getForward() {
		return this.forward;
	}
	
	public BlockPos getFrontTopLeftPos() {
		return this.frontTopLeftPos;
	}
	
	public int[] getFrontTopLeftPosAsArray() {
		return new int[]{this.frontTopLeftPos.getX(), this.frontTopLeftPos.getY(), this.frontTopLeftPos.getZ()};
	}
	
	public Vec3d getFrontTopLeftVec() {
		return this.frontTopLeftVec;
	}
	
	public BlockPos getGlobalBottomLeftPos() {
		return this.globalBottomLeftPos;
	}
	
	public Vec3d getGlobalBottomLeftVec() {
		return this.globalBottomLeftVec;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public NbtCompound getNbt() {
		return new NbtCompound();
	}
	
	public Vec3d getOppositeVec() {
		return this.oppositeVec;
	}
	
	public double getParticleOffset() {
		return this.particleOffset;
	}
	
	public void setParticleOffset(double particleOffset) {
		this.particleOffset = particleOffset;
	}
	
	public BlockPattern getPattern() {
		return this.pattern;
	}
	
	public VoxelShape getShape() {
		return this.shape;
	}
	
	public ParticleEffect getSpawnParticles() {
		return this.spawnParticles;
	}
	
	public void setSpawnParticles(ParticleEffect spawnParticles) {
		this.spawnParticles = spawnParticles;
	}
	
	public Direction getUp() {
		return this.up;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public void readFromNbt(NbtCompound nbt) {
	
	}
	
	public void tick() {
	
	}
	
	BlockBox getBox() {
		return this.box;
	}
	
	int[] getGlobalBottomLeftPosAsArray() {
		return new int[]{this.globalBottomLeftPos.getX(), this.globalBottomLeftPos.getY(), this.globalBottomLeftPos.getZ()};
	}
	
	protected void destroy() {
		this.spawnOutlineParticles(this.breakParticles);
	}
	
	private void addParticle(ParticleEffect particle, BlockPos pos, Vec3d velocity) {
		this.world.addParticle(particle, pos.getX(), pos.getY(), pos.getZ(), velocity.x, velocity.y, velocity.z);
	}
	
	private void addParticle(ParticleEffect particle, Vec3d pos, Vec3d velocity) {
		this.world.addParticle(particle, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
	}
	
	@NotNull
	private List<Vec3d> getCorners() {
		return MathHelper.getCorners(this.frontTopLeftVec, this.forward, this.up, this.width, this.height, this.depth);
	}
	
	@NotNull
	private List<Vec3d> getOutlinePos(double offset) {
		List<Vec3d> corners = this.getCorners();
		
		
		List<Vec3d> particlesPos = new ArrayList<>(corners);
		
		for (double depthOffset = offset; depthOffset < this.depth; depthOffset += offset) {
			particlesPos.add(this.add(corners.get(0), 0, 0, depthOffset));
			particlesPos.add(this.add(corners.get(0), 0, this.height, depthOffset));
			particlesPos.add(this.add(corners.get(0), this.width, 0, depthOffset));
			particlesPos.add(this.add(corners.get(0), this.width, this.height, depthOffset));
		}
		
		for (double heightOffset = offset; heightOffset < this.height; heightOffset += offset) {
			particlesPos.add(this.add(corners.get(0), 0, heightOffset, 0));
			particlesPos.add(this.add(corners.get(0), this.width, heightOffset, 0));
			particlesPos.add(this.add(corners.get(0), 0, heightOffset, this.depth));
			particlesPos.add(this.add(corners.get(0), this.width, heightOffset, this.depth));
		}
		
		for (double widthOffset = offset; widthOffset < this.width; widthOffset += offset) {
			particlesPos.add(this.add(corners.get(0), widthOffset, 0, 0));
			particlesPos.add(this.add(corners.get(0), widthOffset, this.height, 0));
			particlesPos.add(this.add(corners.get(0), widthOffset, 0, this.depth));
			particlesPos.add(this.add(corners.get(0), widthOffset, this.height, this.depth));
		}
		
		return particlesPos;
	}
	
	private Vec3d getVecFromPos(BlockPos pos) {
		return this.add(pos.toCenterPos(), -0.5, -0.5, -0.5);
	}
	
	private void spawnOutlineParticles(ParticleEffect particle) {
		var particlesPos = this.getOutlinePos(this.particleOffset);
		
		for (Vec3d particlePos : particlesPos) {
			this.world.addParticle(particle, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
		}
	}
}
