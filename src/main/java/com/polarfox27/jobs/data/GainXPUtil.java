package com.polarfox27.jobs.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.polarfox27.jobs.network.PacketUpdateClientInfos;
import com.polarfox27.jobs.util.Constants.Job;
import com.polarfox27.jobs.util.handler.PacketHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkDirection;

public class GainXPUtil {

    public static Map<Block, long[]> BREAK_BLOCK_XP = new HashMap<>();
    public static Map<Block, Job> BREAK_BLOCK_JOB = new HashMap<>();

    public static Map<Item, long[]> HARVEST_CROP_XP = new HashMap<>();
    public static Map<Item, Job> HARVEST_CROP_JOB = new HashMap<>();

    public static Map<Item, long[]> CRAFT_ITEM_XP = new HashMap<>();
    public static Map<Item, Job> CRAFT_ITEM_JOB = new HashMap<>();

    public static Map<Item, long[]> SMELT_ITEM_XP = new HashMap<>();
    public static Map<Item, Job> SMELT_ITEM_JOB = new HashMap<>();

    public static Map<String, long[]> KILL_ENTITY_XP = new HashMap<>();
    public static Map<String, Job> KILL_ENTITY_JOB = new HashMap<>();


    public static Map<Item, Integer> CRAFT_UNLOCK_LVL = new HashMap<>();
    public static Map<Item, Job> CRAFT_UNLOCK_JOB = new HashMap<>();

    public static Map<Integer, List<ItemStack>> REWARDS_WARRIOR = new HashMap<>();
    public static Map<Integer, List<ItemStack>> REWARDS_WIZARD = new HashMap<>();
    public static Map<Integer, List<ItemStack>> REWARDS_HERBALIST = new HashMap<>();
    public static Map<Integer, List<ItemStack>> REWARDS_MINER = new HashMap<>();




    public static void sendDataToClient(ServerPlayerEntity player)
    {
        PacketUpdateClientInfos packet1 = new PacketUpdateClientInfos(BREAK_BLOCK_XP, BREAK_BLOCK_JOB,
                                                                     HARVEST_CROP_XP, HARVEST_CROP_JOB,
                                                                     CRAFT_ITEM_XP, CRAFT_ITEM_JOB,
                                                                     SMELT_ITEM_XP, SMELT_ITEM_JOB,
                                                                     KILL_ENTITY_XP, KILL_ENTITY_JOB,
                                                                     CRAFT_UNLOCK_LVL, CRAFT_UNLOCK_JOB);
        PacketHandler.INSTANCE.sendTo(packet1, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }









}
