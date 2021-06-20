package com.polarfox27.jobs.gui.buttons;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.polarfox27.jobs.data.ClientInfos;
import com.polarfox27.jobs.gui.screens.GuiJobInfos;
import com.polarfox27.jobs.util.Constants;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.GuiUtil;
import com.polarfox27.jobs.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class ButtonJob extends Button {

    private final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID + ":textures/gui/jobs_icons.png");
    private final int xTexStart;
    private final int yTexStart;
    private final String title;
    private final Job job;

    public ButtonJob(int posX, int posY, Job j)
    {
        super(posX, posY, 200, 40, new StringTextComponent(""),new OnPressed());
        this.xTexStart = 40 * j.index;
        this.yTexStart = 216;
        this.title = I18n.get("jobs." + j.name);
        this.job = j;
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    
    @Override
    public void renderButton(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) 
    {
    	if (this.visible)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().getTextureManager().bind(this.texture);
            drawIcon(mStack);
            drawGradient(mStack);
            drawName(mStack);

        }
    }

    private void drawIcon(MatrixStack mStack)
    {
        int i = this.xTexStart;
        int j = this.yTexStart;
        GuiUtil.drawScaledTexture(mStack, this.x, this.y, i, j, 40, 40, 30, 30);
    }

    private void drawGradient(MatrixStack mStack)
    {
        long xp = ClientInfos.job.getXPByJob(this.job);
        int lvl = ClientInfos.job.getLevelByJob(this.job);
        if(lvl < 25)
        {
            long total = Constants.XP_BY_LEVEL[lvl+1];
            int size = (int)(150*((double)xp / (double)total));
            GuiUtil.drawTexture(mStack, this, this.x + 45, this.y + 15, 0, 80, 150, 12); //background
            GuiUtil.drawTexture(mStack, this, this.x + 45, this.y + 15, 0, 92, size, 12);

            String info = xp + "/" + total;
            int widthInfo = Minecraft.getInstance().font.width(info);
            Minecraft.getInstance().font.draw(mStack, info, this.x + 120 - widthInfo/2, this.y + 18,
                    Color.white.getRGB());
        }
        else
        {
            int size = 150;
            this.blit(mStack, this.x + 45, this.y + 15, 0, 80, 150, 12); //background
            this.blit(mStack, this.x + 45, this.y + 15, 0, 104, size, 12);

            String info = I18n.get("text.level.max");
            int widthInfo = Minecraft.getInstance().font.width(info);
            Minecraft.getInstance().font.draw(mStack, info, this.x + 120 - widthInfo/2, this.y + 18,
                    Color.white.getRGB());
        }
    }

    private void drawName(MatrixStack mStack)
    {
        int lvl = ClientInfos.job.getLevelByJob(this.job);
        String name = this.title + " (" + I18n.get("text.level") + " " + lvl + ")";
        int x = 120 - Minecraft.getInstance().font.width(name)/2;
        int y = 2;
        Minecraft.getInstance().font.draw(mStack, name, this.x + x, this.y + y, Color.black.getRGB());
    }
    
    public static class OnPressed implements IPressable{

		@Override
		public void onPress(Button btn) 
		{
			if(!(btn instanceof ButtonJob)) return;
			Minecraft.getInstance().setScreen(new GuiJobInfos(((ButtonJob)btn).job));
		}
    	
    }
}
