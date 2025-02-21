package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ExperiencePointsHint(int points) implements BreakHint<ExperiencePointsHint> {

    public static final ResourceLocation ID = new ResourceLocation(Unbreakables.MOD_ID, "xp_points");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public Serializer<ExperiencePointsHint> serializer() {
        return ExperiencePointsHintSerializer.INSTANCE;
    }

    public static class ExperiencePointsHintSerializer implements Serializer<ExperiencePointsHint> {

        public static final ExperiencePointsHintSerializer INSTANCE = new ExperiencePointsHintSerializer();

        @Override
        public void encode(FriendlyByteBuf buf, ExperiencePointsHint hint) {
            buf.writeInt(hint.points());
        }

        @Override
        public ExperiencePointsHint decode(FriendlyByteBuf buf) {
            return new ExperiencePointsHint(buf.readInt());
        }
    }
}
