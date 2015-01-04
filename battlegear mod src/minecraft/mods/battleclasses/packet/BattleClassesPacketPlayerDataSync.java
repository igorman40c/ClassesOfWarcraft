package mods.battleclasses.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;

public class BattleClassesPacketPlayerDataSync extends AbstractMBPacket {

	public static final String packetName = "BC|PlayerDataSync";
	private NBTTagCompound tagCompound = null;
	//private String username;
	
	public BattleClassesPacketPlayerDataSync(NBTTagCompound nbttagcompound) {
    	this.tagCompound = nbttagcompound;
    }

    public BattleClassesPacketPlayerDataSync() {
    	
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		if(tagCompound != null) {
			ByteBufOutputStream outputStream = new ByteBufOutputStream(out);
			try {
				CompressedStreamTools.writeCompressed(tagCompound, outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		//tickCount = in.readInt();
        //username = ByteBufUtils.readUTF8String(in);
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if(side == Side.SERVER) {
			System.out.println("Recieved PlayerDataSync packet from Client. Sending data in response.");
			FMLProxyPacket p = new BattleClassesPacketPlayerDataSync(BattleClassesUtils.getPlayerHooks(player).writeTagCompound()).generatePacket();
			if(player instanceof EntityPlayerMP) {
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
				BattleClassesMod.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
			}
		}
        else {
        	System.out.println("Recieved PlayerDataSync packet from Server. Processing data.");
        	ByteBufInputStream inputStream = new ByteBufInputStream(in);
        	try {
				this.tagCompound = CompressedStreamTools.readCompressed(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
        	BattleClassesUtils.getPlayerHooks(player).readTagCompound(this.tagCompound);
        }
        /*
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
            				activeAbility.proceedAbility(targetEntity, tickCount);
            			}
            		}
            	}
            }
        }
        */
	}
}