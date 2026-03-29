package net.blay09.mods.unbreakables.fabric.datagen;

import net.blay09.mods.unbreakables.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(ModBlockTags.DO_NOT_TRACK)
                .add(Blocks.WATER)
                .add(Blocks.LAVA)
                .add(Blocks.FIRE)
                .add(Blocks.BROWN_MUSHROOM)
                .add(Blocks.RED_MUSHROOM)
                .add(Blocks.VINE)
                .add(Blocks.CAVE_VINES)
                .add(Blocks.CAVE_VINES_PLANT)
                .add(Blocks.WEEPING_VINES)
                .add(Blocks.TWISTING_VINES)
                .add(Blocks.SUGAR_CANE)
                .add(Blocks.KELP)
                .add(Blocks.BAMBOO)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.FROGSPAWN)
                .add(Blocks.SMALL_AMETHYST_BUD)
                .add(Blocks.MEDIUM_AMETHYST_BUD)
                .add(Blocks.LARGE_AMETHYST_BUD)
                .add(Blocks.AMETHYST_CLUSTER)
                .add(Blocks.SCULK_VEIN)
                .add(Blocks.PALE_HANGING_MOSS);
    }
}
