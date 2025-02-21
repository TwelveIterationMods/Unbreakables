package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public final class CooldownHint implements BreakHint<CooldownHint> {

    public static final ResourceLocation ID = new ResourceLocation(Unbreakables.MOD_ID, "cooldown");
    private final int secondsLeft;

    private float ticksPassed;

    public CooldownHint(int secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public Serializer<CooldownHint> serializer() {
        return CooldownHintSerializer.INSTANCE;
    }

    public int secondsLeft() {
        return secondsLeft;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CooldownHint) obj;
        return this.secondsLeft == that.secondsLeft;
    }

    @Override
    public int hashCode() {
        return Objects.hash(secondsLeft);
    }

    @Override
    public String toString() {
        return "CooldownHint[" +
                "secondsLeft=" + secondsLeft + ']';
    }

    public float getTicksPassed() {
        return ticksPassed;
    }

    public void setTicksPassed(float ticksPassed) {
        this.ticksPassed = ticksPassed;
    }


    public static class CooldownHintSerializer implements Serializer<CooldownHint> {

        public static final CooldownHintSerializer INSTANCE = new CooldownHintSerializer();

        @Override
        public void encode(FriendlyByteBuf buf, CooldownHint hint) {
            buf.writeInt(hint.secondsLeft());
        }

        @Override
        public CooldownHint decode(FriendlyByteBuf buf) {
            return new CooldownHint(buf.readInt());
        }
    }
}
