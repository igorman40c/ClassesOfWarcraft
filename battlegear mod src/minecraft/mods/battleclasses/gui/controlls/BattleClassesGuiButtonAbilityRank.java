package mods.battleclasses.gui.controlls;

import java.util.ArrayList;
import java.util.List;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
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
		int abilityRank = this.ability.getRank();
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
		return false;
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
