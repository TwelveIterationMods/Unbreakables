package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.RequirementType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemRequirementType implements RequirementType<ItemRequirement> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Unbreakables.MOD_ID, "item");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public ItemRequirement createInstance() {
        return new ItemRequirement(ItemStack.EMPTY, 0);
    }
}
