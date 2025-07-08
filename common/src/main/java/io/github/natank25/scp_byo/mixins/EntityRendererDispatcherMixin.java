package io.github.natank25.scp_byo.mixins;

import dev.architectury.networking.NetworkManager;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.networking.GrantAdvancementPayload;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererDispatcherMixin {


	@Shadow private World world;

	@Unique
	private <S extends EntityRenderState> boolean scp_byo$canPlayerRaycastSee(ClientPlayerEntity player, S state, double yDelta){
        Vec3d vec3d = new Vec3d(state.x, yDelta, state.z);
        Vec3d vec3d2 = new Vec3d(player.getX(), player.getY(), player.getZ());
        return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, player)).getType() == HitResult.Type.MISS;
    }

	@Unique
	private <S extends EntityRenderState> boolean scp_byo$canPlayerSee(S state) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null || client.world == null)
			return false;
		ClientPlayerEntity player = client.player;
		double eyeY = state.standingEyeHeight + state.y;
		double[] checkedYs = new double[]{state.y, eyeY, state.y + 0.5, (eyeY + state.y) / 2.0, state.y + state.height};

		Vec3d vec3d = player.getRotationVec(1.0F).normalize();

		for (double e : checkedYs) {
			Vec3d vec3d2 = new Vec3d(state.x - player.getX(), e - player.getEyeY(), state.z - player.getZ());
			vec3d2 = vec3d2.normalize();
			double g = vec3d.dotProduct(vec3d2);
			if (g > 0.5
					&& scp_byo$canPlayerRaycastSee(player, state, e)) {
				return true;
			}
		}
		return false;
	}
	
	@Inject(method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/EntityRenderer;)V", at = @At("RETURN"))
	private <S extends EntityRenderState> void render(S state, double x, double y, double z, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EntityRenderer<?, S> renderer, CallbackInfo ci) {

		if (state.entityType == ModEntities.SCP_096.get() && scp_byo$canPlayerSee(state)) {

			//NetworkManager.sendToServer(new UpdatePlayerDataPayload());

			NetworkManager.sendToServer(new GrantAdvancementPayload("1st_scp", Utils.newIdentifier("see_1st_scp")));
		}
		
	}
}
