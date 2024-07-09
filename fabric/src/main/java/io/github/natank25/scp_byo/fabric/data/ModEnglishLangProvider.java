package io.github.natank25.scp_byo.fabric.data;

import io.github.natank25.scp_byo.ModGamerules;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.io.IOException;
import java.nio.file.Path;

public class ModEnglishLangProvider extends FabricLanguageProvider {
    public ModEnglishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.KEYCARD_1.get(), "Keycard L.1");
        translationBuilder.add(ModItems.KEYCARD_2.get(), "Keycard L.2");
        translationBuilder.add(ModItems.KEYCARD_3.get(), "Keycard L.3");
        translationBuilder.add(ModItems.KEYCARD_4.get(), "Keycard L.4");
        translationBuilder.add(ModItems.KEYCARD_5.get(), "Keycard L.5");
        translationBuilder.add(ModItems.KEYCARD_6.get(), "Keycard Omni");
        translationBuilder.add(ModItems.WRENCH.get(), "Wrench");
        translationBuilder.add(ModItems.SCP_096_SPAWN_EGG.get(), "SCP 096 Spawn Egg");

        translationBuilder.add(ModBlocks.CONCRETE_FLOOR.get(), "Concrete Floor");
        translationBuilder.add(ModBlocks.SLIDING_DOOR.get(), "Sliding Door");
        translationBuilder.add(ModBlocks.DIRTY_METAL.get(), "Dirty Metal");
        translationBuilder.add(ModBlocks.TILE_FLOOR.get(), "Tile Floor");
        translationBuilder.add(ModBlocks.WHITE_WALL.get(), "White Wall");
        translationBuilder.add(ModBlocks.CONCRETE_WALL.get(), "Concrete Wall");
        translationBuilder.add(ModBlocks.KEYCARD_READER.get(), "Keycard Reader");
        translationBuilder.add(ModBlocks.ELEVATOR_FLOOR.get(), "Elevator Floor");
        translationBuilder.add(ModBlocks.ELEVATOR_WALL.get(), "Elevator Wall");
        translationBuilder.add(ModBlocks.FONDATION_GLASS.get(), "Fondation Glass");
        translationBuilder.add(ModBlocks.OFFICE_WALL.get(), "Office Wall");

        translationBuilder.add(ModEntities.SCP_096.get(), "SCP 096");

        translationBuilder.add("scp_byo.commands.summonscp096.alreadyexists", "SCP 096 already exists !");

        translationBuilder.add(ModItems.SCP_FONDATION_ITEM_GROUP.get(), "SCP: BYO");

        translationBuilder.add(ModGamerules.CAN_SCP096_SPAWN.getTranslationKey(), "Can SCP-096 spawn naturally");

        // Load an existing language file. for sounds
        try {
            Path existingFilePath = this.dataOutput.getModContainer().findPath("assets/scp_byo/lang/en_us.existing.json").get();
            translationBuilder.add(existingFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to add existing language file!", e);
        }
    }
}
