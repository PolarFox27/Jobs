package com.polarfox27.jobs.events.client;


import java.util.Arrays;

import com.polarfox27.jobs.data.ClientInfos;
import com.polarfox27.jobs.gui.screens.MainJobsMenu;
import com.polarfox27.jobs.network.PacketAskClientUpdate;
import com.polarfox27.jobs.util.handler.PacketHandler;
import com.polarfox27.jobs.util.keybindings.KeyBindings;
import com.polarfox27.jobs.util.keybindings.Keys.Key;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class KeyBindingsEvent {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void handleKeyBindings(KeyInputEvent event)
    {
        Key keyPressed = getPressedKey();
        if(keyPressed == Key.OPEN_GUI)
        {
            if(Minecraft.getInstance().screen == null)
            {
                PacketHandler.INSTANCE.sendToServer(new PacketAskClientUpdate());
                Minecraft.getInstance().setScreen(new MainJobsMenu());
            }
        }

    }


    private static Key getPressedKey()
    {
        if(KeyBindings.open_gui.isDown())
            return Key.OPEN_GUI;

        else return Key.NONE;
    }
}
