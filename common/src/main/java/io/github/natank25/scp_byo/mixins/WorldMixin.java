package io.github.natank25.scp_byo.mixins;

import io.github.natank25.scp_byo.persistent_data.ScpByoDataManager;
import io.github.natank25.scp_byo.utils.ScpByoDataStorage;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin implements ScpByoDataStorage {
	@Unique
	public ScpByoDataManager scp_byo$DataManager;
	
	@Override
	public @NotNull ScpByoDataManager scp_byoGetDataManager() {
		return this.scp_byo$DataManager;
	}
	
	@Inject(method = "<init>*", at = @At("RETURN"))
	private void scp_byo$init(CallbackInfo ci) {
		scp_byo$DataManager = new ScpByoDataManager((World) (Object) this); // Overridden Server side with ServerWorldMixin
	}
	
}
