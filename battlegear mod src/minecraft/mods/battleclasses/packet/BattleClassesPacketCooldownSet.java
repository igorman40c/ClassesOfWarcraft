package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownHolder;
import mods.battleclasses.enumhelper.EnumBattleClassesCooldownType;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;

public class BattleClassesPacketCooldownSet extends AbstractMBPacket {

	public static final String packetName = "BC|CooldownSet";
	
	EnumBattleClassesCooldownType coolDownType = EnumBattleClassesCooldownType.CooldownType_GLOBAL;
	private boolean coolDownForced = false;
	private float coolDownDuration = 0;
	private int coolDownHashCode = -1;
	private String username;
	
	public BattleClassesPacketCooldownSet(EntityPlayer user, int parCoolDownHashCode,
			float parCoolDownDuration, boolean forced, EnumBattleClassesCooldownType type) {
		this.username = user.getCommandSenderName();
    	this.coolDownHashCode = parCoolDownHashCode;
    	this.coolDownDuration = parCoolDownDuration;
    	this.coolDownForced = forced;
    	this.coolDownType = type;
    }

    public BattleClassesPacketCooldownSet() {
    	
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		out.writeBoolean(coolDownForced);
		out.writeFloat(coolDownDuration);
		out.writeInt(coolDownHashCode);
		out.writeInt(coolDownType.ordinal());
        ByteBufUtils.writeUTF8String(out, username);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		coolDownForced = in.readBoolean();
		coolDownDuration = in.readFloat();
		coolDownHashCode = in.readInt();
		coolDownType = EnumBattleClassesCooldownType.values()[in.readInt()];
        username = ByteBufUtils.readUTF8String(in);
        if (username != null && coolDownHashCode != -1) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null){

            	BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
            	
            	ICooldownHolder cooldownHolder = playerHooks.mainCooldownMap.get(coolDownHashCode);
            	if(cooldownHolder != null) {
            		if(this.coolDownType == EnumBattleClassesCooldownType.CooldownType_CANCEL) {
            			cooldownHolder.cancelCooldown();
            		}
            		else {
            			cooldownHolder.setCooldown(coolDownDuration, coolDownForced, coolDownType);
            		}
            	}
            }
        }
	}
	
}