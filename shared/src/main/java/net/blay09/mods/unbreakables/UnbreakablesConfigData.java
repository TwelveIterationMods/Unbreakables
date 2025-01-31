package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;
import net.blay09.mods.balm.api.config.ExpectedType;

import java.util.ArrayList;
import java.util.List;

@Config(Unbreakables.MOD_ID)
public class UnbreakablesConfigData implements BalmConfigData {
    @Comment("IDs of inbuilt rulesets to enable. For example, \"waystones:generated_waystones\" makes all generated waystones unbreakable.")
    @ExpectedType(String.class)
    public List<String> rulesets = new ArrayList<>();

    @Comment("Custom rules.")
    @ExpectedType(String.class)
    public List<String> rules = new ArrayList<>();
}
