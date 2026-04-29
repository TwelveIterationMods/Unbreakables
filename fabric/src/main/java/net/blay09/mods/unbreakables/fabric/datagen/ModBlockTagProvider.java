package net.blay09.mods.unbreakables.fabric.datagen;

import net.blay09.mods.unbreakables.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemIds;

import java.util.concurrent.CompletableFuture;

class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(ModBlockTags.DO_NOT_TRACK)
                .add(BlockIds.WATER)
                .add(BlockIds.LAVA)
                .add(BlockIds.FIRE)
                .add(BlockItemIds.BROWN_MUSHROOM)
                .add(BlockItemIds.RED_MUSHROOM)
                .add(BlockItemIds.VINE)
                .add(BlockItemIds.GLOW_BERRY_CROP)
                .add(BlockIds.CAVE_VINES_PLANT)
                .add(BlockItemIds.WEEPING_VINES)
                .add(BlockItemIds.TWISTING_VINES)
                .add(BlockItemIds.SUGAR_CANE)
                .add(BlockItemIds.KELP)
                .add(BlockItemIds.BAMBOO)
                .add(BlockItemIds.TORCHFLOWER_CROP)
                .add(BlockItemIds.FROGSPAWN)
                .add(BlockItemIds.SMALL_AMETHYST_BUD)
                .add(BlockItemIds.MEDIUM_AMETHYST_BUD)
                .add(BlockItemIds.LARGE_AMETHYST_BUD)
                .add(BlockItemIds.AMETHYST_CLUSTER)
                .add(BlockItemIds.SCULK_VEIN)
                .add(BlockItemIds.PALE_HANGING_MOSS);
    }
}
