package com.polarfox27.jobs.util.handler;

import com.polarfox27.jobs.ModJobs;
import com.polarfox27.jobs.commands.CommandAdd;
import com.polarfox27.jobs.commands.CommandInfo;
import com.polarfox27.jobs.commands.CommandSet;
import com.polarfox27.jobs.events.client.GuiEvents;
import com.polarfox27.jobs.events.server.BreakBlockEvents;
import com.polarfox27.jobs.events.server.CommonEvents;
import com.polarfox27.jobs.events.server.CraftItemEvents;
import com.polarfox27.jobs.events.server.KillEntityEvent;

import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.serializers.BrigadierSerializers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RegistryHandler {
	
	@SubscribeEvent
	public void onCommandsRegistered(RegisterCommandsEvent event)
	{		
		CommandInfo.register(event.getDispatcher());
		//CommandSet.register(event.getDispatcher());
		//CommandAdd.register(event.getDispatcher());
		ModJobs.info("Commands Registered", false);
	}
	
	public static void registerListeners()
	{
		MinecraftForge.EVENT_BUS.register(new GuiEvents());
		MinecraftForge.EVENT_BUS.register(new RegistryHandler());
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
		MinecraftForge.EVENT_BUS.register(new BreakBlockEvents());
		MinecraftForge.EVENT_BUS.register(new KillEntityEvent());
		MinecraftForge.EVENT_BUS.register(new CraftItemEvents());
	}

}
