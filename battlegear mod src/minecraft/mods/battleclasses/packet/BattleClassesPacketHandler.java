package mods.battleclasses.packet;

import java.util.Hashtable;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.AbstractMBPacket;
import mods.battlegear2.packet.BattlegearPacketHandeler;

public class BattleClassesPacketHandler {
	
	public Side side;
	
    public Map<String, AbstractMBPacket> map = new Hashtable<String, AbstractMBPacket>();
    public Map<String, FMLEventChannel> channels = new Hashtable<String, FMLEventChannel>();

	public BattleClassesPacketHandler() {
		side = FMLCommonHandler.instance().getEffectiveSide();
		map.put(BattleClassesPacketPlayerClassSnyc.packetName, new BattleClassesPacketPlayerClassSnyc());
		map.put(BattleClassesPacketCooldownSet.packetName, new BattleClassesPacketCooldownSet());
		map.put(BattleClassesPacketChosenAbilityIDSync.packetName, new BattleClassesPacketChosenAbilityIDSync());
		map.put(BattleClassesPacketTalentNodeChosen.packetName, new BattleClassesPacketTalentNodeChosen());
		map.put(BattleClassesPacketTalentSync.packetName, new BattleClassesPacketTalentSync());
		map.put(BattleClassesPacketGuiTabSwitch.packetName, new BattleClassesPacketGuiTabSwitch());
		map.put(BattleClassesPacketProcessAbilityWithTarget.packetName, new BattleClassesPacketProcessAbilityWithTarget());
		map.put(BattleClassesPacketProcessOffhandAttack.packetName, new BattleClassesPacketProcessOffhandAttack());
		map.put(BattleClassesPacketPlayerDataSync.packetName, new BattleClassesPacketPlayerDataSync());
		map.put(BattleClassesPacketAttributeChanges.packetName, new BattleClassesPacketAttributeChanges());
		map.put(BattleClassesPacketCastingSound.packetName, new BattleClassesPacketCastingSound());
		map.put(BattleClassesPacketPlayerCastingDidStart.packetName, new BattleClassesPacketPlayerCastingDidStart());
		map.put(BattleClassesPacketPlayerCastingDidStop.packetName, new BattleClassesPacketPlayerCastingDidStop());
		map.put(BattleClassesPacketPlayerCastingDidRelease.packetName, new BattleClassesPacketPlayerCastingDidRelease());
	}
	
	public void registerChannels() {
		FMLEventChannel eventChannel;
        for(String channel : this.map.keySet()){
            eventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channel);
            eventChannel.register(this);
            this.channels.put(channel, eventChannel);
        }
	}
	
	public void sendPacketToPlayerWithSideCheck(FMLProxyPacket packet, EntityPlayerMP player){
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.SERVER) {
			sendPacketToPlayer(packet,player);
		}
	}

    public void sendPacketToServerWithSideCheck(FMLProxyPacket packet){
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT) {
			sendPacketToServer(packet);
		}
    }

    public void sendPacketAroundWithSideCheck(Entity entity, double range, FMLProxyPacket packet){
    	sendPacketAround(entity,range,packet);
    }

    public void sendPacketToAllWithSideCheck(FMLProxyPacket packet){
    	sendPacketToAll(packet);
    }
    
    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
        map.get(event.packet.channel()).process(event.packet.payload(), ((NetHandlerPlayServer)event.handler).playerEntity);
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event){
        map.get(event.packet.channel()).process(event.packet.payload(), Battlegear.proxy.getClientPlayer());
    }

    public void sendPacketToPlayer(FMLProxyPacket packet, EntityPlayerMP player){
        channels.get(packet.channel()).sendTo(packet, player);
    }

    public void sendPacketToServer(FMLProxyPacket packet){
        channels.get(packet.channel()).sendToServer(packet);
    }

    public void sendPacketAround(Entity entity, double range, FMLProxyPacket packet){
        channels.get(packet.channel()).sendToAllAround(packet, new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, range));
    }

    public void sendPacketToAll(FMLProxyPacket packet){
        channels.get(packet.channel()).sendToAll(packet);
    }

}
