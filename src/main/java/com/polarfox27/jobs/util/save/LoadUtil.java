package com.polarfox27.jobs.util.save;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.polarfox27.jobs.data.GainXPUtil;
import com.polarfox27.jobs.util.Constants;
import com.polarfox27.jobs.util.Constants.Job;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.registries.ForgeRegistries;

public class LoadUtil {

    public static void loadData(MinecraftServer server)
    {
        LoadXPValues.loadBreakBlockXP(server);
        LoadXPValues.loadKillEntityXP(server);
        LoadXPValues.loadCraftItemXP(server);
        LoadXPValues.loadSmeltItemXP(server);
        LoadXPValues.loadHarvestCropXP(server);
        loadBlockedCrafts(server);
        loadRewards(server);
    }


    public static String getDataFolder(MinecraftServer server)
    {
    	String name = server.getMotd().substring(server.getMotd().indexOf("-")+2);
    	if(server.isSingleplayer())
    		return server.getServerDirectory().getAbsolutePath() + "/saves/" + name + "/jobs/data";
    	else
    		return server.getServerDirectory().getAbsolutePath() + "/jobs/data";
    }

    public static void createEmptyJSON(File folder, String name) throws IOException {
        folder.mkdirs();
        JsonObject json = new JsonObject();
        JsonObject property = new JsonObject();
        property.addProperty("job", "HUNTER");
        JsonArray array = new JsonArray();
        for(int i = 0; i < 25; i++)
            array.add(i);
        property.add("xp", array);
        json.add("minecraft-coal_ore", property);
        FileWriter writer = new FileWriter(new File(folder.getAbsolutePath() + "/" + name));
        new Gson().toJson(json, writer);
        writer.close();

        System.out.println(TextFormatting.BLUE + "[Jobs]" +
                TextFormatting.YELLOW +  " Warning : File <" + name + "> not found. An empty file was created");
    }
    
    public static void createEmptyKillEntityJSON(File folder, String name) throws IOException {
        folder.mkdirs();
        JsonObject json = new JsonObject();
        JsonObject property = new JsonObject();
        property.addProperty("job", "HUNTER");
        JsonArray array = new JsonArray();
        for(int i = 0; i < 25; i++)
            array.add(i);
        property.add("xp", array);
        json.add("Cow", property);
        FileWriter writer = new FileWriter(new File(folder.getAbsolutePath() + "/" + name));
        new Gson().toJson(json, writer);
        writer.close();

        System.out.println(TextFormatting.BLUE + "[Jobs]" +
                TextFormatting.YELLOW +  " Warning : File <" + name + "> not found. An empty file was created");
    }

    public static void createEmptyRewardJSON(File folder, String name) throws IOException {
        folder.mkdirs();
        JsonObject json = new JsonObject();

        for(int i = 0; i < 4; i++)
        {
            String property = Job.byIndex(i).name();
            JsonObject job = new JsonObject();
            for(int j = 1; j <= 25; j ++)
            {
                JsonArray array = new JsonArray();
                JsonObject stack = new JsonObject();
                stack.addProperty("item", "minecraft-coal");
                stack.addProperty("count", 1);
                array.add(stack);
                job.add(String.valueOf(j), array);
            }
            json.add(property, job);
        }
        FileWriter writer = new FileWriter(new File(folder.getAbsolutePath() + "/" + name));
        new Gson().toJson(json, writer);
        writer.close();

        System.out.println(TextFormatting.BLUE + "[Jobs]" +
                TextFormatting.YELLOW +  " Warning : File <" + name + "> not found. An empty file was created");
    }
    
    public static void createEmptyBlockedCraftJSON(File folder, String name) throws IOException {
    	folder.mkdirs();
        JsonObject json = new JsonObject();
        JsonObject property = new JsonObject();
        property.addProperty("job", "HUNTER");
        property.addProperty("lvl", 1);
        json.add("minecraft-diamond_pickaxe", property);
        FileWriter writer = new FileWriter(new File(folder.getAbsolutePath() + "/" + name));
        new Gson().toJson(json, writer);
        writer.close();

        System.out.println(TextFormatting.BLUE + "[Jobs]" +
                TextFormatting.YELLOW +  " Warning : File <" + name + "> not found. An empty file was created");
    }



    public static void loadBlockedCrafts(MinecraftServer server)
    {
        try
        {
            File file = new File(getDataFolder(server) + "/blockedcraft.json");
            if(!file.exists())
            {
                createEmptyBlockedCraftJSON(new File(getDataFolder(server)), "blockedcraft.json");
                return;
            }

            JsonObject json = (JsonObject) new JsonParser().parse(new FileReader(file));
            for(Map.Entry<String, JsonElement> entry : json.entrySet())
            {
                String name = entry.getKey().replace('-', ':');
                JsonObject property = entry.getValue().getAsJsonObject();
                int level = property.get("lvl").getAsInt();

                GainXPUtil.CRAFT_UNLOCK_JOB.put(ForgeRegistries.ITEMS.getValue(new ResourceLocation(name)), Constants.Job.valueOf(property.get("job").getAsString()));
                GainXPUtil.CRAFT_UNLOCK_LVL.put(ForgeRegistries.ITEMS.getValue(new ResourceLocation(name)), level);
            }
        }
        catch(IOException e)
        {
            System.out.println(TextFormatting.RED + "[Jobs] Failed to load Blocked Crafts !");
        }
    }

    public static void loadRewards(MinecraftServer server)
    {
        try
        {
            File file = new File(getDataFolder(server) + "/rewards.json");
            if(!file.exists())
            {
                createEmptyRewardJSON(new File(getDataFolder(server)), "rewards.json");
                return;
            }

            JsonObject json = (JsonObject) new JsonParser().parse(new FileReader(file));
            for(Map.Entry<String, JsonElement> entry : json.entrySet())
            {
                Job job = Job.valueOf(entry.getKey());
                for(Map.Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet())
                {
                    int lvl = Integer.parseInt(entry2.getKey());
                    JsonArray array = entry2.getValue().getAsJsonArray();
                    List<ItemStack> list = new ArrayList<>();
                    for(JsonElement e : array)
                    {
                        ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(e.getAsJsonObject().get("item").getAsString().replace('-', ':'))),
                                                                         e.getAsJsonObject().get("count").getAsInt());
                        list.add(stack);
                    }
                    switch (job)
                    {
                        case HUNTER: GainXPUtil.REWARDS_WARRIOR.put(lvl, list);
                        case MAGICIAN: GainXPUtil.REWARDS_WIZARD.put(lvl, list);
                        case FARMER: GainXPUtil.REWARDS_HERBALIST.put(lvl, list);
                        case MINER: GainXPUtil.REWARDS_MINER.put(lvl, list);
                        default: break;
                    }
                }
            }
        }
        catch(IOException e)
        {
            System.out.println(TextFormatting.RED + "[Jobs] Failed to load Rewards !");
        }
    }

}
