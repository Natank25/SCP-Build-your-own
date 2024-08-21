package io.github.natank25.scp_byo.mixins;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererDispatcherMixin {
	
	
	@Inject(method = "render", at = @At("HEAD"))
	private <E extends Entity> void render(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		/* TODO
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		
 		if (!player.getComponent(ModEntitiesComponents.HAS_SEEN_SCP).getValue() && entity instanceof ScpEntity && player.canSee(entity)) {
			
			player.getComponent(ModEntitiesComponents.HAS_SEEN_SCP).swap();
			ModEntitiesComponents.HAS_SEEN_SCP.sync(player);
			
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeString("1st_scp");
			buf.writeIdentifier(new Identifier("scp_byo/see_1st_scp"));
			
			ClientPlayNetworking.send(ModConstants.Networking.GRANT_ADVANCEMENT_PACKET_ID, buf);
		}
		*/
		
	}
}
