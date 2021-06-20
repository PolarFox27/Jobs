package com.polarfox27.jobs.network;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.MixinEnvironment.Side;

import com.polarfox27.jobs.data.PlayerData;
import com.polarfox27.jobs.util.handler.PacketHandler;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketAskClientUpdate{


    public PacketAskClientUpdate(){}



    public static PacketAskClientUpdate fromBytes(PacketBuffer buf){return new PacketAskClientUpdate();}


    public static void toBytes(PacketAskClientUpdate packet, PacketBuffer buf){}

    public static void handle(PacketAskClientUpdate message, Supplier<Context> ctx) 
    {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) 
        {
            PacketHandler.INSTANCE.sendTo(new PacketUpdateClientJob(PlayerData.getPlayerJobs(ctx.get().getSender()).toTotalXPs()), ctx.get().getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
        ctx.get().setPacketHandled(true);
    }
}
