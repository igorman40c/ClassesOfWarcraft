package mods.battleclasses.ability.passive;

import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.ICWAttributeModifierOwner;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

//TODO

public class BattleClassesTalentAbilityAmplifier extends BattleClassesAbstractTalent /*implements ICWAttributeModifierOwner*/ {

	public BattleClassesTalentAbilityAmplifier(int parAbilityID, int parTalentLevel) {
		super(parAbilityID, parTalentLevel);
		// TODO Auto-generated constructor stub
	}	
	
	public void onStateChanged() {
		super.onStateChanged();
		if(this.getCurrentState() == 0) {
			this.playerHooks.playerClass.spellBook.unLearnAbility(this);
		}
		else {
			this.playerHooks.playerClass.spellBook.learnAbility(this);
		}
	}
}