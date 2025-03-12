package net.blay09.mods.unbreakables.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.unbreakables.Unbreakables;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.allowServerOnly(Unbreakables.MOD_ID);

        networking.registerClientboundPacket(UnbreakableRulesMessage.TYPE, UnbreakableRulesMessage.class, UnbreakableRulesMessage.STREAM_CODEC, UnbreakableRulesMessage::handle);
        networking.registerClientboundPacket(ClientboundUnbreakableStatusPacket.TYPE, ClientboundUnbreakableStatusPacket.class, ClientboundUnbreakableStatusPacket.STREAM_CODEC, ClientboundUnbreakableStatusPacket::handle);
    }

}
