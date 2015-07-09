package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;

public class BattleClassesPacketPlayerCastingDidRelease extends AbstractMBPacket {

	public static final String packetName = "BC|PlayerCastingDidRelease";

	private String username;
	private String abilityID = "";
	
	public BattleClassesPacketPlayerCastingDidRelease(EntityPlayer user, String abilityID) {
		this.username = user.getCommandSenderName();
    	this.abilityID = abilityID;
    }
	
	BattleClassesPacketPlayerCastingDidRelease() {
		
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		ByteBufUtils.writeUTF8String(out, username);
		ByteBufUtils.writeUTF8String(out, abilityID);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		this.username = ByteBufUtils.readUTF8String(in);
		this.abilityID = ByteBufUtils.readUTF8String(in);
        if (username != null && abilityID!= null && !abilityID.equals("")) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){
            	BattleClassesClientEvents.activityRegistry.playAbilityLaunchFX(username, abilityID);
            }
        }
	}
}