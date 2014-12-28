package mods.battleclasses.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearPacketHandeler;

public class BattleClassesPacketHandeler extends BattlegearPacketHandeler {
	
	public Side side;

	public BattleClassesPacketHandeler() {
		super();
		side = FMLCommonHandler.instance().getEffectiveSide();
		map.put(BattleClassesPacketPlayerClassSnyc.packetName, new BattleClassesPacketPlayerClassSnyc());
		map.put(BattleClassesPacketCooldownSet.packetName, new BattleClassesPacketCooldownSet());
		map.put(BattleClassesPacketChosenAbilityIDSync.packetName, new BattleClassesPacketChosenAbilityIDSync());
		map.put(BattleClassesPacketTalentNodeChosen.packetName, new BattleClassesPacketTalentNodeChosen());
		map.put(BattleClassesPacketTalentSync.packetName, new BattleClassesPacketTalentSync());
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

}
