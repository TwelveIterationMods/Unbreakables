package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.shogi.common.effect.cost.ExperienceLevelCostInformation;
import net.blay09.mods.shogi.common.effect.cost.ExperiencePointsCostInformation;
import net.blay09.mods.shogi.common.effect.cost.ItemCostInformation;
import net.blay09.mods.shogi.common.effect.failure.FailureInformation;
import net.blay09.mods.shogi.common.effect.failure.RefusalInformation;
import net.blay09.mods.shogi.common.effect.server.cooldown.CooldownInformation;
import net.blay09.mods.shogi.effect.failure.ShogiDeferred;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShogiHintFactory {

    public static BreakHint<?> fromPayload(Object payload) {
        switch (payload) {
            case List<?> list -> {
                final List<BreakHint<?>> hints = new ArrayList<>();
                for (final var item : list) {
                    final var hint = fromPayload(item);
                    if (!(hint instanceof NoHint)) {
                        hints.add(hint);
                    }
                }
                if (hints.isEmpty()) {
                    return NoHint.INSTANCE;
                }
                if (hints.size() == 1) {
                    return hints.getFirst();
                }
                return new CombinedHint(hints);
            }
            case ExperiencePointsCostInformation info -> {
                return new ExperiencePointsHint(info.required());
            }
            case ExperienceLevelCostInformation info -> {
                return new ExperienceLevelHint(info.required());
            }
            case ItemCostInformation(
                    net.minecraft.core.HolderSet<net.minecraft.world.item.Item> item, int available, int required
            ) -> {
                final var itemStack = item.stream()
                        .findFirst()
                        .map(it -> new ItemStack(it.value()))
                        .orElse(ItemStack.EMPTY);
                return new ItemHint(itemStack, required, available >= required);
            }
            case CooldownInformation info -> {
                return new CooldownHint((int) Math.ceil(info.remainingTicks() / 20f));
            }
            case RefusalInformation(Component message1) -> {
                return new MessageHint(message1);
            }
            case FailureInformation(Component message1) -> {
                return new MessageHint(message1);
            }
            case Throwable throwable -> {
                final var message = throwable.getMessage() != null ? throwable.getMessage() : throwable.getClass().getSimpleName();
                return new MessageHint(Component.literal(message));
            }
            default -> {
            }
        }

        return NoHint.INSTANCE;
    }

    public static boolean isDeferredOnlyFailure(Object failure) {
        if (failure instanceof ShogiDeferred) {
            return true;
        }
        if (failure instanceof List<?> list && !list.isEmpty()) {
            return list.stream().allMatch(ShogiHintFactory::isDeferredOnlyFailure);
        }
        return false;
    }

    private ShogiHintFactory() {
    }
}
