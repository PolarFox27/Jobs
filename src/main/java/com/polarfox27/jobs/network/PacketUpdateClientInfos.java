package com.polarfox27.jobs.network;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.xml.ws.handler.MessageContext;

import org.spongepowered.asm.mixin.MixinEnvironment.Side;

import com.polarfox27.jobs.data.ClientInfos;
import com.polarfox27.jobs.util.Constants;
import com.polarfox27.jobs.util.Constants.Job;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketUpdateClientInfos{

    public Map<Block, long[]> BREAK_BLOCK_XP = new HashMap<>();
    public Map<Block, Job> BREAK_BLOCK_JOB = new HashMap<>();

    public Map<Item, long[]> HARVEST_CROP_XP = new HashMap<>();
    public Map<Item, Job> HARVEST_CROP_JOB = new HashMap<>();

    public Map<Item, long[]> CRAFT_ITEM_XP = new HashMap<>();
    public Map<Item, Job> CRAFT_ITEM_JOB = new HashMap<>();

    public Map<Item, long[]> SMELT_ITEM_XP = new HashMap<>();
    public Map<Item, Job> SMELT_ITEM_JOB = new HashMap<>();

    public Map<String, long[]> KILL_ENTITY_XP = new HashMap<>();
    public Map<String, Job> KILL_ENTITY_JOB = new HashMap<>();


    public Map<Item, Integer> CRAFT_UNLOCK_LVL = new HashMap<>();
    public Map<Item, Job> CRAFT_UNLOCK_JOB = new HashMap<>();


    public PacketUpdateClientInfos(){}
    public PacketUpdateClientInfos(Map<Block, long[]> m1, Map<Block, Job> m2, Map<Item, long[]> m3, Map<Item, Job> m4,
                                   Map<Item, long[]> m5, Map<Item, Job> m6,
                                   Map<Item, long[]> m7, Map<Item, Job> m8,
                                   Map<String, long[]> m9, Map<String, Job> m10,
                                   Map<Item, Integer> m11, Map<Item, Job> m12)
    {
        this.BREAK_BLOCK_XP = m1;
        this.BREAK_BLOCK_JOB = m2;
        this.HARVEST_CROP_XP = m3;
        this.HARVEST_CROP_JOB = m4;
        this.CRAFT_ITEM_XP = m5;
        this.CRAFT_ITEM_JOB = m6;
        this.SMELT_ITEM_XP = m7;
        this.SMELT_ITEM_JOB = m8;
        this.KILL_ENTITY_XP = m9;
        this.KILL_ENTITY_JOB = m10;
        this.CRAFT_UNLOCK_LVL = m11;
        this.CRAFT_UNLOCK_JOB = m12;
    }



    public static PacketUpdateClientInfos fromBytes(PacketBuffer buf)
    {
    	PacketUpdateClientInfos packet = new PacketUpdateClientInfos();
        int size1 = buf.readInt();
        int size2 = buf.readInt();
        int size3 = buf.readInt();
        int size4 = buf.readInt();
        int size5 = buf.readInt();
        int size6 = buf.readInt();

        for(int i = 0; i < size1; i++) //BREAK BLOCK
        {
            Block b = Block.stateById(buf.readInt()).getBlock();
            Job j = Job.byIndex(buf.readInt());
            long[] xps = new long[25];
            for(int x = 0; x < 25; x++)
                xps[x] = buf.readLong();
            packet.BREAK_BLOCK_JOB.put(b, j);
            packet.BREAK_BLOCK_XP.put(b, xps);
        }

        for(int i = 0; i < size2; i++) //HARVEST CROP
        {
            Item b = Item.byId(buf.readInt());
            Job j = Job.byIndex(buf.readInt());
            long[] xps = new long[25];
            for(int x = 0; x < 25; x++)
                xps[x] = buf.readLong();
            packet.HARVEST_CROP_JOB.put(b, j);
            packet.HARVEST_CROP_XP.put(b, xps);
        }

        for(int i = 0; i < size3; i++) //CRAFT ITEM
        {
            Item item = Item.byId(buf.readInt());
            Job j = Job.byIndex(buf.readInt());
            long[] xps = new long[25];
            for(int x = 0; x < 25; x++)
                xps[x] = buf.readLong();
            packet.CRAFT_ITEM_JOB.put(item, j);
            packet.CRAFT_ITEM_XP.put(item, xps);
        }

        for(int i = 0; i < size4; i++) //SMELT ITEM
        {
            Item item = Item.byId(buf.readInt());
            Job j = Job.byIndex(buf.readInt());
            long[] xps = new long[25];
            for(int x = 0; x < 25; x++)
                xps[x] = buf.readLong();
            packet.SMELT_ITEM_JOB.put(item, j);
            packet.SMELT_ITEM_XP.put(item, xps);
        }

        for(int i = 0; i < size5; i++)//KILL ENTITY
        {
            String s = Constants.getIDEntities().get(buf.readInt());
            Job job = Job.byIndex(buf.readInt());
            long[] xps = new long[25];
            for(int j = 0; j < 25; j++)
                xps[j] = buf.readLong();
            packet.KILL_ENTITY_JOB.put(s, job);
            packet.KILL_ENTITY_XP.put(s, xps);
        }

        for(int i = 0; i < size6; i++) //UNLOCK CRAFT
        {
            Item item = Item.byId(buf.readInt());
            Job j = Job.byIndex(buf.readInt());
            int lvl = buf.readInt();
            packet.CRAFT_UNLOCK_JOB.put(item, j);
            packet.CRAFT_UNLOCK_LVL.put(item, lvl);
        }
        return packet;
    }

    public static void toBytes(PacketUpdateClientInfos packet, PacketBuffer buf)
    {
        buf.writeInt(packet.BREAK_BLOCK_JOB.size());
        buf.writeInt(packet.HARVEST_CROP_JOB.size());
        buf.writeInt(packet.CRAFT_ITEM_JOB.size());
        buf.writeInt(packet.SMELT_ITEM_JOB.size());
        buf.writeInt(packet.KILL_ENTITY_JOB.size());
        buf.writeInt(packet.CRAFT_UNLOCK_JOB.size());

        for(Block b : packet.BREAK_BLOCK_JOB.keySet())   //BREAK BLOCK
        {
            buf.writeInt(Block.getId(b.defaultBlockState()));
            buf.writeInt(packet.BREAK_BLOCK_JOB.get(b).index);
            for(int i = 0 ; i < 25; i++)
                buf.writeLong(packet.BREAK_BLOCK_XP.get(b)[i]);
        }

        for(Item b : packet.HARVEST_CROP_JOB.keySet())   //HARVEST CROP
        {
            buf.writeInt(Item.getId(b));
            buf.writeInt(packet.HARVEST_CROP_JOB.get(b).index);
            for(int i = 0 ; i < 25; i++)
                buf.writeLong(packet.HARVEST_CROP_XP.get(b)[i]);
        }

        for(Item item : packet.CRAFT_ITEM_JOB.keySet())   //CRAFT ITEM
        {
            buf.writeInt(Item.getId(item));
            buf.writeInt(packet.CRAFT_ITEM_JOB.get(item).index);
            for(int i = 0 ; i < 25; i++)
                buf.writeLong(packet.CRAFT_ITEM_XP.get(item)[i]);
        }

        for(Item item : packet.SMELT_ITEM_JOB.keySet())   //SMELT ITEM
        {
            buf.writeInt(Item.getId(item));
            buf.writeInt(packet.SMELT_ITEM_JOB.get(item).index);
            for(int i = 0 ; i < 25; i++)
                buf.writeLong(packet.SMELT_ITEM_XP.get(item)[i]);
        }

        for(String s : packet.KILL_ENTITY_JOB.keySet())//KILL ENTITY
        {
            buf.writeInt(Constants.getEntitiesID().get(s));
            buf.writeInt(packet.KILL_ENTITY_JOB.get(s).index);
            for(int i = 0; i < 25; i++)
                buf.writeLong(packet.KILL_ENTITY_XP.get(s)[i]);
        }

        for(Item item : packet.CRAFT_UNLOCK_JOB.keySet())   //UNLOCK CRAFT
        {
            buf.writeInt(Item.getId(item));
            buf.writeInt(packet.CRAFT_UNLOCK_JOB.get(item).index);
            buf.writeInt(packet.CRAFT_UNLOCK_LVL.get(item));
        }
    }




    public static void handle(PacketUpdateClientInfos message, Supplier<Context> ctx)
    {
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
        {
            ClientInfos.BREAK_BLOCK_XP = message.BREAK_BLOCK_XP;
            ClientInfos.BREAK_BLOCK_JOB = message.BREAK_BLOCK_JOB;
            ClientInfos.HARVEST_CROP_XP = message.HARVEST_CROP_XP;
            ClientInfos.HARVEST_CROP_JOB = message.HARVEST_CROP_JOB;
            ClientInfos.CRAFT_ITEM_XP = message.CRAFT_ITEM_XP;
            ClientInfos.CRAFT_ITEM_JOB = message.CRAFT_ITEM_JOB;
            ClientInfos.SMELT_ITEM_XP = message.SMELT_ITEM_XP;
            ClientInfos.SMELT_ITEM_JOB = message.SMELT_ITEM_JOB;
            ClientInfos.KILL_ENTITY_XP = message.KILL_ENTITY_XP;
            ClientInfos.KILL_ENTITY_JOB = message.KILL_ENTITY_JOB;

            ClientInfos.CRAFT_UNLOCK_LVL = message.CRAFT_UNLOCK_LVL;
            ClientInfos.CRAFT_UNLOCK_JOB = message.CRAFT_UNLOCK_JOB;
        }
        ctx.get().setPacketHandled(true);
    }
}
