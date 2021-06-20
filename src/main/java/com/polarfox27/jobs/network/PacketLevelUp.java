package com.polarfox27.jobs.network;

import java.util.function.Supplier;

import javax.xml.ws.handler.MessageContext;

import org.spongepowered.asm.mixin.MixinEnvironment.Side;

import com.polarfox27.jobs.data.ClientInfos;
import com.polarfox27.jobs.util.Constants.Job;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketLevelUp{

    private Job job = Job.NONE;

    public PacketLevelUp(){}
    public PacketLevelUp(Job j)
    {
        this.job = j;
    }

    public static PacketLevelUp fromBytes(PacketBuffer buf)
    {
        return new PacketLevelUp(Job.byIndex(buf.readInt()));
    }

    public static void toBytes(PacketLevelUp msg, PacketBuffer buf)
    {
        buf.writeInt(msg.job.index);
    }

    public static void handle(PacketLevelUp message, Supplier<Context> ctx)
    {
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
        {
            if(Minecraft.getInstance().player == null) return;
            ClientInfos.showLevelUpGui(message.job);
        }
        ctx.get().setPacketHandled(true);
    }
}
