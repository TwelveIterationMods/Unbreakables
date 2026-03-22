package net.blay09.mods.unbreakables.client;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.blay09.mods.unbreakables.Unbreakables;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = Unbreakables.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeUnbreakablesClient {

    public NeoForgeUnbreakablesClient(ModContainer modContainer, IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modContainer, modEventBus);
        BalmClient.initializeMod(Unbreakables.MOD_ID, context, UnbreakablesClient::initialize);
    }
}
