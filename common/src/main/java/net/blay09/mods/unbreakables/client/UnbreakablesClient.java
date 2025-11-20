package net.blay09.mods.unbreakables.client;

import net.blay09.mods.balm.client.platform.event.callback.RenderCallback;
import net.blay09.mods.unbreakables.BreakTracker;
import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.blay09.mods.unbreakables.api.client.UnbreakablesClientAPI;
import net.blay09.mods.unbreakables.client.hint.*;
import net.blay09.mods.unbreakables.mixin.MultiPlayerGameModeAccessor;
import net.blay09.mods.unbreakables.rules.hint.*;
import net.minecraft.client.Minecraft;

public class UnbreakablesClient {
    @SuppressWarnings("unchecked")
    public static void initialize() {
        UnbreakablesClientAPI.registerHintRenderer(MessageHint.ID, new MessageHintRenderer());
        UnbreakablesClientAPI.registerHintRenderer(CooldownHint.ID, new CooldownHintRenderer());
        UnbreakablesClientAPI.registerHintRenderer(ExperiencePointsHint.ID, new ExperiencePointsHintRenderer());
        UnbreakablesClientAPI.registerHintRenderer(ExperienceLevelHint.ID, new ExperienceLevelHintRenderer());
        UnbreakablesClientAPI.registerHintRenderer(CombinedHint.ID, new CombinedHintRenderer());
        UnbreakablesClientAPI.registerHintRenderer(ItemHint.ID, new ItemHintRenderer());

        RenderCallback.Gui.AFTER.register((guiGraphics, window) -> {
            final var player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }

            final var gameMode = Minecraft.getInstance().gameMode;
            if (gameMode == null || !gameMode.isDestroying()) {
                return;
            }

            BreakTracker.getContext(player, ((MultiPlayerGameModeAccessor) gameMode).getDestroyBlockPos()).ifPresent(context -> {
                final var requirement = context.resolve();
                requirement.hint(context, player).ifPresent(hint -> {
                    @SuppressWarnings("rawtypes") final var renderer = (BreakHintRenderer) BreakHintClientRegistry.getRenderer(hint.id());
                    if (renderer != null) {
                        final var partialTicks = Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks();
                        renderer.render(window, guiGraphics, partialTicks, hint);
                    }
                });
            });
        });
    }

}
