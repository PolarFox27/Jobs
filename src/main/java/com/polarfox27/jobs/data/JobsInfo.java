package com.polarfox27.jobs.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.polarfox27.jobs.network.PacketAddXP;
import com.polarfox27.jobs.network.PacketLevelUp;
import com.polarfox27.jobs.network.PacketSendRewardsClient;
import com.polarfox27.jobs.network.PacketUpdateClientJob;
import com.polarfox27.jobs.util.Constants;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.JobsMath;
import com.polarfox27.jobs.util.handler.PacketHandler;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkDirection;

public class JobsInfo {
	
	private int[] levels = new int[] {0, 0, 0, 0};
	private long[] XPs = new long[] {0, 0, 0, 0};
	
	
	//Set Values
	public void set(Job j, int lvl, long xp)
	{
		long total = Constants.TOTAL_XP_BY_LEVEL[lvl] + xp;
		set(j, total);
	}
	
	public void set(Job j, long value)
	{
		long total = JobsMath.clamp(value, 0, Constants.TOTAL_XP_BY_LEVEL[25]);
		int lvl = 0;
		long xp = 0;
		for(int i = 25; i >= 0; i--)
		{
			if(Constants.TOTAL_XP_BY_LEVEL[i] <= total)
			{
				lvl = i;
				xp = total - Constants.TOTAL_XP_BY_LEVEL[i];
				break;
			}
		}
		
		this.levels[j.index] = lvl;
		this.XPs[j.index] = xp;
	}
	
	
	//Get Values
	public int getLevelByJob(Job j)
	{
		return this.levels[j.index];
	}
	
	public long getXPByJob(Job j)
	{
		return this.XPs[j.index];
	}
	
	public long getMissingXPForJob(Job j)
	{
		if(this.levels[j.index] >= 25) return 0;
		
		return Constants.XP_BY_LEVEL[j.index + 1] - this.XPs[j.index];
	}
	
	
	//Conversion with total xp long[]
	public JobsInfo fromTotalXPs(long[] total)
	{
		if(total.length != 4) return new JobsInfo();
		
		for(int i = 0; i < 4; i++)
		{
			long l = total[i];
			Job j = Job.byIndex(i);
			set(j, l);
		}
		return this;
	}
	
	public long[] toTotalXPs()
	{
		long[] total = new long[] {0, 0, 0, 0};
		
		for(int i = 0; i < 4; i++)
		{
			long l = Constants.TOTAL_XP_BY_LEVEL[this.levels[i]] + this.XPs[i];
			total[i] = l;
				
		}
		return total;
	}
	
	
	//Add XP
	public void addXP(Job j, long xp)
	{
		long total = Constants.TOTAL_XP_BY_LEVEL[this.levels[j.index]] + this.XPs[j.index] + xp;
		set(j, total);
	}
	
	public boolean isMax(Job j)
	{
		return this.levels[j.index] >= 25;
	}
	
	public void gainXP(Job j, long xp, ServerPlayerEntity p)
	{
		if(xp <= 0) return;

		int previousLVL = this.levels[j.index]; 
		addXP(j, xp);
		PacketHandler.INSTANCE.sendTo(new PacketUpdateClientJob(this.toTotalXPs()), p.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
		int LVL = this.levels[j.index];
		PacketHandler.INSTANCE.sendTo(new PacketAddXP(j, xp), p.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
		if(LVL > previousLVL)
		{
			PacketHandler.INSTANCE.sendTo(new PacketLevelUp(j), p.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			giveReward(p, j, LVL);
		}

		if(LVL == 25)
		{
			for(ServerPlayerEntity mp : p.getServer().getPlayerList().getPlayers())
			{
				mp.displayClientMessage(new StringTextComponent(TextFormatting.DARK_PURPLE + p.getName().getString() +
						TextFormatting.BLUE + " has reached level 25 for the job " + I18n.get("jobs." + j.name) + " !"), true);
				p.getServer().sendMessage(new StringTextComponent(TextFormatting.DARK_PURPLE + p.getName().getString() +
						TextFormatting.BLUE + " has reached level 25 for the job " + I18n.get("jobs." + j.name) + " !"), mp.getGameProfile().getId());
			}
		}
	}

	private void giveReward(ServerPlayerEntity p, Job j, int lvl)
	{
		List<ItemStack> list = new ArrayList<>();
		System.out.println(j + " : " + lvl);
		switch (j)
		{
			case HUNTER:
				for(ItemStack s : GainXPUtil.REWARDS_WARRIOR.get(lvl))
					list.add(s.copy());
				break;
			case MAGICIAN:
				for(ItemStack s : GainXPUtil.REWARDS_WIZARD.get(lvl))
					list.add(s.copy());
				break;
			case FARMER:
				for(ItemStack s : GainXPUtil.REWARDS_HERBALIST.get(lvl))
					list.add(s.copy());
				break;
			case MINER:
				for(ItemStack s : GainXPUtil.REWARDS_MINER.get(lvl))
					list.add(s.copy());
				break;
			default:
				break;
		}
		PacketHandler.INSTANCE.sendTo(new PacketSendRewardsClient(list), p.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
		for(ItemStack s : list)
			p.inventory.add(s.copy());
		p.inventoryMenu.broadcastChanges();
	}
	
	public void fromNBT(CompoundNBT nbt)
	{
		this.fromTotalXPs(new long[] {nbt.getLong("hunter"),
									  nbt.getLong("magician"),
									  nbt.getLong("farmer"),
									  nbt.getLong("miner")});
	}
	
	public CompoundNBT toNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		long[] xps = this.toTotalXPs();
		nbt.putLong("hunter", xps[0]);
		nbt.putLong("magician", xps[1]);
		nbt.putLong("farmer", xps[2]);
		nbt.putLong("miner", xps[3]);
		return nbt;
	}
	
	public void copy(JobsInfo other)
	{
		this.fromTotalXPs(other.toTotalXPs());
	}
	
	@Override
	public String toString() 
	{
		long[] xps = this.toTotalXPs();
		return "Job(" + Arrays.toString(xps) + ")";
	}

}
