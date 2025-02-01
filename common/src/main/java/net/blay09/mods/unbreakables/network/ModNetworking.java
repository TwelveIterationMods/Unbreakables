package net.blay09.mods.unbreakables.network;

import net.blay09.mods.balm.api.network.BalmNetworking;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerClientboundPacket(UnbreakableRulesMessage.TYPE, UnbreakableRulesMessage.class, UnbreakableRulesMessage::encode, UnbreakableRulesMessage::decode, UnbreakableRulesMessage::handle);
    }

}
