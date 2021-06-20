package com.polarfox27.jobs.gui.buttons;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.polarfox27.jobs.gui.screens.GuiJobInfos;
import com.polarfox27.jobs.util.JobsMath;
import com.polarfox27.jobs.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class SlideBarUnlock extends Button {

    private final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID + ":textures/gui/gui_unlocked_items.png");
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffText;
    private final GuiJobInfos gui;

    public SlideBarUnlock(int index, int posX, int posY, GuiJobInfos gui)
    {
        super(posX, posY, 12, 15, new StringTextComponent(""), new OnPressed());
        this.xTexStart = 70;
        this.yTexStart = 0;
        this.yDiffText = 15;
        this.gui = gui;
    }

    @Override
    public void renderButton(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().getTextureManager().bind(this.texture);
            int j = this.yTexStart;

            if(hovered)
            {
                j += this.yDiffText;
                if(Minecraft.getInstance().mouseHandler.isLeftPressed())
                {
                    this.y = JobsMath.clamp(mouseY-7, this.gui.top + 14 +16, this.gui.top + 14 + 16 +105);
                    this.gui.page = (int) ((float)this.gui.pageNumber * ((float) (this.y - (this.gui.top + 14 +16))/ (float) 105));
                }
            }
            this.blit(mStack, this.x, this.y, this.xTexStart, j, 12, 15); //Icon
        }
    }
    
    public static class OnPressed implements IPressable{
    	@Override
		public void onPress(Button btn) {}    	
    }

}

