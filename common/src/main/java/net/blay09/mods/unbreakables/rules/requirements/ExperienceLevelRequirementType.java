package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.RequirementType;
import net.minecraft.resources.ResourceLocation;

public class ExperienceLevelRequirementType implements RequirementType<ExperienceLevelRequirement> {

    public static final ResourceLocation ID = new ResourceLocation(Unbreakables.MOD_ID, "experience_levels");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public ExperienceLevelRequirement createInstance() {
        return new ExperienceLevelRequirement(0);
    }
}
