package io.github.natank25.scp_byo.events;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.block.multiblockBAK.Multiblocks;
import io.github.natank25.scp_byo.networking.MultiblockStructurePayload;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModEvents {

    public static void registerEvents() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            StructureTemplateManager structureTemplateManager = Objects.requireNonNull(player.getServer()).getStructureTemplateManager();
            List<MultiblockStructurePayload.MultiblockStructureData> structureList = new ArrayList<>();
            structureTemplateManager.streamTemplates().forEach(identifier -> {
                if (!Objects.equals(identifier.getNamespace(), Scp_byo.MOD_ID) || !identifier.getPath().startsWith(Multiblocks.MULTIBLOCK_PATH))
                    return;
                StructureTemplate structureTemplate = structureTemplateManager.getTemplateOrBlank(identifier);
                structureList.add(new MultiblockStructurePayload.MultiblockStructureData(identifier, structureTemplate));
            });
            NetworkManager.sendToPlayer(player, new MultiblockStructurePayload(structureList));
        });
    }
}
