package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;

public class UnbreakablesConfig {
    public static UnbreakablesConfigData getActive() {
        return Balm.getConfig().getActive(UnbreakablesConfigData.class);
    }

    public static void initialize() {
        Balm.getConfig().registerConfig(UnbreakablesConfigData.class, null);
    }

}
