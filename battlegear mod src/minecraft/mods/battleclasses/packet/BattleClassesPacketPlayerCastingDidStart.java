package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.CooldownClock;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;

public class BattleClassesPacketPlayerCastingDidStart extends AbstractMBPacket {

	public static final String packetName = "BC|PlayerCastingDidStart";

	private String username;
	private String abilityID = "";
	private float duration = 0; 
	
	public BattleClassesPacketPlayerCastingDidStart(EntityPlayer user, String abilityID, float duration) {
		this.username = user.getCommandSenderName();
    	this.abilityID = abilityID;
    	this.duration = duration;
    }
	
	BattleClassesPacketPlayerCastingDidStart() {
		
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		ByteBufUtils.writeUTF8String(out, username);
		ByteBufUtils.writeUTF8String(out, abilityID);
		out.writeFloat(this.duration);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		this.username = ByteBufUtils.readUTF8String(in);
		this.abilityID = ByteBufUtils.readUTF8String(in);
    	this.duration = in.readFloat();
        if (username != null && abilityID!= null && !abilityID.equals("")) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){
            	if(Minecraft.getMinecraft().thePlayer != entityPlayer) {
            		BattleClassesUtils.setEntityPlayerItemInUseInSeconds(entityPlayer, entityPlayer.getCurrentEquippedItem(), this.duration);
            	}
            	BattleClassesClientEvents.activityRegistry.addActivity(username, abilityID, duration);
            }
        }
	}
}