package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.fabricmc.api.ModInitializer;

public class FabricUnbreakables implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initializeMod(Unbreakables.MOD_ID, FabricLoadContext.INSTANCE, Unbreakables::initialize);
    }
}
