package io.github.natank25.scp_byo.mixins;

import dev.architectury.networking.NetworkManager;
import io.github.natank25.scp_byo.entity.custom.ScpEntity;
import io.github.natank25.scp_byo.networking.GrantAdvancementPayload;
import io.github.natank25.scp_byo.networking.UpdatePlayerDataPayload;
import io.github.natank25.scp_byo.persistent_data.player.PerPlayerData;
import io.github.natank25.scp_byo.utils.ModConstants;
import io.github.natank25.scp_byo.utils.Utils;
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
			NetworkManager.sendToServer(new UpdatePlayerDataPayload());
			
			NetworkManager.sendToServer(new GrantAdvancementPayload("1st_scp", Utils.newIdentifier("see_1st_scp")));
		}
		
		
	}
}
