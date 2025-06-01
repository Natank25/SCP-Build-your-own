package io.github.natank25.scp_byo.networking;

import io.github.natank25.scp_byo.utils.ModConstants;
import net.minecraft.network.packet.CustomPayload;

public record UpdatePlayerDataPayload() implements CustomPayload {

    public static final CustomPayload.Id<UpdatePlayerDataPayload> ID = new CustomPayload.Id<>(ModConstants.Networking.UPDATE_PLAYER_DATA);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
