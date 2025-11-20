package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.RequirementType;
import net.minecraft.resources.Identifier;

public class ExperienceLevelRequirementType implements RequirementType<ExperienceLevelRequirement> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Unbreakables.MOD_ID, "experience_levels");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public ExperienceLevelRequirement createInstance() {
        return new ExperienceLevelRequirement(0);
    }
}
