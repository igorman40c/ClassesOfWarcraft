package mods.battleclasses.gui.controlls;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
import mods.battleclasses.packet.BattleClassesPacketAbilityRankUpRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

public class BattleClassesGuiButtonAbilityRank extends BattleClassesGuiButton {

	public BattleClassesAbstractAbilityActive ability;
	public BattleClassesGuiButtonAbilityRank(int id, BattleClassesAbstractAbilityActive parAbility) {
		super(id, 0, 0, 40, 18, "");
		this.trunctationMargins = 4F;
		this.showHoveringText = true;
		this.hoveringTextString = "FEATURE IN DEVELOPMENT!:(";
		this.ability = parAbility;
		this.setOrigin(36, 202);
		this.resource = BattleClassesTabSpellbook.resource;
		this.updateDisplayTitle();
	}
	
	public void updateDisplayTitle() {
		String rankString = "";
		int abilityRank = this.ability.getCurrentRank();
		if(abilityRank == 0) {
			rankString = StatCollector.translateToLocal("bcgui.ability_rank.zero");
		}
		else {
			rankString = StatCollector.translateToLocal("bcgui.ability_rank");
			String rankNumber = "" + abilityRank;
			rankString = rankString.replace("%1$s", rankNumber);
		}
		
		this.setDisplayString(rankString);
	}

	public boolean shouldBeDisabled() {
		return this.ability.getCurrentRank() >= this.ability.getFinalRank();
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		boolean press = inWindow;
		if (press) {
			FMLProxyPacket p = new BattleClassesPacketAbilityRankUpRequest(mc.thePlayer, this.ability.getAbilityID()).generatePacket();
			BattleClassesMod.packetHandler.sendPacketToServer(p);
		}
		return press;
	}
	
	/*
	@Override
	public List<String> getTooltipText() {
		ArrayList<String> stringList = new ArrayList<String>();
    	stringList.add(this.hoveringTextString);
    	return stringList;
	}
	*/
 
}
