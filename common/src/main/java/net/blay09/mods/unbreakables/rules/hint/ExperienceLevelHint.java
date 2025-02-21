package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ExperienceLevelHint(int levels) implements BreakHint<ExperienceLevelHint> {

    public static final ResourceLocation ID = new ResourceLocation(Unbreakables.MOD_ID, "xp_level");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public Serializer<ExperienceLevelHint> serializer() {
        return ExperienceLevelHintSerializer.INSTANCE;
    }

    public static class ExperienceLevelHintSerializer implements Serializer<ExperienceLevelHint> {

        public static final ExperienceLevelHintSerializer INSTANCE = new ExperienceLevelHintSerializer();

        @Override
        public void encode(FriendlyByteBuf buf, ExperienceLevelHint hint) {
            buf.writeInt(hint.levels());
        }

        @Override
        public ExperienceLevelHint decode(FriendlyByteBuf buf) {
            return new ExperienceLevelHint(buf.readInt());
        }
    }
}
