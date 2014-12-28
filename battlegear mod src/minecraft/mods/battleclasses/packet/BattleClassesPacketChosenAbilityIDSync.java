package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownHolder;
import mods.battlegear2.packet.AbstractMBPacket;

public class BattleClassesPacketChosenAbilityIDSync extends AbstractMBPacket {
	
	public static final String packetName = "BC|ChosenAbilityIDSync";
	
	private int abilityID = -1;
	private String username;
	
	public BattleClassesPacketChosenAbilityIDSync(EntityPlayer user, int parAbilityID) {
		this.username = user.getCommandSenderName();
    	this.abilityID = parAbilityID;
    }
	
	public BattleClassesPacketChosenAbilityIDSync() {
		
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		out.writeInt(abilityID);
        ByteBufUtils.writeUTF8String(out, username);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		
		abilityID = in.readInt();
        username = ByteBufUtils.readUTF8String(in);
        if (username != null && abilityID != -1) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){
            	BattleClassesUtils.getPlayerSpellBook(entityPlayer).setChosenAbilityID(abilityID);
            }
        }
	}
}
