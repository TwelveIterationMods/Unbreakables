package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.api.parameter.IntParameter;

public record BoundsParameter(IntParameter minX, IntParameter minY, IntParameter minZ, IntParameter maxX, IntParameter maxY, IntParameter maxZ) {
}
