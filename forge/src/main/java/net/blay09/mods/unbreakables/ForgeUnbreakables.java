package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.unbreakables.client.UnbreakablesClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Unbreakables.MOD_ID)
public class ForgeUnbreakables {

    public ForgeUnbreakables() {
        Balm.initialize(Unbreakables.MOD_ID, Unbreakables::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initialize(Unbreakables.MOD_ID, UnbreakablesClient::initialize));
    }

}
