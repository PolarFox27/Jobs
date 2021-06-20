package com.polarfox27.jobs.gui.buttons;


import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.polarfox27.jobs.gui.screens.GuiHowXP;
import com.polarfox27.jobs.gui.screens.GuiJobInfos;
import com.polarfox27.jobs.util.Constants.XPCategories;
import com.polarfox27.jobs.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class ButtonXPCategory extends Button {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_job_infos.png");
    private final int xTexStart;
    private final int yTexStart;
    private final XPCategories category;
    private GuiJobInfos parent;

    public ButtonXPCategory(int x, int y, XPCategories categories, GuiJobInfos parent)
    {
        super(x, y, 80, 16, new StringTextComponent(""), new OnPressed());
        this.category = categories;
        this.xTexStart = 16 * this.category.index;
        this.yTexStart = this.category.isCategory ? 196 : 180;
        this.parent = parent;
    }

    public void setPosition(int xPos, int yPos)
    {
        this.x = xPos;
        this.y = yPos;
    }
    
    public void renderButton(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) 
    {
    	if (this.visible)
        {
            boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            Minecraft.getInstance().getTextureManager().bind(BACKGROUND);
            int i = this.xTexStart;
            int j = this.yTexStart;

            if(hovered) GL11.glColor4f(0.8F, 0.8F, 0.8F, 1.0F);
            else GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            this.blit(mStack, this.x, this.y, i, j, 16, 16);
            String txt = I18n.get("category." + this.category.name());
            int txtWidth = Minecraft.getInstance().font.width(txt);
            Minecraft.getInstance().font.draw(mStack, txt, this.x + 48 - txtWidth/2, this.y + 5, Color.BLACK.getRGB());
        }
    }
    
    public static class OnPressed implements IPressable{

		@Override
		public void onPress(Button btn) 
		{
			if(!(btn instanceof ButtonXPCategory)) return;
			ButtonXPCategory button = (ButtonXPCategory)btn;
			if(button.category == XPCategories.UNLOCK)
			{
				button.parent.offsetUnlock = button.parent.offsetUnlock == 0 ? -70 : 0;
				button.parent.update();
			}
			else if(button.category == XPCategories.XP)
				Minecraft.getInstance().setScreen(new GuiHowXP(button.parent.job));
		}
    	
    }
}
