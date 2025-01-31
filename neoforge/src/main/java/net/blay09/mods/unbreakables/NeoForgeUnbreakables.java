package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Unbreakables.MOD_ID)
public class NeoForgeUnbreakables {

    public NeoForgeUnbreakables(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        Balm.initialize(Unbreakables.MOD_ID, context, Unbreakables::initialize);
    }
}
