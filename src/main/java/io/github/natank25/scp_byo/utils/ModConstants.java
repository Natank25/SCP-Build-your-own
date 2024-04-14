package io.github.natank25.scp_byo.utils;

import net.minecraft.util.Identifier;

public class ModConstants {
	public enum Networking {
		;
		public static final Identifier GRANT_ADVANCEMENT_PACKET_ID = Utils.newIdentifier("grant_advancement");
		public static final Identifier DESTROY_MULTIBLOCK_PACKET_ID = Utils.newIdentifier("destroy_multiblock");
	}
}
