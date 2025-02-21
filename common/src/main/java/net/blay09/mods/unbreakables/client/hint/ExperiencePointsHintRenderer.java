package net.blay09.mods.unbreakables.client.hint;

import com.mojang.blaze3d.platform.Window;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.blay09.mods.unbreakables.rules.hint.ExperiencePointsHint;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ExperiencePointsHintRenderer implements BreakHintRenderer<ExperiencePointsHint> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Unbreakables.MOD_ID, "textures/gui/icons.png");

    @Override
    public void render(Window window, GuiGraphics guiGraphics, float partialTicks, ExperiencePointsHint hint) {
        final var player = Minecraft.getInstance().player;
        final var font = Minecraft.getInstance().font;
        final var canAfford = player.totalExperience >= hint.points();
        final var component = Component.translatable("gui.unbreakables.xp_points", hint.points())
                .withStyle(canAfford ? ChatFormatting.GREEN : ChatFormatting.RED);
        final var textWidth = font.width(component);
        final var x = window.getGuiScaledWidth() / 2 - 8 - textWidth / 2;
        final var y = window.getGuiScaledHeight() / 2 - 8 + 16;
        guiGraphics.blit(RenderType::guiTextured, TEXTURE, x, y, canAfford ? 16 : 32, 0, 16, 16, 256, 256);
        guiGraphics.drawString(font, component, x + 16 + 4, y + 8 - font.lineHeight / 2 + 1, 0xFFFFFFFF);
    }
}
