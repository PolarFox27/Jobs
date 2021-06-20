package com.polarfox27.jobs.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.polarfox27.jobs.data.JobsInfo;
import com.polarfox27.jobs.data.PlayerData;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.handler.PacketHandler;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandInfo {
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("jobs-info")
				.requires((source) -> {return source.hasPermission(2);})
				.executes((ctx) -> 
					{
						if(!(ctx.getSource().getEntity() instanceof ServerPlayerEntity)) return 0;
						info(ctx.getSource(), (ServerPlayerEntity)ctx.getSource().getEntity());
						return 0;
					})
				.then(Commands.argument("target", EntityArgument.player()).executes((ctx)->
					{
						info(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"));
						return 0;
					})
				));
	}
	
	
	private static void info(CommandSource source, ServerPlayerEntity target)
	{
		if(!(source.getEntity() instanceof ServerPlayerEntity)) return;
		ServerPlayerEntity sender = (ServerPlayerEntity)source.getEntity();
		JobsInfo infos = PlayerData.getPlayerJobs(target);
		if(sender.getGameProfile().getId().equals(target.getGameProfile().getId()))
			PacketHandler.sendMessageToClient(sender, new StringTextComponent(TextFormatting.BLUE + "Your Stats"));
		else
			PacketHandler.sendMessageToClient(sender, new StringTextComponent(TextFormatting.LIGHT_PURPLE + "Stats of " + TextFormatting.BLUE + target.getName().getString()));
        for(int i = 0; i < 4; i++)
        {
            int lvl = infos.getLevelByJob(Job.byIndex(i));
            long xp = infos.getXPByJob(Job.byIndex(i));
            PacketHandler.sendMessageToClient(sender, new StringTextComponent(TextFormatting.LIGHT_PURPLE + Job.byIndex(i).name() + " : lvl " +
                    TextFormatting.BLUE + lvl + TextFormatting.LIGHT_PURPLE + ", xp " + TextFormatting.BLUE + xp));
        }
	}

}
