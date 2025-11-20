package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.RequirementType;
import net.minecraft.resources.Identifier;

public class ExperiencePointsRequirementType implements RequirementType<ExperiencePointsRequirement> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Unbreakables.MOD_ID, "experience_points");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public ExperiencePointsRequirement createInstance() {
        return new ExperiencePointsRequirement(0);
    }
}
