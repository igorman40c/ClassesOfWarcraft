package mods.battleclasses.ability.passive;

import mods.battleclasses.core.BattleClassesAttributes;
import mods.battleclasses.core.IAmplifyProvider;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

public class BattleClassesTalentAbilityAmplifier extends BattleClassesAbstractTalent implements IAmplifyProvider {

	public BattleClassesTalentAbilityAmplifier(int parAbilityID, int parTalentLevel) {
		super(parAbilityID, parTalentLevel);
		// TODO Auto-generated constructor stub
	}
	
	EnumBattleClassesAmplifierApplyType amplifierApplyType = EnumBattleClassesAmplifierApplyType.BASE_ATTRIBUTE_BONUS;
	BattleClassesAttributes attributes = null;
	
	@Override
	public EnumBattleClassesAmplifierApplyType getApplyType() {
		return amplifierApplyType;
	}

	@Override
	public BattleClassesAttributes getAttributes(int targetAbilityID) {
		return attributes;
	}

	@Override
	public BattleClassesAttributes getAttributes(int targetAbilityID,
			BattleClassesAttributes totalAttributes) {
		return this.getAttributes(targetAbilityID);
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