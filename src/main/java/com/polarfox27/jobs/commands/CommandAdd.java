package com.polarfox27.jobs.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.polarfox27.jobs.data.PlayerData;
import com.polarfox27.jobs.network.PacketUpdateClientJob;
import com.polarfox27.jobs.util.Constants;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.handler.PacketHandler;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;

public class CommandAdd {
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("jobs-add").requires((source)-> source.hasPermission(2))
			.then(Commands.argument("target", EntityArgument.player())).then(Commands.argument("job", IntegerArgumentType.integer(0, 3)))
			.then(Commands.argument("xp", LongArgumentType.longArg(0, Constants.MAX_XP))).executes((ctx) ->
				{
					addXP(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"), Job.byIndex(IntegerArgumentType.getInteger(ctx, "job")), LongArgumentType.getLong(ctx, "xp"));
					return 0;
				})
			);
	}

	
	private static void addXP(CommandSource source, ServerPlayerEntity target, Job job, long xp)
	{
		if(!(source.getEntity() instanceof ServerPlayerEntity)) return;
		ServerPlayerEntity sender = (ServerPlayerEntity)source.getEntity();
		PlayerData.getPlayerJobs(target).gainXP(job, xp, target);
        sender.displayClientMessage(new StringTextComponent(xp + " xp added to " + target.getName() + " for job " + job.name()), true);
        PacketHandler.INSTANCE.sendTo(new PacketUpdateClientJob(PlayerData.getPlayerJobs(target).toTotalXPs()), target.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}
}
