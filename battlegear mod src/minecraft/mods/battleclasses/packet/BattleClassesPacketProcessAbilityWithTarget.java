package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.passive.BattleClassesAbstractTalent;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class BattleClassesPacketProcessAbilityWithTarget extends AbstractMBPacket {

	public static final String packetName = "BC|ProcessAbilityWithTarget";
	private int abilityID = -1 ;
	private int targetEntityID = -1 ;
	private int tickCount = -1 ;
	private String username;
		
	public BattleClassesPacketProcessAbilityWithTarget(EntityPlayer user, int parAbilityID, int parTargetEntityID, int parTickCount) {
    	this.abilityID = parAbilityID;
    	this.targetEntityID =  parTargetEntityID;
    	this.tickCount = parTickCount;
    	this.username = user.getCommandSenderName();
    }

    public BattleClassesPacketProcessAbilityWithTarget() {
    	
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		out.writeInt(abilityID);
		out.writeInt(targetEntityID);
		out.writeInt(tickCount);
        ByteBufUtils.writeUTF8String(out, username);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		abilityID = in.readInt();
		targetEntityID = in.readInt();
		tickCount = in.readInt();
        username = ByteBufUtils.readUTF8String(in);
        if (username != null && abilityID != -1 && targetEntityID != -1) {
            EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByName(username);
            if(entityPlayer!=null) {
            	BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(entityPlayer);
            	if (playerHooks != null) {
            		BattleClassesAbstractAbilityActive activeAbility = playerHooks.playerClass.spellBook.getActiveAbilitiyByID(abilityID);
            		if(activeAbility != null) {
            			Entity target = BattleClassesUtils.getEntityByID(targetEntityID, entityPlayer.getEntityWorld());
            			if(target instanceof EntityLivingBase) {
            				EntityLivingBase targetEntity = (EntityLivingBase) target;
            				activeAbility.finishCastingWithTarget(targetEntity, tickCount);
            			}
            		}
            	}
            }
        }
	}

}
