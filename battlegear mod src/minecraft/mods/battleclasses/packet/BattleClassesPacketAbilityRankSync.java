package mods.battleclasses.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
import mods.battlegear2.packet.AbstractMBPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesPacketAbilityRankSync extends AbstractMBPacket {
	
	public static final String packetName = "BC|AbilityRankSync";
	public static final String abilityNullID = "abilityNullID";
	
	private String abilityID = abilityNullID;
	private int rank = 0;
	
	public BattleClassesPacketAbilityRankSync(String parAbilityID, int rank) {
    	this.abilityID = parAbilityID;
    	this.rank = rank;
    }
	
	public BattleClassesPacketAbilityRankSync() {
		
	}
	
	@Override
	public String getChannel() {
		return packetName;
	}

	@Override
	public void write(ByteBuf out) {
		ByteBufUtils.writeUTF8String(out, abilityID);
        out.writeInt(this.rank);
	}

	@Override
	public void process(ByteBuf in, EntityPlayer player) {
		BattleClassesUtils.Log("Trying to process " + this.getClass() , LogType.PACKET);
		abilityID = ByteBufUtils.readUTF8String(in);
		rank = in.readInt();		
		Minecraft mc = Minecraft.getMinecraft();
		BattleClassesUtils.getPlayerSpellBook(player).setAbilityRankById(player, rank, abilityID);
		if(mc.currentScreen instanceof BattleClassesTabSpellbook) {
			BattleClassesTabSpellbook spellBookGui = (BattleClassesTabSpellbook) mc.currentScreen;
			spellBookGui.updateRankButtons();
		}
	}
	
}
