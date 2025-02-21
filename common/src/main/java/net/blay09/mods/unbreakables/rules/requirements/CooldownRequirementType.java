package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.RequirementType;
import net.minecraft.resources.ResourceLocation;

public class CooldownRequirementType implements RequirementType<CooldownRequirement> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Unbreakables.MOD_ID, "cooldown");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public CooldownRequirement createInstance() {
        return new CooldownRequirement(ID, 0);
    }
}
