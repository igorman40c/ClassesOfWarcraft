package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;

public class BattleClassesPacketPlayerCastingDidStop extends AbstractMBPacket {

	public static final String packetName = "BC|PlayerCastingDidStop";

	private String username; 
	
	public BattleClassesPacketPlayerCastingDidStop(EntityPlayer user) {
		this.username = user.getCommandSenderName();
    }
	
	BattleClassesPacketPlayerCastingDidStop() {
		
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		ByteBufUtils.writeUTF8String(out, username);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		this.username = ByteBufUtils.readUTF8String(in);
        if (username != null) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){
            	if(Minecraft.getMinecraft().thePlayer != entityPlayer) {
            		entityPlayer.stopUsingItem();
            	}
            	BattleClassesClientEvents.activityRegistry.removeActivity(username);
            }
        }
	}
}