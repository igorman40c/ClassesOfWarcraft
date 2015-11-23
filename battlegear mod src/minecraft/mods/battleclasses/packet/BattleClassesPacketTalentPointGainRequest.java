package mods.battleclasses.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesPacketTalentPointGainRequest extends AbstractMBPacket {
	
	public static final String packetName = "BC|TalentPointGainRequest";

	private String username;
	
	public BattleClassesPacketTalentPointGainRequest(EntityPlayer user) {
		this.username = user.getCommandSenderName();
    }
	
	public BattleClassesPacketTalentPointGainRequest() {
		
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
        username = ByteBufUtils.readUTF8String(in);
        if (username != null) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){
            	BattleClassesUtils.getPlayerTalentMatrix(entityPlayer).gainTalentPoint(entityPlayer);
            }
        }
	}
}