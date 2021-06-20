package com.polarfox27.jobs.util.keybindings;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class KeyBindings {
	
	private static InputMappings.Input getKey(int key) 
	{
		return InputMappings.Type.KEYSYM.getOrCreate(key);
	}
	
	public static KeyBinding open_gui = new KeyBinding(Keys.OPEN_GUI, KeyConflictContext.IN_GAME, KeyModifier.NONE, getKey(GLFW.GLFW_KEY_J), Keys.CATEGORY);
	
	public static void register()
	{
		ClientRegistry.registerKeyBinding(KeyBindings.open_gui);
	}

}
