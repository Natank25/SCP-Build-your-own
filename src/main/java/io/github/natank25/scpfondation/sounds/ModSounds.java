package io.github.natank25.scpfondation.sounds;

import io.github.natank25.scpfondation.ScpFondation;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent SLIDING_DOOR_OPEN = registerSoundEvent();

    private static SoundEvent registerSoundEvent(){
        Identifier id = new Identifier(ScpFondation.MOD_ID, "sliding_door_open");
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerModSounds() {
        ScpFondation.LOGGER.debug("Registering ModSounds for " + ScpFondation.MOD_ID);

    }
}
