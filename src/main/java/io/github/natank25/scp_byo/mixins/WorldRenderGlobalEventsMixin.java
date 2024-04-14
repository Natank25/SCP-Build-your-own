package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.ModGlobalEvents;
import io.github.natank25.scp_byo.sounds.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRenderGlobalEventsMixin {
	
	@Shadow
	@Final
	private MinecraftClient client;
	@Shadow
	private @Nullable ClientWorld world;
	
	@Inject(method = "processGlobalEvent", at = @At("HEAD"))
	private void injectWorldRenderGlobalEvents(int eventId, BlockPos pos, int data, CallbackInfo ci) {
		if (eventId == ModGlobalEvents.SCP096_TRIGGER) {
			Camera camera = this.client.gameRenderer.getCamera();
			if (camera.isReady()) {
				double d = pos.getX() - camera.getPos().x;
				double e = pos.getY() - camera.getPos().y;
				double f = pos.getZ() - camera.getPos().z;
				double g = Math.sqrt(d * d + e * e + f * f);
				double h = camera.getPos().x;
				double i = camera.getPos().y;
				double j = camera.getPos().z;
				if (g > 0.0) {
					h += d / g * 2.0;
					i += e / g * 2.0;
					j += f / g * 2.0;
				}
				
				this.world.playSound(h, i, j, ModSounds.SCP096_TRIGGER, SoundCategory.HOSTILE, 5.0F, 1.0F, false);
			}
		}
	}
}
