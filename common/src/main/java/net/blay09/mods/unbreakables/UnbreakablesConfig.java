package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.reflection.Comment;
import net.blay09.mods.balm.platform.config.reflection.Config;
import net.blay09.mods.balm.platform.config.reflection.NestedType;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Config(Unbreakables.MOD_ID)
public class UnbreakablesConfig {
    public enum PlacedBlockTracking implements StringRepresentable {
        NONE,
        IN_STRUCTURE;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    @Comment("List of custom rules in Shogi format. Will be applied in order. For example, use('waystones:generated_waystones').")
    @NestedType(String.class)
    public List<String> rules = new ArrayList<>();

    @Comment("Which block placements should be tracked. Required for `is_generated` rule.")
    public PlacedBlockTracking placedBlockTracking = PlacedBlockTracking.IN_STRUCTURE;

    public static UnbreakablesConfig getActive() {
        return Objects.requireNonNull(Balm.config().getActiveConfig(UnbreakablesConfig.class));
    }

    public static void initialize() {
        Balm.config().registerConfig(UnbreakablesConfig.class);
    }
}
