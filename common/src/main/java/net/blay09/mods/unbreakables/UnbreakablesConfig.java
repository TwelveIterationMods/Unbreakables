package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.reflection.Comment;
import net.blay09.mods.balm.platform.config.reflection.Config;
import net.blay09.mods.balm.platform.config.reflection.NestedType;

import java.util.ArrayList;
import java.util.List;

@Config(Unbreakables.MOD_ID)
public class UnbreakablesConfig {
    @Comment("IDs of inbuilt rulesets to enable. For example, \"waystones:generated_waystones\" makes all generated waystones breakable.")
    @NestedType(String.class)
    public List<String> rulesets = new ArrayList<>();

    @Comment("List of custom rules with comma-separated parameters in parentheses. Conditions can be defined as comma-separated list in square brackets. Will be applied in order.")
    @NestedType(String.class)
    public List<String> rules = new ArrayList<>();

    public static UnbreakablesConfig getActive() {
        return Balm.config().getActiveConfig(UnbreakablesConfig.class);
    }

    public static void initialize() {
        Balm.config().registerConfig(UnbreakablesConfig.class);
    }
}
