package net.blay09.mods.unbreakables;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static net.blay09.mods.unbreakables.Unbreakables.id;

public final class ModBlockTags {

    public static final TagKey<Block> DO_NOT_TRACK = TagKey.create(Registries.BLOCK, id("do_not_track"));
    public static final TagKey<Block> GROWTH = TagKey.create(Registries.BLOCK, id("growth"));

    private ModBlockTags() {
    }
}
