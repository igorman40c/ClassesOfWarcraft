package mods.battleclasses.packet;

import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.sound.CastingSound;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;

public class BattleClassesPacketCastingSound extends AbstractMBPacket {

	private String userName;
	private String resourceName;
	private boolean startSound;
    public static final String packetName = "BC|CastingSound";

    public BattleClassesPacketCastingSound() {
    	
    }
    
    public BattleClassesPacketCastingSound(EntityPlayer entityPlayer) {
    	this.startSound = false;
    	this.userName = entityPlayer.getCommandSenderName();
    } 
    
    public BattleClassesPacketCastingSound(EntityPlayer entityPlayer, String resource, boolean start) {
    	this.startSound = start;
    	this.userName = entityPlayer.getCommandSenderName();
    	this.resourceName = resource;
    }
    
    @Override
	public void write(ByteBuf out) {
		out.writeBoolean(this.startSound);
        ByteBufUtils.writeUTF8String(out, userName);
        if(this.startSound) {
        	ByteBufUtils.writeUTF8String(out, resourceName);
        }
	}

    @Override
    public void process(ByteBuf in, EntityPlayer player) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if(side == Side.CLIENT) {
        	Minecraft mc = Minecraft.getMinecraft();
        	this.startSound = in.readBoolean();
        	this.userName = ByteBufUtils.readUTF8String(in);
        	if(startSound) {
        		this.resourceName = ByteBufUtils.readUTF8String(in);
        		CastingSound.startCastingSound(userName, resourceName);
        	}
        	else {
        		CastingSound.stopCastingSound(userName);
        	}
        }
    }

	@Override
	public String getChannel() {
		return packetName;
	}

	
}

