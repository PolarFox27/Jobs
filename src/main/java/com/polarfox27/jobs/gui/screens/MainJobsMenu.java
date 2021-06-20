package com.polarfox27.jobs.gui.screens;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.polarfox27.jobs.gui.buttons.ButtonJob;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class MainJobsMenu extends Screen {

    public MainJobsMenu() 
    {
		super(new TranslationTextComponent("text.jobs.title"));
	}

	public static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/background.png");
    public static final ResourceLocation ICONS = new ResourceLocation(Reference.MOD_ID, "textures/gui/jobs_icons.png");
    
    @Override
    protected void init() 
    {
    	this.<ButtonJob>addButton(new ButtonJob(this.width/2 - 100, this.height/2 - 70, Job.byIndex(0)));
        this.<ButtonJob>addButton(new ButtonJob(this.width/2 - 100, this.height/2 - 30, Job.byIndex(1)));
        this.<ButtonJob>addButton(new ButtonJob(this.width/2 - 100, this.height/2 + 10, Job.byIndex(2)));
        this.<ButtonJob>addButton(new ButtonJob(this.width/2 - 100, this.height/2 + 50, Job.byIndex(3)));
    	super.init();
    }
    
    @Override
    public boolean isPauseScreen() 
    {
    	return false;
    }
    
    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) 
    {
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(BACKGROUND);
        this.blit(mStack, this.width/2 - 128, this.height/2 - 110, 0, 0, 256, 220);
        drawTitle(mStack);
    	super.render(mStack, mouseX, mouseY, partialTicks);
    }

    private void drawTitle(MatrixStack mStack)
    {
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        String title = I18n.get("text.jobs.title");
        int x = this.width/4 - this.font.width(title)/4 - 5;
        int y = this.height/2 - 114;
        this.font.draw(mStack, title, x, y, Color.black.getRGB());
        GL11.glPopMatrix();
    }
}
