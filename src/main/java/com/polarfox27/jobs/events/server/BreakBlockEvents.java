package com.polarfox27.jobs.events.server;


import java.util.HashMap;
import java.util.Map;

import com.polarfox27.jobs.data.GainXPUtil;
import com.polarfox27.jobs.data.JobsInfo;
import com.polarfox27.jobs.data.PlayerData;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BreakBlockEvents {

    @SubscribeEvent
    public void onBreakOreOrCrop(BreakEvent event)
    {
    	if(event.getWorld().isClientSide()) return;
        if(!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        Block block = event.getState().getBlock();
        JobsInfo jobs = PlayerData.getPlayerJobs(player);

        //Ores
        if(GainXPUtil.BREAK_BLOCK_XP.containsKey(block))
        {
            long xp = GainXPUtil.BREAK_BLOCK_XP.get(block)
                    [jobs.getLevelByJob(GainXPUtil.BREAK_BLOCK_JOB.get(block))];
            jobs.gainXP(GainXPUtil.BREAK_BLOCK_JOB.get(block), xp, player);
        }

        //Crops
        else if(GainXPUtil.HARVEST_CROP_XP.containsKey(getItemsFromCrops().get(block)) && block instanceof CropsBlock && getItemsFromCrops().containsKey(block))
        {
            if(!((CropsBlock)block).isMaxAge(event.getState())) return;

            long xp = GainXPUtil.HARVEST_CROP_XP.get(getItemsFromCrops().get(block))
                    [jobs.getLevelByJob(GainXPUtil.HARVEST_CROP_JOB.get(getItemsFromCrops().get(block)))];
            jobs.gainXP(GainXPUtil.HARVEST_CROP_JOB.get(getItemsFromCrops().get(block)), xp, player);
        }

    }


    public static Map<Block, Item> getItemsFromCrops()
    {
        Map<Block, Item> map = new HashMap<>();
        map.put(Blocks.WHEAT, Items.WHEAT);
        map.put(Blocks.POTATOES, Items.POTATO);
        map.put(Blocks.CARROTS, Items.CARROT);
        map.put(Blocks.BEETROOTS, Items.BEETROOT);
        return map;
    }
}
