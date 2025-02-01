package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.RequirementType;
import net.minecraft.resources.ResourceLocation;

public class ExperiencePointsRequirementType implements RequirementType<ExperiencePointsRequirement> {

    public static final ResourceLocation ID = new ResourceLocation(Unbreakables.MOD_ID, "experience_points");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public ExperiencePointsRequirement createInstance() {
        return new ExperiencePointsRequirement(0);
    }
}
