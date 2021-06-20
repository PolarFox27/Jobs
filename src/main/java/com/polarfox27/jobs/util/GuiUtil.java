package com.polarfox27.jobs.util;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiUtil {
	
	public static void drawTexture(MatrixStack mStack, AbstractGui gui, int x, int y, int textX, int textY, int width, int height)
	{
		gui.blit(mStack, x, y, textX, textY, width, height);
	}
	
	
	public static void drawScaledTexture(MatrixStack mStack, int x, int y, float u, float v, int uWidth, int vHeight, int width, int height)
    {
		Matrix4f matrix = mStack.last().pose();
        float f = 1.0F / 256.0f;
        float f1 = 1.0F / 256.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(matrix, (float)x, (float)(y + height), 0.0F).uv((float)(u * f), (float)((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.vertex(matrix, (float)(x + width), (float)(y + height), 0.0F).uv((float)((u + (float)uWidth) * f), (float)((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.vertex(matrix, (float)(x + width), (float)y, 0.0F).uv((float)((u + (float)uWidth) * f), (float)(v * f1)).endVertex();
        bufferbuilder.vertex(matrix, (float)x, (float)y, 0.0F).uv((float)(u * f), (float)(v * f1)).endVertex();
        tessellator.end();
    }

}
