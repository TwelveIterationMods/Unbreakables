package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.RequirementType;
import net.minecraft.resources.Identifier;

public class CooldownRequirementType implements RequirementType<CooldownRequirement> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Unbreakables.MOD_ID, "cooldown");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public CooldownRequirement createInstance() {
        return new CooldownRequirement(ID, 0);
    }
}
