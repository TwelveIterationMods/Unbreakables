package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

@Mod(Unbreakables.MOD_ID)
public class ForgeUnbreakables {

    public ForgeUnbreakables() {
        Balm.initialize(Unbreakables.MOD_ID, Unbreakables::initialize);
    }

}
