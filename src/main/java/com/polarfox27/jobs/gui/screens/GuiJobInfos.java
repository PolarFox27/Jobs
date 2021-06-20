package com.polarfox27.jobs.gui.screens;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.polarfox27.jobs.data.ClientInfos;
import com.polarfox27.jobs.gui.buttons.ButtonBack;
import com.polarfox27.jobs.gui.buttons.ButtonXPCategory;
import com.polarfox27.jobs.gui.buttons.SlideBarUnlock;
import com.polarfox27.jobs.util.Constants;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.Constants.XPCategories;
import com.polarfox27.jobs.util.JobsMath;
import com.polarfox27.jobs.util.Reference;

import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiJobInfos extends Screen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_job_infos.png");
    private static final ResourceLocation ICONES = new ResourceLocation(Reference.MOD_ID, "textures/gui/jobs_icons.png");
    private static final ResourceLocation UNLOCK_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_unlocked_items.png");
    public final Job job;
    public int left;
    public int top;
    public int offsetUnlock;
    private ItemRenderer renderItem;
    public int page;
    public final int pageNumber;
    private List<Item> unlocked_items = new ArrayList<>();
    public GuiJobInfos(Job job)
    {
    	super(new StringTextComponent(""));
        this.left = this.width/2 - 110;
        this.top = this.height/2 - 90;
        this.job = job;
        this.offsetUnlock = 0;
        this.renderItem = Minecraft.getInstance().getItemRenderer();
        this.page = 0;
        this.unlocked_items = ClientInfos.getClassedUnlockedItems(this.job);
        this.pageNumber = unlocked_items.size() <= 7 ? 1 : unlocked_items.size() - 6;
    }

    @Override
    public void init()
    {
        this.buttons.clear();
        this.<ButtonBack>addButton(new ButtonBack(this.width/2 - 105 + offsetUnlock, this.height/2-85, this));
        this.<ButtonXPCategory>addButton(new ButtonXPCategory(this.width/2 - 84 + offsetUnlock, this.height/2 + 40, XPCategories.XP, this));
        this.<ButtonXPCategory>addButton(new ButtonXPCategory(this.width/2 + 4 + offsetUnlock, this.height/2 + 40, XPCategories.UNLOCK, this));

        if(offsetUnlock == -70)
        {
            this.<SlideBarUnlock>addButton(new SlideBarUnlock(3, this.width/2 + offsetUnlock + 115 + 48, this.top + 14 + 16, this));
        }
        super.init();
    }
    
    public void update()
    {
    	this.buttons.get(0).x = this.width/2 - 105 + offsetUnlock;
    	this.buttons.get(1).x = this.width/2 - 84 + offsetUnlock;
    	this.buttons.get(2).x = this.width/2 + 4 + offsetUnlock;
    	if(offsetUnlock == -70)
    		this.<SlideBarUnlock>addButton(new SlideBarUnlock(3, this.width/2 + offsetUnlock + 115 + 48, this.top + 14 + 16, this));
    	else
    		this.buttons.remove(this.buttons.size()-1);
    }
    
    @Override
    public boolean isPauseScreen() 
    {
    	return false;
    }

    
    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) 
    {
    	super.render(mStack, mouseX, mouseY, partialTicks);
    	this.left = this.width/2 - 110 + offsetUnlock;
        this.top = this.height/2 - 90;
        Minecraft.getInstance().getTextureManager().bind(BACKGROUND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(mStack, this.left, this.top, 0, 0, 220, 180);//background
        this.drawGradients(mStack);

        if(offsetUnlock != 0)
        {
            Minecraft.getInstance().getTextureManager().bind(UNLOCK_BACKGROUND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.blit(mStack, this.left + 225, this.top + 14, 0, 0, 70, 152);
        }
        super.render(mStack, mouseX, mouseY, partialTicks);
        if(offsetUnlock != 0)
            this.drawUnlockedItems(mStack, mouseX, mouseY);
    }

    private void drawUnlockedItems(MatrixStack mStack, int mouseX, int mouseY)
    {
        RenderHelper.setupForFlatItems();
        int renderIndex = -1;
        for(int i = 0; i < (unlocked_items.size() >= 7 ? 7 : unlocked_items.size() - page); i++)
        {
            renderItem.renderGuiItem(new ItemStack(unlocked_items.get(i + page)), this.left + 242, this.top + 27 + i*18);
            if(mouseX >= this.left + 242      && mouseX < this.left + 258 &&
               mouseY >= this.top + 27 + i*18 && mouseY < this.top + 27 + 16 + i*18)
                renderIndex = i;
        }
        if(renderIndex != -1) renderComponentTooltip(mStack, getItemToolTip(new ItemStack(unlocked_items.get(renderIndex + page))), mouseX, mouseY);
        RenderHelper.setupFor3DItems();
    }

    private void drawGradients(MatrixStack mStack)
    {
        int lvl = ClientInfos.job.getLevelByJob(job);
        Minecraft.getInstance().getTextureManager().bind(ICONES);
        this.blit(mStack, this.width/2 - 20 + offsetUnlock, this.top + 7, 40*this.job.index, 0, 40, 40);//icon
        this.blit(mStack, this.width/2 - 20 + offsetUnlock, this.top + 7, 0, 40, 40, 40);//icon
        this.blit(mStack, this.width/2 - 75 + offsetUnlock, this.top + 65, 0, 80, 150, 12); //background1
        if(lvl < 25)
            this.blit(mStack, this.width/2 - 75 + offsetUnlock, this.top + 90, 0, 80, 150, 12);//background2

        int y = lvl < 25 ? 92 : 104;
        int size1 = lvl < 25 ? (int) (((double)ClientInfos.job.getXPByJob(job)/ (double)Constants.XP_BY_LEVEL[lvl + 1]) * 150) : 150;
        int size2 = lvl < 25 ? (int) (((double)ClientInfos.job.toTotalXPs()[job.index]/ (double)Constants.TOTAL_XP_BY_LEVEL[25]) * 150) : 0;
        String text1 = lvl < 25 ? ClientInfos.job.getXPByJob(job) + "/" + Constants.XP_BY_LEVEL[lvl + 1] : I18n.get("text.level.max");;
        String text2 = lvl < 25 ? ClientInfos.job.toTotalXPs()[job.index] + "/" + Constants.TOTAL_XP_BY_LEVEL[25] : "";
        String title1 = I18n.get("text.level") + " " + lvl;
        String title2 = lvl < 25 ? I18n.get("text.total_progression") : "";

        this.blit(mStack, this.width/2 - 75 + offsetUnlock, this.top + 65, 0, y, size1, 12);//gradient1
        this.blit(mStack, this.width/2 - 75 + offsetUnlock, this.top + 90, 0, y, size2, 12);//gradient2
        this.font.draw(mStack, text1, this.width/2 + offsetUnlock - this.font.width(text1)/2, this.top + 67, Color.WHITE.getRGB());
        this.font.draw(mStack, text2, this.width/2 + offsetUnlock - this.font.width(text2)/2, this.top + 92, Color.WHITE.getRGB());
        this.font.draw(mStack, title1, this.width/2 + offsetUnlock - this.font.width(title1)/2, this.top + 56, Color.BLACK.getRGB());
        this.font.draw(mStack, title2, this.width/2 + offsetUnlock - this.font.width(title2)/2, this.top + 81, Color.BLACK.getRGB());

    }

    public static void drawEntityOnScreen(int xOffset, int yOffset, int scale, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float)Math.atan((double)(mouseX / 40.0F));
        float f1 = (float)Math.atan((double)(mouseY / 40.0F));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)xOffset, (float)yOffset, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        matrixstack.mulPose(quaternion);
        float f2 = entity.yBodyRot;
        float f3 = entity.yRot;
        float f4 = entity.xRot;
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0F + f * 20.0F;
        entity.yRot = 180.0F + f * 40.0F;
        entity.xRot = -f1 * 20.0F;
        entity.yHeadRot = entity.yRot;
        entity.yHeadRotO = entity.yRot;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
           entityrenderermanager.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        });
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        entity.yBodyRot = f2;
        entity.yRot = f3;
        entity.xRot = f4;
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
        RenderSystem.popMatrix();
     }


    public List<ITextComponent> getItemToolTip(ItemStack stack)
    {
        List<ITextComponent> tooltip = new ArrayList<>();
        tooltip.add(new StringTextComponent(stack.getDisplayName().getString().replace("[", "").replace("]", "")));
        if(ClientInfos.CRAFT_UNLOCK_LVL.get(stack.getItem()) > ClientInfos.job.getLevelByJob(this.job))
        {
            tooltip.add(new StringTextComponent(TextFormatting.RED  + I18n.get("text.unlock_lvl") + " " + 
            									ClientInfos.CRAFT_UNLOCK_LVL.get(stack.getItem())));
        }
        else tooltip.add(new StringTextComponent(TextFormatting.GREEN  + I18n.get("text.unlock_craft")));

        return tooltip;
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double direction) 
    {
    	int x;
        if (direction != 0 && offsetUnlock == -70)
        {
            x = -1 * Integer.signum((int)direction);
            this.page = JobsMath.clamp(this.page + x, 0, this.pageNumber-1);
            this.buttons.get(3).y = this.top + 30 + (int)(((double)this.page/(double) (this.pageNumber - 1))*105);
        }
    	return true;
    }
}
