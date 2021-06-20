package com.polarfox27.jobs.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.polarfox27.jobs.data.ClientInfos;
import com.polarfox27.jobs.util.Constants;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ClientBossInfo;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiGainXP extends AbstractGui {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_gain_xp.png");

    private Job job;
    private long xp;
    public GuiGainXP(Job job, long xpAdded)
    {
        this.job = job;
        this.xp = xpAdded;
    }
    
    public void render(MatrixStack mStack, float partialTicks)
    {
    	GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(TEXTURE);
        int render_width = Minecraft.getInstance().getWindow().getGuiScaledWidth();

        long xp_progression = ClientInfos.job.getXPByJob(job);
        long total = Constants.XP_BY_LEVEL[ClientInfos.job.getLevelByJob(job)+1];
        int width = (int)(150 * ((double)xp_progression /(double)total));
        String title = TextFormatting.WHITE + I18n.get("jobs." + job.name) + " (lvl " + ClientInfos.job.getLevelByJob(job) + ") : " +
                TextFormatting.AQUA + "+" + xp + TextFormatting.WHITE + " xp";
        String xpTotal = xp_progression + "/" + total;
        int titleWidth = Minecraft.getInstance().font.width(title);
        int xpTotalWidth = Minecraft.getInstance().font.width(xpTotal);

        this.blit(mStack, render_width/2 - 90, 5, 0, 0, 180, 50);//background
        this.blit(mStack, render_width/2 - 75, 35, 0, 50, 150, 12);//progressbackground
        this.blit(mStack, render_width/2 - 75, 35, 0, 62, width, 12);//progressbar
        Minecraft.getInstance().font.draw(mStack, title, render_width/2 - titleWidth/2, 15, Color.white.getRGB());
        Minecraft.getInstance().font.draw(mStack, xpTotal, render_width/2 - xpTotalWidth/2, 38, Color.black.getRGB());
        GL11.glPopMatrix();
    }


    public static class GuiAddXpInfos{
        public long xpAdded;
        public Job job;
        public long ticks;

        public GuiAddXpInfos(Job j, long xp)
        {
            this.job = j;
            this.xpAdded = xp;
            ticks = System.currentTimeMillis() + 5000;
        }
    }

}
