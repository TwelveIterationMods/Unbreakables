package net.blay09.mods.unbreakables.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.unbreakables.Unbreakables;
import net.fabricmc.api.ClientModInitializer;

public class FabricUnbreakablesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BalmClient.initialize(Unbreakables.MOD_ID, UnbreakablesClient::initialize);
    }
}
