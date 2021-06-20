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
import net.minecraftforge.fml.network.NetworkDirection;

public class CommandSet {
	
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("jobs-set")
			.requires((source) -> {return source.hasPermission(2);})
			.then(Commands.argument("target", EntityArgument.player())).then(Commands.argument("job", IntegerArgumentType.integer(0, 3)))
			.then(Commands.argument("xp", LongArgumentType.longArg(0, Constants.MAX_XP))).executes((ctx) ->
				{
					System.out.println("executes : " + StringArgumentType.getString(ctx, "job").toUpperCase());
					setJobs(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"), Job.byIndex(IntegerArgumentType.getInteger(ctx, "job")), LongArgumentType.getLong(ctx, "xp"));
					return 0;
				})
			.then(Commands.argument("level", IntegerArgumentType.integer(0, 25))).executes((ctx)->
				{
					setJobs(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"), Job.byIndex(IntegerArgumentType.getInteger(ctx, "job")), IntegerArgumentType.getInteger(ctx, "level"), LongArgumentType.getLong(ctx, "xp"));
					return 0;
				})
			);
	}
	
	private static void setJobs(CommandSource source, ServerPlayerEntity target, Job job, int lvl, long xp)
	{
		setJobs(source, target, job, Constants.TOTAL_XP_BY_LEVEL[lvl] + xp);
	}
	
	private static void setJobs(CommandSource source, ServerPlayerEntity target, Job job, long total)
	{
		System.out.println("function starting");
		PlayerData.getPlayerJobs(target).set(job, total);
		System.out.println("jobs set");
        PacketHandler.INSTANCE.sendTo(new PacketUpdateClientJob(PlayerData.getPlayerJobs(target).toTotalXPs()), target.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        System.out.println("done");
	}

}
