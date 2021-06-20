package com.polarfox27.jobs.gui.screens;



import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.polarfox27.jobs.data.ClientInfos;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;


public class GuiLevelUp extends Screen {

    public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_level_up.png");
    public static final ResourceLocation ICONS = new ResourceLocation(Reference.MOD_ID, "textures/gui/jobs_icons.png");
    private final Job job;

    public GuiLevelUp(Job job)
    {
    	super(new StringTextComponent(""));
        this.job = job;
    }
    
    @Override
    public boolean isPauseScreen() 
    {
    	return false;
    }

    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        if(this.font == null) return;
        Minecraft.getInstance().getTextureManager().bind(TEXTURES);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(mStack, this.width/2 - 88, this.height/2 - 75, 0, 0, 176, 150); //background

        Minecraft.getInstance().getTextureManager().bind(ICONS);
        int textY = ClientInfos.job.getLevelByJob(this.job) >= 25 ? 12 : 0;
        this.blit(mStack, this.width/2 - 75, this.height/2 - 30, 0, 92 + textY, 150, 12); //gradient

        blit(mStack, this.width/2 -15, this.height/2 - 66, this.job.index*40, 0,
                40, 40, 30, 30, 256, 256);
        blit(mStack, this.width/2 -15, this.height/2 - 66, 0, 40,
                40, 40, 30, 30, 256, 256);//icon

        String lvl = I18n.get("text.level") + " " + ClientInfos.job.getLevelByJob(job);
        this.font.draw(mStack, lvl, this.width/2 - this.font.width(lvl)/2, this.height/2 - 27, Color.white.getRGB());
        String unlock = I18n.get("text.unlocked");
        this.font.draw(mStack, unlock, this.width/2 - this.font.width(unlock)/2, this.height/2, Color.black.getRGB());
        this.drawUnlockedStacks(mStack, mouseX, mouseY);
        String reward = I18n.get("text.rewards");
        this.font.draw(mStack, reward, this.width/2 - this.font.width(unlock)/2, this.height/2 + 34, Color.black.getRGB());
        this.drawRewardStacks(mStack, mouseX, mouseY);
        super.render(mStack, mouseX, mouseY, partialTicks);
    }


    private void drawUnlockedStacks(MatrixStack mStack, int mouseX, int mouseY)
    {
        RenderHelper.setupForFlatItems();
        List<ItemStack> stacks = new ArrayList<>();
        int hovered = -1;
        for(Item i : ClientInfos.CRAFT_UNLOCK_LVL.keySet())
        {
            if(ClientInfos.CRAFT_UNLOCK_JOB.get(i) == this.job && ClientInfos.CRAFT_UNLOCK_LVL.get(i) == ClientInfos.job.getLevelByJob(this.job))
            {
                ItemStack s = new ItemStack(i);
                stacks.add(s);
            }
        }
        int y = this.height/2 + 11;
        int sizeX = stacks.size()*16 + (stacks.size()-1)*6;
        int x = this.width/2 - sizeX/2;
        for(int i = 0; i < stacks.size(); i++)
        {
            this.itemRenderer.renderGuiItem(stacks.get(i), x + i*22, y);
            if(mouseX >= x + i*22 && mouseX < x + i*22 + 16 && mouseY >= y && mouseY < y + 16)
                hovered = i;
        }
        if(hovered != -1)
            this.renderToolTip(mStack, stacks.get(hovered), mouseX, mouseY);
        RenderHelper.setupFor3DItems();
    }

    private void drawRewardStacks(MatrixStack mStack, int mouseX, int mouseY)
    {
        RenderHelper.setupForFlatItems();
        List<ItemStack> stacks = ClientInfos.CURRENT_REWARDS;
        int hovered = -1;

        int y = this.height/2 + 48;
        int sizeX = stacks.size()*16 + (stacks.size()-1)*6;
        int x = this.width/2 - sizeX/2;
        for(int i = 0; i < stacks.size(); i++)
        {
            this.itemRenderer.renderGuiItem(stacks.get(i).copy(), x + i*22, y);
            if(mouseX >= x + i*22 && mouseX < x + i*22 + 16 && mouseY >= y && mouseY < y + 16)
                hovered = i;
        }
        if(hovered != -1)
            this.renderToolTipAndCount(mStack, stacks.get(hovered), mouseX, mouseY);
        RenderHelper.setupFor3DItems();
    }


    private void renderToolTip(MatrixStack mStack, ItemStack stack, int x, int y)
    {
        List<ITextComponent> tooltips = new ArrayList<>();
        FontRenderer font = stack.getItem().getFontRenderer(stack);

        tooltips.add(new StringTextComponent(stack.getDisplayName().getString().replace("[", "").replace("]", "")));

        if(ClientInfos.CRAFT_UNLOCK_JOB.containsKey(stack.getItem()))
            tooltips.add(new StringTextComponent(TextFormatting.GREEN + I18n.get("text.unlock_craft")));

        this.renderComponentTooltip(mStack, tooltips, x, y);
    }

    protected void renderToolTipAndCount(MatrixStack mStack, ItemStack stack, int x, int y)
    {
        List<ITextComponent> tooltips = new ArrayList<>();
        FontRenderer font = stack.getItem().getFontRenderer(stack);

        tooltips.add(new StringTextComponent(stack.getDisplayName().getString().replace("[", "").replace("]", "")));
        tooltips.add(new StringTextComponent(TextFormatting.GREEN + "" + stack.getCount()));

        this.renderComponentTooltip(mStack, tooltips, x, y);
    }
}
