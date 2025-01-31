package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.minecraftforge.fml.common.Mod;

@Mod(Unbreakables.MOD_ID)
public class ForgeUnbreakables {

    public ForgeUnbreakables() {
        Balm.initialize(Unbreakables.MOD_ID, EmptyLoadContext.INSTANCE, Unbreakables::initialize);
    }

}
