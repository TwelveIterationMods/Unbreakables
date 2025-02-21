package net.blay09.mods.unbreakables.api.client;

import com.mojang.blaze3d.platform.Window;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.client.gui.GuiGraphics;

public interface BreakHintRenderer<T extends BreakHint<T>> {
    void render(Window window, GuiGraphics guiGraphics, float partialTicks, T hint);
}
