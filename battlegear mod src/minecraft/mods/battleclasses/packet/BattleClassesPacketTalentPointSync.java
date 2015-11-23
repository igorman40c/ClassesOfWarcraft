package mods.battleclasses.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesPacketTalentPointSync extends AbstractMBPacket {
	
	public static final String packetName = "BC|TalentPointSync";
	private int points = 0;
	
	public BattleClassesPacketTalentPointSync(int points) {
    	this.points = points;
    }
	
	public BattleClassesPacketTalentPointSync() {
		
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
        out.writeInt(this.points);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		points = in.readInt();		
		Minecraft mc = Minecraft.getMinecraft();
		BattleClassesUtils.getPlayerTalentMatrix(player).setCurrentTalentPoints(points);
		/*
		if(mc.currentScreen instanceof BattleClassesTabSpellbook) {
			BattleClassesTabSpellbook spellBookGui = (BattleClassesTabSpellbook) mc.currentScreen;
			spellBookGui.updateRankButtons();
		}
		*/
	}
	
}