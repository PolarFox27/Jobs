package com.polarfox27.jobs.gui.screens;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.polarfox27.jobs.data.ClientInfos;
import com.polarfox27.jobs.gui.buttons.ButtonBack;
import com.polarfox27.jobs.gui.buttons.SlideBarXP;
import com.polarfox27.jobs.util.Constants;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.Constants.XPCategories;
import com.polarfox27.jobs.util.JobsMath;
import com.polarfox27.jobs.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiHowXP extends Screen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_how_xp.png");
    public int left;
    public int top;
    public final Job job;
    public int page = 0;
    public int pageNumber = 0;
    private List<XPCategories> categories = new ArrayList<>();
    private Map<Integer, List<ItemStack>> itemLists = new HashMap<>();
    private List<ITextComponent> tooltip = new ArrayList<>();

    public GuiHowXP(Job j)
    {
    	super(new StringTextComponent(""));
        this.job = j;
        this.page = 0;
        for(XPCategories c : XPCategories.getXPValues())
        {
        	if(!ClientInfos.getStacksFromCategories(c, this.job).isEmpty())
        	{
        		this.categories.add(c);
        	}
        }
        
        for (int i = 0; i < this.categories.size(); i ++)
        {
            List<ItemStack> stacks = ClientInfos.getStacksFromCategories(this.categories.get(i), this.job);
            this.itemLists.put(i, stacks);
        }
        for(Map.Entry<Integer, List<ItemStack>> entry : this.itemLists.entrySet())
        {
            if(entry.getValue().size()-4 > pageNumber)
                this.pageNumber = entry.getValue().size()-4;
        }
    }

    @Override
    public void init()
    {
        this.buttons.clear();
        this.addButton(new ButtonBack(this.width/2 - 83, this.height/2-70, this));
        this.addButton(new SlideBarXP(this.width/2 + 65, this.height/2 - 46, this));
    }
    
    @Override
    public boolean isPauseScreen() 
    {
    	return false;
    }

    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        this.tooltip.clear();
        this.left = this.width/2 - 88;
        this.top = this.height/2 - 75;
        Minecraft.getInstance().getTextureManager().bind(BACKGROUND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(mStack, this.left, this.top, 0, 0, 176, 150);//background

        super.render(mStack, mouseX, mouseY, partialTicks);
        this.drawCategories(mStack, mouseX, mouseY);
        this.drawCategoriesStacks(mStack, mouseX, mouseY);

        if(!tooltip.isEmpty())
            this.renderComponentTooltip(mStack, tooltip, mouseX, mouseY);
    }

    private void drawCategories(MatrixStack mStack, int mouseX, int mouseY)
    {
        Minecraft.getInstance().getTextureManager().bind(BACKGROUND);
        int size = this.categories.size()*30 + this.categories.size()-1;
        int x = this.left + 3  + 149/2 - size/2;
        int renderIndex = -1;
        for(int i = 0; i < this.categories.size(); i++)
        {
            this.blit(mStack, x + i*31 + 7, this.top + 20, this.categories.get(i).index*16, 150, 16, 16);
            if(mouseX >= x + i*31 + 7 && mouseX < x + i*31 + 23 && mouseY >= this.top + 20 && mouseY < this.top + 36)
                renderIndex = i;
            if(i < this.categories.size() - 1)
                this.blit(mStack, x + i*31 + 30, this.top + 20, 176, 0, 1, 126);
        }
        if(renderIndex != -1) this.tooltip.add(new TranslationTextComponent("category." + this.categories.get(renderIndex).name()));
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double direction) 
    {
    	int x;
        if (direction != 0)
        {
            x = -1 * Integer.signum((int)direction);
            this.page = JobsMath.clamp(this.page + x, 0, this.pageNumber-1);
            this.buttons.get(1).y = this.top + 29 + (int)(((double)this.page/(double) (this.pageNumber - 1))*91);
        }
        return true;
    }

    private void drawCategoriesStacks(MatrixStack mStack, int mouseX, int mouseY)
    {
        int size = this.categories.size()*30 + this.categories.size()-1;
        for(int i = 0; i < this.categories.size(); i++)
        {
            int x = this.left + 3  + 149/2 - size/2 + 7 + i*31;
            List<ItemStack> stacks = this.itemLists.get(i);
            RenderHelper.setupForFlatItems();

            int hoveredIndex = -1;
            for(int j = this.page; j < stacks.size() && j < this.page+5 && j >= 0; j++)
            {
                int y = this.top + 49 + (j - this.page)*18;
                ItemStack stack = stacks.get(j);
                if(stack.getItem() == Items.DIAMOND_SWORD && stack.hasTag())
                {
                	int scale = Constants.getClassScaleByName().get(stack.getOrCreateTag().getString("class"));
                    GuiJobInfos.drawEntityOnScreen(x + 8, y + 16, scale/2, this.width/2 - mouseX, this.height/2 - mouseY,
                            Constants.getEntityByName(stack.getOrCreateTag().getString("class"), Minecraft.getInstance().player.level));
                }
                else this.itemRenderer.renderGuiItem(stack, x, y);

                if(mouseX >= x && mouseX < x+16 && mouseY >= y && mouseY < y+16)
                    hoveredIndex = j;
            }
            if(hoveredIndex != -1) {
                ItemStack s = stacks.get(hoveredIndex);
                if(s.getItem() == Items.DIAMOND_SWORD && s.hasTag())
                    tooltip.add(new StringTextComponent(s.getOrCreateTag().getString("class")));

                else tooltip.add(new StringTextComponent(s.getDisplayName().getString().replace("[", "").replace("]", "")));
                long xp = 0L;
                int lvl = ClientInfos.job.getLevelByJob(this.job);
                if (lvl < 25)
                {
                    if(ClientInfos.CRAFT_ITEM_JOB.containsKey(s.getItem()))
                    {
                        xp = ClientInfos.CRAFT_ITEM_XP.get(s.getItem())[lvl];
                    }
                    else if(ClientInfos.SMELT_ITEM_JOB.containsKey(s.getItem()))
                    {
                        xp = ClientInfos.SMELT_ITEM_XP.get(s.getItem())[lvl];
                    }
                    else if(ClientInfos.BREAK_BLOCK_JOB.containsKey(Block.byItem(s.getItem())))
                    {
                        xp = ClientInfos.BREAK_BLOCK_XP.get(Block.byItem(s.getItem()))[lvl];
                    }
                    else if(ClientInfos.HARVEST_CROP_JOB.containsKey(s.getItem()))
                    {
                        xp = ClientInfos.HARVEST_CROP_XP.get(s.getItem())[lvl];
                    }
                    else if(s.hasTag() && s.getItem() == Items.DIAMOND_SWORD && s.getTag().contains("class"))
                    {
                        if(ClientInfos.KILL_ENTITY_JOB.containsKey(s.getOrCreateTag().getString("class")))
                            xp = ClientInfos.KILL_ENTITY_XP.get(s.getOrCreateTag().getString("class"))[lvl];
                    }
                    if(xp != 0L) tooltip.add(new StringTextComponent(TextFormatting.GREEN + "" + xp + " xp"));
                    else if(xp == 0L)
                    {
                        boolean toUnlock = false;
                        for(int j = lvl+1; j < 25; j++)
                        {
                            if(ClientInfos.CRAFT_ITEM_XP.containsKey(s.getItem()))
                                if(ClientInfos.CRAFT_ITEM_XP.get(s.getItem())[j] > 0)
                                {
                                    toUnlock = true;
                                    tooltip.add(new StringTextComponent(TextFormatting.RED + I18n.get("text.unlock_lvl") + " " + j));
                                    break;
                                }
                            if(ClientInfos.SMELT_ITEM_XP.containsKey(s.getItem()))
                                if(ClientInfos.SMELT_ITEM_XP.get(s.getItem())[j] > 0)
                                {
                                    toUnlock = true;
                                    tooltip.add(new StringTextComponent(TextFormatting.RED + I18n.get("text.unlock_lvl") + " " + j));
                                    break;
                                }
                            if(ClientInfos.BREAK_BLOCK_XP.containsKey(Block.byItem(s.getItem())))
                                if(ClientInfos.BREAK_BLOCK_XP.get(Block.byItem(s.getItem()))[j] > 0)
                                {
                                    toUnlock = true;
                                    tooltip.add(new StringTextComponent(TextFormatting.RED + I18n.get("text.unlock_lvl") + " " + j));
                                    break;
                                }
                            if(ClientInfos.HARVEST_CROP_XP.containsKey(s.getItem()))
                                if(ClientInfos.HARVEST_CROP_XP.get(s.getItem())[j] > 0)
                                {
                                    toUnlock = true;
                                    tooltip.add(new StringTextComponent(TextFormatting.RED + I18n.get("text.unlock_lvl") + " " + j));
                                    break;
                                }
                            if(s.getItem() == Items.DIAMOND_SWORD && s.hasTag())
                            {
                            	if(ClientInfos.KILL_ENTITY_XP.containsKey(s.getOrCreateTag().getString("class")))
                            		if(ClientInfos.KILL_ENTITY_XP.get(s.getOrCreateTag().getString("class"))[j] > 0)
                            		{
                            			toUnlock = true;
                            			tooltip.add(new StringTextComponent(TextFormatting.RED + I18n.get("text.unlock_lvl") + " " + j));
                            			break;
                            		}
                            }

                        }
                        if(!toUnlock)
                            tooltip.add(new StringTextComponent(TextFormatting.RED + "0 xp"));
                    }
                }
                else
                {
                    tooltip.add(new StringTextComponent(TextFormatting.DARK_PURPLE + "0 xp"));
                }
            }
            RenderHelper.setupFor3DItems();
        }
    }
}
