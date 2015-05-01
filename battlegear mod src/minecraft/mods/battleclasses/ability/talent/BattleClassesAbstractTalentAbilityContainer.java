package mods.battleclasses.ability.talent;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;

public abstract class BattleClassesAbstractTalentAbilityContainer extends BattleClassesAbstractTalent {

	public BattleClassesAbstractTalentAbilityContainer(int parAbilityID, int parTalentLevel, BattleClassesAbstractAbility parAbility) {
		super(parAbilityID, parTalentLevel);
		this.containedAbility = parAbility;
	}
	
	BattleClassesAbstractAbility containedAbility;
	
	public void onStateChanged() {
		super.onStateChanged();
		if(this.getCurrentState() == 0) {
			this.playerHooks.playerClass.spellBook.unLearnAbility(containedAbility);
		}
		else {
			this.playerHooks.playerClass.spellBook.learnAbility(containedAbility);
		}
	}

}
