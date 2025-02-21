package net.blay09.mods.unbreakables.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.GuiDrawEvent;
import net.blay09.mods.unbreakables.BreakTracker;
import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.blay09.mods.unbreakables.api.client.UnbreakablesClientAPI;
import net.blay09.mods.unbreakables.client.hint.*;
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

        Balm.getEvents().onEvent(GuiDrawEvent.Post.class, event -> {
            if (event.getElement() == GuiDrawEvent.Element.ALL) {
                final var player = Minecraft.getInstance().player;
                if (player == null) {
                    return;
                }

                BreakTracker.getContext(player).ifPresent(context -> {
                    final var requirement = context.resolve();
                    requirement.hint(context, player).ifPresent(hint -> {
                        @SuppressWarnings("rawtypes") final var renderer = (BreakHintRenderer) BreakHintClientRegistry.getRenderer(hint.id());
                        if (renderer != null) {
                            final var partialTicks = Minecraft.getInstance().getDeltaFrameTime();
                            renderer.render(event.getWindow(), event.getGuiGraphics(), partialTicks, hint);
                        }
                    });
                });
            }
        });
    }

}
