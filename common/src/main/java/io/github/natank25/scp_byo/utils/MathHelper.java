package io.github.natank25.scp_byo.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public enum MathHelper {
	;


	public static Vec3d add(Vec3d vec, Direction forwards, Direction up, double offsetLeft, double offsetDown, double offsetForwards) {

		if (forwards == up || forwards == up.getOpposite()) {
			throw new IllegalArgumentException("Invalid forwards & up combination");
		}

		Vec3d forwardVec = new Vec3d(forwards.getOffsetX(), forwards.getOffsetY(), forwards.getOffsetZ());
		Vec3d upVec = new Vec3d(up.getOffsetX(), up.getOffsetY(), up.getOffsetZ());
		Vec3d crossProduct = forwardVec.crossProduct(upVec);
		return vec.add(upVec.getX() * -offsetDown + crossProduct.getX() * offsetLeft + forwardVec.getX() * offsetForwards, upVec.getY() * -offsetDown + crossProduct.getY() * offsetLeft + forwardVec.getY() * offsetForwards, upVec.getZ() * -offsetDown + crossProduct.getZ() * offsetLeft + forwardVec.getZ() * offsetForwards);
	}

	public static Vec3i add(Vec3i vec, Direction forwards, Direction up, double offsetLeft, double offsetDown, double offsetForwards) {

		if (forwards == up || forwards == up.getOpposite()) {
			throw new IllegalArgumentException("Invalid forwards & up combination");
		}

		Vec3i forwardVec = new Vec3i(forwards.getOffsetX(), forwards.getOffsetY(), forwards.getOffsetZ());
		Vec3i upVec = new Vec3i(up.getOffsetX(), up.getOffsetY(), up.getOffsetZ());
		Vec3i crossProduct = forwardVec.crossProduct(upVec);
		return vec.add((int) (upVec.getX() * -offsetDown + crossProduct.getX() * offsetLeft + forwardVec.getX() * offsetForwards),
				(int) (upVec.getY() * -offsetDown + crossProduct.getY() * offsetLeft + forwardVec.getY() * offsetForwards),
				(int) (upVec.getZ() * -offsetDown + crossProduct.getZ() * offsetLeft + forwardVec.getZ() * offsetForwards));
	}

	public static BlockPos add(BlockPos pos, Direction forwards, Direction up, int offsetLeft, int offsetDown, int offsetForwards) {
		return new BlockPos(add(new Vec3i(pos.getX(), pos.getY(), pos.getZ()), forwards, up, offsetLeft, offsetDown, offsetForwards));
	}

	public static Vec3d add(Vec3d vec, Direction forwards, Direction up, Vec3d amount) {
		return add(vec, forwards, up, amount.x, amount.y, amount.z);
	}
	
	public static List<Vec3d> getCorners(Vec3d corner, Direction forward, Direction up, double width, double height, double depth) {
		List<Vec3d> corners = new ArrayList<>();
		corners.add(corner);
		
		corners.add(add(corner, forward, up, width, 0, 0));
		corners.add(add(corner, forward, up, 0, height, 0));
		corners.add(add(corner, forward, up, 0, 0, depth));
		corners.add(add(corner, forward, up, width, height, 0));
		corners.add(add(corner, forward, up, width, 0, depth));
		corners.add(add(corner, forward, up, 0, height, depth));
		corners.add(add(corner, forward, up, width, height, depth));
		
		return corners;
	}

}
