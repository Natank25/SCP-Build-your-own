package io.github.natank25.scp_byo.networking;

import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record MultiblockStructurePayload(List<MultiblockStructureData> structures) implements CustomPayload {
    public static final PacketCodec<? super RegistryByteBuf, MultiblockStructurePayload> CODEC =
            PacketCodec.of((payload, buf) -> {
                // Write the size of the list first
                buf.writeVarInt(payload.structures().size());
                // Write each structure
                for (MultiblockStructureData structureData : payload.structures()) {
                    // Write the identifier
                    buf.writeIdentifier(structureData.id());
                    // Write the structure template as NBT
                    NbtCompound nbt = new NbtCompound();
                    structureData.template().writeNbt(nbt);
                    buf.writeNbt(nbt);
                }
            }, buf -> {
                // Read the size of the list
                int size = buf.readVarInt();
                List<MultiblockStructureData> structures = new ArrayList<>(size);

                // Read each structure
                for (int i = 0; i < size; i++) {
                    // Read the identifier
                    Identifier id = buf.readIdentifier();
                    // Read the structure template from NBT
                    NbtCompound nbt = buf.readNbt();
                    StructureTemplate template = new StructureTemplate();
                    template.readNbt(buf.getRegistryManager().getOrThrow(RegistryKeys.BLOCK), nbt);

                    structures.add(new MultiblockStructureData(id, template));
                }

                return new MultiblockStructurePayload(structures);
            });
    private static final Identifier ID = Utils.newIdentifier("multiblock_structure_payload");
    public static final CustomPayload.Id<MultiblockStructurePayload> PACKET_ID = new CustomPayload.Id<>(ID);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public record MultiblockStructureData(Identifier id, StructureTemplate template){
    }
}
