package net.blay09.mods.unbreakables.client.hint;

import com.mojang.blaze3d.platform.Window;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.blay09.mods.unbreakables.rules.hint.MessageHint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;

public class MessageHintRenderer implements BreakHintRenderer<MessageHint> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Unbreakables.MOD_ID, "textures/gui/icons.png");

    @Override
    public void render(Window window, GuiGraphics guiGraphics, float partialTicks, MessageHint hint) {
        final var font = Minecraft.getInstance().font;
        final var textWidth = font.width(hint.component());
        final var x = window.getGuiScaledWidth() / 2 - 8 - textWidth / 2;
        final var y = window.getGuiScaledHeight() / 2 - 8 + 16;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, 16, 16, 256, 256);
        guiGraphics.drawString(font, hint.component(), x + 16 + 4, y + 8 - font.lineHeight / 2 + 1, 0xFFFFFFFF);
    }
}
