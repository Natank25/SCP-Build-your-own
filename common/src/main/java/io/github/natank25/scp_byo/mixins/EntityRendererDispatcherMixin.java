package io.github.natank25.scp_byo.mixins;

import dev.architectury.networking.NetworkManager;
import io.github.natank25.scp_byo.entity.custom.ScpEntity;
import io.github.natank25.scp_byo.persistent_data.player.PerPlayerData;
import io.github.natank25.scp_byo.utils.ModConstants;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererDispatcherMixin {
	
	
	@Inject(method = "render", at = @At("RETURN"))
	private <E extends Entity> void render(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		assert player != null;
		if (!PerPlayerData.getPlayerData(player).hasSeenScp() && entity instanceof ScpEntity && player.canSee(entity)) {
			
			PerPlayerData.getPlayerData(player).setHasSeenScp(true);
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			NetworkManager.sendToServer(ModConstants.Networking.UPDATE_PLAYER_DATA, buf);
			
			buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeString("1st_scp");
			buf.writeIdentifier(new Identifier("scp_byo/see_1st_scp"));
			
			NetworkManager.sendToServer(ModConstants.Networking.GRANT_ADVANCEMENT_PACKET_ID, buf);
		}
		
		
	}
}
