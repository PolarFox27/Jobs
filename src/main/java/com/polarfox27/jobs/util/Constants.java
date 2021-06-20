package com.polarfox27.jobs.util;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.MagmaCubeEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Constants {
	
	public static final long[] XP_BY_LEVEL = new long[] {0, 100, 150, 250, 500, 800, //0 - 5
														 1150, 1550, 2000, 2500, 3100, //6 - 10
														 3750, 4500, 5500, 7000, 9000, //11 - 15
														 11500, 15000, 19000, 24000, 30000, //16 - 20
														 39250, 49500, 63100, 80000, 100000}; //21 - 25
	
	public static final long[] TOTAL_XP_BY_LEVEL = new long[] {0, 100, 250, 500, 1000, 1800, //0 - 5
															   2950, 4500, 6500, 9000, 12100, //6 - 10
															   15850, 20350, 25850, 32850, 41850, //11 - 15
															   53350, 68350, 87350, 111350, 141350, //16 - 20
															   180600, 230100, 293200, 373200, 473200}; //21 - 25
	
	public static long MAX_XP = TOTAL_XP_BY_LEVEL[25];
	
	public static final String[] ENTITIES = new String[]{"Cow", "Chicken", "Zombie", "Sheep", "Rabbit", "Blaze", "Creeper", "Enderman", "Ghast", "Magma Cube", 
														 "Pig", "Skeleton", "Slime", "Spider", "Witch", "Wither Skeleton", "Zombie Pigman", "Player", "Piglin", 
														 "Hoglin", "Zoglin", "Wither", "Ender Dragon", "Guardian", "Elder Guardian"};
	public static final int[] IDS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};

	public static Map<String, Integer> getEntitiesID()
	{
		Map<String, Integer> map = new HashMap<>();
		for(int i = 0; i < ENTITIES.length; i++)
		{
			map.put(ENTITIES[i], IDS[i]);
		}
		return map;
	}
	public static Map<Integer, String> getIDEntities()
	{
		Map<Integer, String> map = new HashMap<>();
		for(int i = 0; i < ENTITIES.length; i++)
		{
			map.put(IDS[i], ENTITIES[i]);
		}
		return map;
	}


	public static Map<String, EntityType> getClassesByName()
	{
		Map<String, EntityType> map = new HashMap<>();
		map.put("Cow", EntityType.COW);
		map.put("Chicken", EntityType.CHICKEN);
		map.put("Zombie", EntityType.ZOMBIE);
		map.put("Sheep", EntityType.SHEEP);
		map.put("Rabbit", EntityType.RABBIT);
		map.put("Blaze", EntityType.BLAZE);
		map.put("Creeper", EntityType.CREEPER);
		map.put("Enderman", EntityType.ENDERMAN);
		map.put("Ghast", EntityType.GHAST);
		map.put("Magma Cube", EntityType.MAGMA_CUBE);
		map.put("Pig", EntityType.PIG);
		map.put("Skeleton", EntityType.SKELETON);
		map.put("Slime", EntityType.SLIME);
		map.put("Spider", EntityType.SPIDER);
		map.put("Witch", EntityType.WITCH);
		map.put("Wither Skeleton", EntityType.WITHER_SKELETON);
		map.put("Zombie Pigman", EntityType.ZOMBIFIED_PIGLIN);
		map.put("Piglin", EntityType.PIGLIN);
		map.put("Hoglin", EntityType.HOGLIN);
		map.put("Zoglin", EntityType.ZOGLIN);
		map.put("Player", EntityType.PLAYER);
		map.put("Wither", EntityType.WITHER);
		map.put("Ender Dragon", EntityType.ENDER_DRAGON);
		map.put("Guardian", EntityType.GUARDIAN);
		map.put("Elder Guardian", EntityType.ELDER_GUARDIAN);
		return map;
	}
	
	public static Map<EntityType, String> getNamesByClass()
	{
		Map<EntityType, String> map = new HashMap<EntityType, String>();
		for(Entry<String, EntityType> entry : getClassesByName().entrySet())
			map.put(entry.getValue(), entry.getKey());
		return map;
	}

	public static Map<String, Integer> getClassScaleByName()
	{
		Map<String, Integer> map = new HashMap<>();
		map.put("Cow", 17);
		map.put("Chicken", 30);
		map.put("Zombie", 15);
		map.put("Sheep", 17);
		map.put("Rabbit", 30);
		map.put("Blaze", 16);
		map.put("Creeper", 16);
		map.put("Enderman", 12);
		map.put("Ghast", 5);
		map.put("Magma Cube", 30);
		map.put("Pig", 18);
		map.put("Skeleton", 15);
		map.put("Slime", 30);
		map.put("Spider", 18);
		map.put("Witch", 15);
		map.put("Wither Skeleton", 12);
		map.put("Zombie Pigman", 15);
		map.put("Piglin", 15);
		map.put("Hoglin", 13);
		map.put("Zoglin", 13);
		map.put("Player", 15);
		map.put("Wither", 10);
		map.put("Ender Dragon", 5);
		map.put("Guardian", 15);
		map.put("Elder Guardian", 10);
		return map;
	}

	@OnlyIn(Dist.CLIENT)
	public static LivingEntity getEntityByName(String name, World world)
	{
		LivingEntity e = null;
		Constructor constructor = null;
		if(world == null) return null;
		EntityType type = getClassesByName().get(name);
		if(type == EntityType.PLAYER)
		{
			e = Minecraft.getInstance().player;
		}
		else
		{
			e = (LivingEntity) type.create(world);
		}
		return e;
	}


	public static enum Job
	{
		HUNTER(0, "hunter"),
		MAGICIAN(1, "magician"),
		FARMER(2, "farmer"),
		MINER(3, "miner"),
		NONE(-1, "none");
		
		public int index;
		public String name;
		
		Job(int index, String name) 
		{
			this.index = index;
			this.name = name;
		}
		
		public static Job byIndex(int index)
		{
			switch(index)
			{
			case 0:
				return HUNTER;
			case 1:
				return MAGICIAN;
			case 2:
				return FARMER;
			case 3:
				return MINER;
			default:
				return NONE;
			}
		}
		
		public static String[] names()
		{
			return new String[] {HUNTER.name(), MAGICIAN.name(), FARMER.name(), MINER.name()};
		}
		
		public static Job fromString(String str)
		{
			try
			{
				return Job.valueOf(str.toUpperCase());
			}
			catch (IllegalArgumentException e)
			{
				return Job.NONE;
			}
		}
		
	}

	public static enum XPCategories
	{
		CRAFTING(false, 0),
		SMELTING(false, 1),
		BREAKING(false, 2),
		KILLING(false, 3),
		HARVESTING(false, 4),

		XP(true, 0),
		UNLOCK(true, 1);


		public final boolean isCategory;
		public final int index;
		XPCategories(boolean isCategory, int in)
		{
			this.isCategory = isCategory;
			this.index = in;
		}
		
		public static XPCategories[] getXPValues()
		{
			return new XPCategories[] {CRAFTING, SMELTING, BREAKING, KILLING, HARVESTING};
		}

	}

}
