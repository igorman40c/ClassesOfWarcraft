package mods.battleclasses.gui.controlls;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
import net.minecraft.util.StatCollector;

public class BattleClassesGuiButtonAbilityUpgrade extends BattleClassesGuiButton {

	public BattleClassesAbstractAbilityActive ability;
	public BattleClassesGuiButtonAbilityUpgrade(int id, BattleClassesAbstractAbilityActive parAbility) {
		super(id, 0, 0, 40, 14, StatCollector.translateToLocal("bctab.spellbook.upgrade"));
		this.ability = parAbility;
		this.setOrigin(36, 214);
		this.resource = BattleClassesTabSpellbook.resource;
	}

	public boolean shouldBeDisabled() {
		return true;
	}
}
