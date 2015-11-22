package mods.battleclasses.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesPacketAbilityRankUpRequest extends AbstractMBPacket {
	
	public static final String packetName = "BC|AbilityRankUpRequest";
	public static final String abilityNullID = "abilityNullID";
	
	private String abilityID = abilityNullID;
	private String username;
	
	public BattleClassesPacketAbilityRankUpRequest(EntityPlayer user, String parAbilityID) {
		this.username = user.getCommandSenderName();
    	this.abilityID = parAbilityID;
    }
	
	public BattleClassesPacketAbilityRankUpRequest() {
		
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		ByteBufUtils.writeUTF8String(out, abilityID);
        ByteBufUtils.writeUTF8String(out, username);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		abilityID = ByteBufUtils.readUTF8String(in);
        username = ByteBufUtils.readUTF8String(in);
        if (username != null && abilityID!=null && !abilityID.equals(abilityNullID)) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){
            	BattleClassesUtils.getPlayerSpellBook(entityPlayer).rankUpAbilityById(player, abilityID);
            }
        }
	}
}
