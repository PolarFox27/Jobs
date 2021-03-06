package com.dorian2712.jobs.events.client;

import com.dorian2712.jobs.data.ClientInfos;
import com.dorian2712.jobs.gui.screens.MainJobsMenu;
import com.dorian2712.jobs.util.keybindings.KeyBindings;
import com.dorian2712.jobs.util.keybindings.Keys.Key;
import com.dorian2712.jobs.network.PacketAskClientUpdate;
import com.dorian2712.jobs.util.handlers.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

@EventBusSubscriber
public class KeyBindingsEvent {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void handleKeyBindings(KeyInputEvent event)
    {
        Key keyPressed = getPressedKey();

        if(keyPressed == Key.OPEN_GUI)
        {
            if(Minecraft.getMinecraft().currentScreen == null)
            {
                PacketHandler.INSTANCE.sendToServer(new PacketAskClientUpdate());
                Minecraft.getMinecraft().displayGuiScreen(new MainJobsMenu());
            }
        }

    }


    private static Key getPressedKey()
    {
        if(KeyBindings.open_gui.isPressed())
            return Key.OPEN_GUI;

        else return Key.NONE;
    }
}
