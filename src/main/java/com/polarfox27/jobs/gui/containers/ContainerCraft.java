package com.polarfox27.jobs.gui.containers;

import java.util.Optional;

import com.polarfox27.jobs.ModJobs;
import com.polarfox27.jobs.data.GainXPUtil;
import com.polarfox27.jobs.data.JobsInfo;
import com.polarfox27.jobs.data.PlayerData;
import com.polarfox27.jobs.util.Constants.Job;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerCraft extends RecipeBookContainer<CraftingInventory> {
	   private final CraftingInventory craftSlots = new CraftingInventory(this, 3, 3);
	   private final CraftResultInventory resultSlots = new CraftResultInventory();
	   private final IWorldPosCallable access;
	   private final PlayerEntity player;

	   public ContainerCraft(int size, PlayerInventory player) 
	   {
	      this(size, player, IWorldPosCallable.NULL);
	   }

	   public ContainerCraft(int size, PlayerInventory playerInventory, IWorldPosCallable access) 
	   {
	      super(ContainerType.CRAFTING, size);
	      this.access = access;
	      this.player = playerInventory.player;
	      this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));

	      for(int i = 0; i < 3; ++i) 
	      {
	         for(int j = 0; j < 3; ++j) 
	         {
	            this.addSlot(new Slot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
	         }
	      }

	      for(int k = 0; k < 3; ++k) 
	      {
	         for(int i1 = 0; i1 < 9; ++i1) 
	         {
	            this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	         }
	      }

	      for(int l = 0; l < 9; ++l) 
	      {
	         this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
	      }

	   }

	   public void slotsChanged(IInventory inventory) 
	   {
	      this.access.execute((world, pos) -> {
	         slotChangedCraftingGrid(this.containerId, world, this.player, this.craftSlots, this.resultSlots);
	      });
	   }

	   public void fillCraftSlotsStackedContents(RecipeItemHelper recipeHelper) 
	   {
	      this.craftSlots.fillStackedContents(recipeHelper);
	   }

	   public void clearCraftingContent() 
	   {
	      this.craftSlots.clearContent();
	      this.resultSlots.clearContent();
	   }

	   public boolean recipeMatches(IRecipe<? super CraftingInventory> recipe) 
	   {
	      return recipe.matches(this.craftSlots, this.player.level);
	   }

	   public void removed(PlayerEntity player) 
	   {
	      super.removed(player);
	      this.access.execute((world, pos) -> {
	         this.clearContainer(player, world, this.craftSlots);
	      });
	   }

	   public boolean stillValid(PlayerEntity player) 
	   {
	      return stillValid(this.access, player, Blocks.CRAFTING_TABLE);
	   }

	   public ItemStack quickMoveStack(PlayerEntity player, int slotID) 
	   {
	      ItemStack itemstack = ItemStack.EMPTY;
	      Slot slot = this.slots.get(slotID);
	      if (slot != null && slot.hasItem()) 
	      {
	         ItemStack itemstack1 = slot.getItem();
	         itemstack = itemstack1.copy();
	         if (slotID == 0) 
	         {
	            this.access.execute((p_217067_2_, p_217067_3_) -> {
	               itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, player);
	            });
	            if (!this.moveItemStackTo(itemstack1, 10, 46, true)) 
	            {
	               return ItemStack.EMPTY;
	            }

	            slot.onQuickCraft(itemstack1, itemstack);
	         } 
	         else if (slotID >= 10 && slotID < 46) 
	         {
	            if (!this.moveItemStackTo(itemstack1, 1, 10, false)) 
	            {
	               if (slotID < 37) {
	                  if (!this.moveItemStackTo(itemstack1, 37, 46, false)) 
	                  {
	                     return ItemStack.EMPTY;
	                  }
	               } 
	               else if (!this.moveItemStackTo(itemstack1, 10, 37, false)) 
	               {
	                  return ItemStack.EMPTY;
	               }
	            }
	         } 
	         else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) 
	         {
	            return ItemStack.EMPTY;
	         }

	         if (itemstack1.isEmpty()) 
	         {
	            slot.set(ItemStack.EMPTY);
	         } 
	         else 
	         {
	            slot.setChanged();
	         }

	         if (itemstack1.getCount() == itemstack.getCount()) 
	         {
	            return ItemStack.EMPTY;
	         }

	         ItemStack itemstack2 = slot.onTake(player, itemstack1);
	         if (slotID == 0) {
	            player.drop(itemstack2, false);
	         }
	      }

	      return itemstack;
	   }

	   public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) 
	   {
	      return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
	   }

	   public int getResultSlotIndex() 
	   {
	      return 0;
	   }

	   public int getGridWidth() 
	   {
	      return this.craftSlots.getWidth();
	   }

	   public int getGridHeight() 
	   {
	      return this.craftSlots.getHeight();
	   }

	   @OnlyIn(Dist.CLIENT)
	   public int getSize() 
	   {
	      return 10;
	   }
	   
	   protected static void slotChangedCraftingGrid(int index, World world, PlayerEntity player, CraftingInventory matrix, CraftResultInventory result)
	   {
		      if (!world.isClientSide) 
		      {
		         ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
		         JobsInfo jobs = PlayerData.getPlayerJobs(serverplayerentity);
		         ItemStack itemstack = ItemStack.EMPTY;
		         Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, matrix, world);
		         if (optional.isPresent()) 
		         {
		            ICraftingRecipe icraftingrecipe = optional.get();
		            if (result.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) 
		            {
		               itemstack = icraftingrecipe.assemble(matrix);
		            }
		            if(GainXPUtil.CRAFT_UNLOCK_JOB.containsKey(itemstack.getItem()))
		            {
		                Job j = GainXPUtil.CRAFT_UNLOCK_JOB.get(itemstack.getItem());
		                int lvl = GainXPUtil.CRAFT_UNLOCK_LVL.get(itemstack.getItem());
		                if(lvl > jobs.getLevelByJob(j))
		                    itemstack = ItemStack.EMPTY;
		            }
		         }

		         result.setItem(0, itemstack);
		         serverplayerentity.connection.send(new SSetSlotPacket(index, 0, itemstack));
		      }
		}

	   @OnlyIn(Dist.CLIENT)
	   public RecipeBookCategory getRecipeBookType() 
	   {
	      return RecipeBookCategory.CRAFTING;
	   }
}
