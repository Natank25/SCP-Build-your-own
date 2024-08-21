package io.github.natank25.scp_byo.utils;

import net.minecraft.util.Identifier;

public class ModConstants {
	public enum Networking {
		;
		public static final Identifier GRANT_ADVANCEMENT_PACKET_ID = Utils.newIdentifier("grant_advancement");
		public static final Identifier MULTIBLOCK_UPDATE_PACKET_ID = Utils.newIdentifier("multiblock_update");
		public static final Identifier ALL_WORLD_DATA_PACKET_ID = Utils.newIdentifier("all_world_data");
		public static final Identifier REQUEST_WORLD_DATA_PACKET_ID = Utils.newIdentifier("request_world_data");
	}
}
