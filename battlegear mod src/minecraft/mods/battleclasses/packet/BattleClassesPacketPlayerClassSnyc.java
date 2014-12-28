package mods.battleclasses.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battlegear2.api.core.IBattlePlayer;
import mods.battlegear2.packet.AbstractMBPacket;
import mods.battlegear2.utils.EnumBGAnimations;

public class BattleClassesPacketPlayerClassSnyc extends AbstractMBPacket {

	public static final String packetName = "BC|PlayerClassSync";
	private EnumBattleClassesPlayerClass playerClass;
	private String username;
	
	public BattleClassesPacketPlayerClassSnyc(EntityPlayer user, EnumBattleClassesPlayerClass parClass) {
    	this.playerClass = parClass;
    	this.username = user.getCommandSenderName();
    }

    public BattleClassesPacketPlayerClassSnyc() {
    	
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		out.writeInt(playerClass.ordinal());
        ByteBufUtils.writeUTF8String(out, username);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		playerClass = EnumBattleClassesPlayerClass.values()[in.readInt()];
        username = ByteBufUtils.readUTF8String(in);
        if (username != null && playerClass != null) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){

            	BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
            	
            	playerHooks.switchToPlayerClass(playerClass);
            }
        }
	}

}
