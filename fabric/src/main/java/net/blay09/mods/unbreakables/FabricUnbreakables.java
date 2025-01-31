package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.fabricmc.api.ModInitializer;

public class FabricUnbreakables implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initialize(Unbreakables.MOD_ID, Unbreakables::initialize);
    }
}
