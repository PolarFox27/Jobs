package com.polarfox27.jobs.data;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerData {
	
	@CapabilityInject(JobsInfo.class)
	public static Capability<JobsInfo> JOBS;
	
	
	public static JobsInfo getPlayerJobs(PlayerEntity player)
	{
		return player.getCapability(JOBS, null).resolve().get();
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(JobsInfo.class, new Capability.IStorage<JobsInfo>() 
		{

			@Override
			public INBT writeNBT(Capability<JobsInfo> capability, JobsInfo instance, Direction side) 
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public void readNBT(Capability<JobsInfo> capability, JobsInfo instance, Direction side, INBT nbt) 
			{
				throw new UnsupportedOperationException();
			}
			
		}, () -> null);
	}
	
	public static class JobsDispatcher implements ICapabilitySerializable<CompoundNBT>{

		private JobsInfo jobs = new JobsInfo();

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) 
		{
			return LazyOptional.of(()-> (T)jobs);
		}

		@Override
		public CompoundNBT serializeNBT() 
		{
			return this.jobs.toNBT();
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt) 
		{
			this.jobs.fromNBT(nbt);
		}
		
		
	}

}
