package net.blay09.mods.unbreakables.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.unbreakables.Unbreakables;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Unbreakables.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeUnbreakablesClient {

    public NeoForgeUnbreakablesClient(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        BalmClient.initialize(Unbreakables.MOD_ID, context, UnbreakablesClient::initialize);
    }
}
