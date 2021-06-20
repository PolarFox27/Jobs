package com.polarfox27.jobs.network;

import java.util.function.Function;
import java.util.function.Supplier;

import com.polarfox27.jobs.data.ClientInfos;
import com.polarfox27.jobs.util.Constants.Job;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketAddXP{

    private int job;
    private long xpAdded;
    public PacketAddXP(){}
    public PacketAddXP(Job j, long xp)
    {
        this.job = j.index;
        this.xpAdded = xp;
    }

    public static PacketAddXP fromBytes(PacketBuffer buf)
    {
    	return new PacketAddXP(Job.byIndex(buf.readInt()), buf.readLong());
	}


    public static void toBytes(PacketAddXP packet, PacketBuffer buf)
    {
        buf.writeInt(packet.job);
        buf.writeLong(packet.xpAdded);
    }


    public static void handle(PacketAddXP message, Supplier<Context> ctx)
    {
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
        {
            ClientInfos.showAddGui(Job.byIndex(message.job), message.xpAdded);
        }
        ctx.get().setPacketHandled(true);
    }
}
