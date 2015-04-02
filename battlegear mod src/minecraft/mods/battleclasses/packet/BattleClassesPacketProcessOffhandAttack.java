package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesWeaponHitHandler;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;

public class BattleClassesPacketProcessOffhandAttack extends AbstractMBPacket {

	public static final String packetName = "BC|ProcessOffhandAttack";
	private int targetEntityID = -1 ;
	private String username;
		
	public BattleClassesPacketProcessOffhandAttack(EntityPlayer user, int parTargetEntityID) {
    	this.targetEntityID =  parTargetEntityID;
    	this.username = user.getCommandSenderName();
    }

    public BattleClassesPacketProcessOffhandAttack() {
    	
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		out.writeInt(targetEntityID);
        ByteBufUtils.writeUTF8String(out, username);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		targetEntityID = in.readInt();
        username = ByteBufUtils.readUTF8String(in);
        if (player != null && targetEntityID != -1) {
        	Entity target = BattleClassesUtils.getEntityByID(targetEntityID, player.getEntityWorld());
			if(target instanceof EntityLivingBase) {
				EntityLivingBase targetEntity = (EntityLivingBase) target;
				BattleClassesWeaponHitHandler weaponHitHandler = BattleClassesUtils.getPlayerWeaponHandler(player);
				weaponHitHandler.processOffhandAttack(player, targetEntity);
			}
        }
	}

}
