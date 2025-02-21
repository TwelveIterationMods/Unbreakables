package net.blay09.mods.unbreakables.client.hint;

import com.mojang.blaze3d.platform.Window;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.blay09.mods.unbreakables.rules.hint.CooldownHint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CooldownHintRenderer implements BreakHintRenderer<CooldownHint> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Unbreakables.MOD_ID, "textures/gui/icons.png");

    @Override
    public void render(Window window, GuiGraphics guiGraphics, float partialTicks, CooldownHint hint) {
        if (hint.secondsLeft() > 0) {
            final var effectiveSecondsLeft = (int) Math.ceil(hint.secondsLeft() - hint.getTicksPassed() / 20f);
            final var font = Minecraft.getInstance().font;
            final var component = effectiveSecondsLeft > 0
                    ? Component.translatable("gui.unbreakables.cooldown", effectiveSecondsLeft)
                    : Component.translatable("gui.unbreakables.cooldown_ready");
            final var textWidth = font.width(component);
            final var x = window.getGuiScaledWidth() / 2 - 8 - textWidth / 2;
            final var y = window.getGuiScaledHeight() / 2 - 8 + 16;
            guiGraphics.blit(RenderType::guiTextured, TEXTURE, x, y, 0, 0, 16, 16, 256, 256);
            guiGraphics.drawString(font, component, x + 16 + 4, y + 8 - font.lineHeight / 2 + 1, 0xFFFFFFFF);
            hint.setTicksPassed(hint.getTicksPassed() + partialTicks);
        }
    }
}
