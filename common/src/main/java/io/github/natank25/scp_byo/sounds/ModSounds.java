package io.github.natank25.scp_byo.sounds;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum ModSounds {
	;
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Scp_byo.MOD_ID, RegistryKeys.SOUND_EVENT);
	
	public static final RegistrySupplier<SoundEvent> SLIDING_DOOR_OPEN = registerSoundEvent("sliding_door_open");
	public static final RegistrySupplier<SoundEvent> SCP096_TRIGGER = registerSoundEvent("scp_096_trigger");// done
	public static final RegistrySupplier<SoundEvent> SCP096_SCREAM = registerSoundEvent("scp_096_scream");
	public static final RegistrySupplier<SoundEvent> SCP096_CHASE = registerSoundEvent("scp_096_chase");// done
	public static final RegistrySupplier<SoundEvent> SCP096_ELEVATOR_SLAM = registerSoundEvent("scp_096_elevator_slam");
	public static final RegistrySupplier<SoundEvent> SCP096_IDLE = registerSoundEvent("scp_096_idle");// done
	public static final RegistrySupplier<SoundEvent> SCP096_KILL = registerSoundEvent("scp_096_kill");
	public static final RegistrySupplier<SoundEvent> SCP096_RAGE = registerSoundEvent("scp_096_rage");// done
	public static final RegistrySupplier<SoundEvent> SCP096_RAGE_CHASE = registerSoundEvent("scp_096_rage_chase");// done
	
	
	private static RegistrySupplier<SoundEvent> registerSoundEvent(String name) {
		Identifier id = Utils.newIdentifier(name);
		return SOUNDS.register(id, () -> SoundEvent.of(id));
	}
	
	public static void registerModSounds() {
		SOUNDS.register();
		
	}
}
