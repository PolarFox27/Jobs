package com.polarfox27.jobs.events.server;

import com.polarfox27.jobs.data.GainXPUtil;
import com.polarfox27.jobs.data.JobsInfo;
import com.polarfox27.jobs.data.PlayerData;
import com.polarfox27.jobs.util.Constants.Job;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CraftItemEvents {


    @SubscribeEvent
    public void onCraft(ItemCraftedEvent event)
    {
    	if(event.getPlayer().level.isClientSide()) return;
        int count = event.getCrafting().getCount();
        Item item = event.getCrafting().getItem();

        if(GainXPUtil.CRAFT_ITEM_JOB.containsKey(item))
        {
            Job j = GainXPUtil.CRAFT_ITEM_JOB.get(item);
            JobsInfo infos = PlayerData.getPlayerJobs(event.getPlayer());
            long xp = GainXPUtil.CRAFT_ITEM_XP.get(item)[infos.getLevelByJob(j)];

            infos.gainXP(j, xp * count, (ServerPlayerEntity) event.getPlayer());
        }
    }

    @SubscribeEvent
    public void onSmelt(ItemSmeltedEvent event)
    {
    	if(event.getPlayer().level.isClientSide()) return;
        int count = event.getSmelting().getCount();
        Item item = event.getSmelting().getItem();

        if(GainXPUtil.SMELT_ITEM_JOB.containsKey(item))
        {
            Job j = GainXPUtil.SMELT_ITEM_JOB.get(item);
            JobsInfo infos = PlayerData.getPlayerJobs(event.getPlayer());
            long xp = GainXPUtil.SMELT_ITEM_XP.get(item)[infos.getLevelByJob(j)];

            infos.gainXP(j, xp * count, (ServerPlayerEntity) event.getPlayer());
        }
    }

    
}
