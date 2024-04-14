package io.github.natank25.scp_byo.sounds;

import io.github.natank25.scp_byo.scp_byo;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum ModSounds {
    ;
    public static final SoundEvent SLIDING_DOOR_OPEN = registerSoundEvent("sliding_door_open");

    public static final SoundEvent SCP096_TRIGGER = registerSoundEvent("scp_096_trigger");// done
    public static final SoundEvent SCP096_SCREAM = registerSoundEvent("scp_096_scream");
    public static final SoundEvent SCP096_CHASE = registerSoundEvent("scp_096_chase");// done
    public static final SoundEvent SCP096_ELEVATOR_SLAM = registerSoundEvent("scp_096_elevator_slam");
    public static final SoundEvent SCP096_IDLE = registerSoundEvent("scp_096_idle");// done
    public static final SoundEvent SCP096_KILL = registerSoundEvent("scp_096_kill");
    public static final SoundEvent SCP096_RAGE = registerSoundEvent("scp_096_rage");// done
    public static final SoundEvent SCP096_RAGE_CHASE = registerSoundEvent("scp_096_rage_chase");// done


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(scp_byo.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerModSounds() {
        scp_byo.LOGGER.debug("Registering ModSounds for " + scp_byo.MOD_ID);

    }
}
