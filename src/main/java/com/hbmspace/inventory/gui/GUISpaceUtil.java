package com.hbmspace.inventory.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class GUISpaceUtil {
    public static void pushScissor(Minecraft mc, int guiLeft, int guiTop, int ySize, int x, int y, int width, int height) {
        ScaledResolution res = new ScaledResolution(mc);
        int scale = res.getScaleFactor();

        // Note: Scissor is cut from the BOTTOM of the screen, so Y is inverted!
        // Note from Th3_Sl1ze: I don't really know if that can be replaced with smth else..
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((guiLeft + x) * scale, (guiTop + ySize - y - height) * scale, width * scale, height * scale);
    }

    public static void popScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
