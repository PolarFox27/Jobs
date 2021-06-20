package com.polarfox27.jobs.util.handler;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.polarfox27.jobs.network.PacketAddXP;
import com.polarfox27.jobs.network.PacketAskClientUpdate;
import com.polarfox27.jobs.network.PacketLevelUp;
import com.polarfox27.jobs.network.PacketSendChatMessage;
import com.polarfox27.jobs.network.PacketSendRewardsClient;
import com.polarfox27.jobs.network.PacketUpdateClientInfos;
import com.polarfox27.jobs.network.PacketUpdateClientJob;
import com.polarfox27.jobs.util.Reference;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation(Reference.MOD_ID, "main"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	
	public static void registerPackets()
	{
		INSTANCE.registerMessage(0, PacketAddXP.class, PacketAddXP::toBytes, PacketAddXP::fromBytes, PacketAddXP::handle);
		INSTANCE.registerMessage(1, PacketAskClientUpdate.class, PacketAskClientUpdate::toBytes, PacketAskClientUpdate::fromBytes, PacketAskClientUpdate::handle);
		INSTANCE.registerMessage(2, PacketLevelUp.class, PacketLevelUp::toBytes, PacketLevelUp::fromBytes, PacketLevelUp::handle);
		INSTANCE.registerMessage(3, PacketSendRewardsClient.class, PacketSendRewardsClient::toBytes, PacketSendRewardsClient::fromBytes, PacketSendRewardsClient::handle);
		INSTANCE.registerMessage(4, PacketUpdateClientInfos.class, PacketUpdateClientInfos::toBytes, PacketUpdateClientInfos::fromBytes, PacketUpdateClientInfos::handle);
		INSTANCE.registerMessage(5, PacketUpdateClientJob.class, PacketUpdateClientJob::toBytes, PacketUpdateClientJob::fromBytes, PacketUpdateClientJob::handle);
		INSTANCE.registerMessage(6, PacketSendChatMessage.class, PacketSendChatMessage::toBytes, PacketSendChatMessage::fromBytes, PacketSendChatMessage::handle);
	}
	
	
	public static void sendMessageToClient(ServerPlayerEntity player, ITextComponent message)
	{
		INSTANCE.sendTo(new PacketSendChatMessage(message), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}

}
