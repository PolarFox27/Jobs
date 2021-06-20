package com.polarfox27.jobs;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.polarfox27.jobs.events.client.GuiEvents;
import com.polarfox27.jobs.events.server.CommonEvents;
import com.polarfox27.jobs.util.handler.PacketHandler;
import com.polarfox27.jobs.util.handler.RegistryHandler;
import com.polarfox27.jobs.util.keybindings.KeyBindings;
import com.polarfox27.jobs.util.save.LoadUtil;

import net.minecraft.command.Commands;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("jobs")
public class ModJobs {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public ModJobs()
	{
		RegistryHandler.registerListeners();
		PacketHandler.registerPackets();
		info("Packets Registered", false);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void setup(final FMLCommonSetupEvent event)
	{
		
	}
	
	public void clientSetup(final FMLClientSetupEvent event)
	{
		KeyBindings.register();
		info("Keybindings Registered", false);
	}

	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event)
	{
		LoadUtil.loadData(event.getServer());
		info("Data Loaded", false);
		
	}
	
	public static void info(String message, boolean isError)
	{
		String msg = isError ? TextFormatting.RED + "[Jobs] " : TextFormatting.BLUE + "[Jobs] ";
		msg += message;
		System.out.println(msg);
	}
}
