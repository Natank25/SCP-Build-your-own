package io.github.natank25.scp_byo.networking;

import io.github.natank25.scp_byo.utils.ModConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record GrantAdvancementPayload(String criterion, Identifier id) implements CustomPayload {
    public static final CustomPayload.Id<GrantAdvancementPayload> ID = new CustomPayload.Id<>(ModConstants.Networking.GRANT_ADVANCEMENT_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, GrantAdvancementPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, GrantAdvancementPayload::criterion,
            Identifier.PACKET_CODEC, GrantAdvancementPayload::id,
            GrantAdvancementPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
