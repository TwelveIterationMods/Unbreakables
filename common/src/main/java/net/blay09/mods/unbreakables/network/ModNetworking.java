package net.blay09.mods.unbreakables.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.unbreakables.Unbreakables;
import net.minecraft.resources.ResourceLocation;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerClientboundPacket(id("unbreakable_rules"), UnbreakableRulesMessage.class, UnbreakableRulesMessage::encode, UnbreakableRulesMessage::decode, UnbreakableRulesMessage::handle);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(Unbreakables.MOD_ID, path);
    }
}
