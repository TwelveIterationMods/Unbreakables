package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.CooldownTracker;
import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.blay09.mods.unbreakables.rules.hint.CooldownHint;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class CooldownRequirement implements BreakRequirement {

    private ResourceLocation id;
    private int seconds;

    public CooldownRequirement(ResourceLocation id, int seconds) {
        this.id = id;
        this.seconds = seconds;
    }

    @Override
    public boolean canAfford(BreakContext context, Player player) {
        return context.viaServer((level) -> getCooldownMillisLeft(player) <= 0);
    }

    @Override
    public void consume(Player player) {
        if (seconds > 0) {
            CooldownTracker.setCooldownUntil(player, id, System.currentTimeMillis() + seconds * 1000L);
        }
    }

    @Override
    public void rollback(Player player) {
        CooldownTracker.setCooldownUntil(player, id, 0);
    }

    @Override
    public Optional<BreakHint<?>> hint(BreakContext context, Player player) {
        final var secondsLeft = getCooldownMillisLeft(player) / 1000;
        return Optional.of(new CooldownHint((int) secondsLeft));
    }

    private long getCooldownMillisLeft(Player player) {
        return CooldownTracker.getCooldownMillisLeft(player, id);
    }

    @Override
    public boolean isEmpty() {
        return seconds <= 0;
    }

    public void setCooldown(ResourceLocation key, int seconds) {
        this.id = key;
        this.seconds = seconds;
    }

    public ResourceLocation getCooldownId() {
        return id;
    }

    public int getCooldownSeconds() {
        return seconds;
    }
}
