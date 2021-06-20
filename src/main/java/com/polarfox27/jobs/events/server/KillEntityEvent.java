package com.polarfox27.jobs.events.server;

import com.polarfox27.jobs.data.GainXPUtil;
import com.polarfox27.jobs.data.JobsInfo;
import com.polarfox27.jobs.data.PlayerData;
import com.polarfox27.jobs.util.Constants;
import com.polarfox27.jobs.util.Constants.Job;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class KillEntityEvent {

    @SubscribeEvent
    public void onKill(LivingDeathEvent event)
    {
    	if(event.getEntityLiving().level.isClientSide()) return;
        if(event.getSource().getEntity() instanceof ServerPlayerEntity)
        {
            String name = "";
            if(event.getEntityLiving() instanceof ServerPlayerEntity) name = "Player";
            else name = Constants.getNamesByClass().get(event.getEntityLiving().getType());

            ServerPlayerEntity p = (ServerPlayerEntity)event.getSource().getEntity();
            if(!GainXPUtil.KILL_ENTITY_XP.containsKey(name)) return;
            JobsInfo infos = PlayerData.getPlayerJobs(p);
            Job j = GainXPUtil.KILL_ENTITY_JOB.get(name);
            long xp = GainXPUtil.KILL_ENTITY_XP.get(name)[infos.getLevelByJob(j)];

            infos.gainXP(j, xp, p);
        }
    }


}
