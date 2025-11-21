package net.blay09.mods.unbreakables.client;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.blay09.mods.unbreakables.Unbreakables;
import net.fabricmc.api.ClientModInitializer;

public class FabricUnbreakablesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BalmClient.initializeMod(Unbreakables.MOD_ID, FabricLoadContext.INSTANCE, UnbreakablesClient::initialize);
    }
}
