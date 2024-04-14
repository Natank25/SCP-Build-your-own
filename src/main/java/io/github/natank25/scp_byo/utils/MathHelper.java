package io.github.natank25.scp_byo.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public enum MathHelper {
    ;

    public static BlockPos add(BlockPos pos, Direction forwards, Direction up, int offsetLeft, int offsetDown, int offsetForwards) {
        if (forwards != up && forwards != up.getOpposite()) {
            Vec3i vec3i = new Vec3i(forwards.getOffsetX(), forwards.getOffsetY(), forwards.getOffsetZ());
            Vec3i vec3i2 = new Vec3i(up.getOffsetX(), up.getOffsetY(), up.getOffsetZ());
            Vec3i vec3i3 = vec3i.crossProduct(vec3i2);
            return pos.add(vec3i2.getX() * -offsetDown + vec3i3.getX() * offsetLeft + vec3i.getX() * offsetForwards, vec3i2.getY() * -offsetDown + vec3i3.getY() * offsetLeft + vec3i.getY() * offsetForwards, vec3i2.getZ() * -offsetDown + vec3i3.getZ() * offsetLeft + vec3i.getZ() * offsetForwards);
        } else {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
    }

    public static Vec3d add(Vec3d vec, Direction forwards, Direction up, double offsetLeft, double offsetDown, double offsetForwards) {

        if (forwards == up || forwards == up.getOpposite()) {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }

        Vec3d forwardVec = new Vec3d(forwards.getOffsetX(), forwards.getOffsetY(), forwards.getOffsetZ());
        Vec3d upVec = new Vec3d(up.getOffsetX(), up.getOffsetY(), up.getOffsetZ());
        Vec3d crossProduct = forwardVec.crossProduct(upVec);
        return vec.add(upVec.getX() * -offsetDown + crossProduct.getX() * offsetLeft + forwardVec.getX() * offsetForwards, upVec.getY() * -offsetDown + crossProduct.getY() * offsetLeft + forwardVec.getY() * offsetForwards, upVec.getZ() * -offsetDown + crossProduct.getZ() * offsetLeft + forwardVec.getZ() * offsetForwards);
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

    public static List<BlockPos> getCorners(BlockPos corner, Direction forward, Direction up, int width, int height, int depth) {
        List<BlockPos> corners = new ArrayList<>();
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
