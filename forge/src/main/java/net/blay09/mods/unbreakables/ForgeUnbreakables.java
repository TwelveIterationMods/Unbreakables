package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.unbreakables.client.UnbreakablesClient;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Unbreakables.MOD_ID)
public class ForgeUnbreakables {

    public ForgeUnbreakables(FMLJavaModLoadingContext context) {
        final var loadContext = new ForgeLoadContext(context.getModEventBus());
        Balm.initialize(Unbreakables.MOD_ID, loadContext, Unbreakables::initialize);
        if (FMLEnvironment.dist.isClient()) {
            BalmClient.initialize(Unbreakables.MOD_ID, loadContext, UnbreakablesClient::initialize);
        }
    }

}
