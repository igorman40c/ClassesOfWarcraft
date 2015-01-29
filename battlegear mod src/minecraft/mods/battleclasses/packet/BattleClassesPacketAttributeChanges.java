package mods.battleclasses.packet;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesPacketAttributeChanges extends AbstractMBPacket {

    public static final String packetName = "BC|AttributeChanges";

    public BattleClassesPacketAttributeChanges() {
    }

    @Override
    public void process(ByteBuf in, EntityPlayer player) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if(side == Side.SERVER) {
        	BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(player);
        	if(playerHooks != null) {
        		playerHooks.onAttributeSourcesChanged();
        	}
        }
        else {
        	Minecraft mc = Minecraft.getMinecraft();
        	BattleClassesPlayerHooks playerHooks = BattleClassesUtils.getPlayerHooks(mc.thePlayer);
        	if(playerHooks != null) {
        		playerHooks.onAttributeSourcesChanged();
        	}
        }
    }

	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		
	}

	
}
