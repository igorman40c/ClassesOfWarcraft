package mods.battleclasses.ability.passive;

import mods.battleclasses.core.BattleClassesAttributes;
import mods.battleclasses.core.ICWAttributeModifier;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

public class BattleClassesPassiveAbilityAmplifier extends BattleClassesAbstractAbilityPassive implements ICWAttributeModifier {
	
	public BattleClassesPassiveAbilityAmplifier(int parAbilityID) {
		super(parAbilityID);
	}
	
	EnumBattleClassesAmplifierApplyType amplifierApplyType = EnumBattleClassesAmplifierApplyType.BASE_ATTRIBUTE_BONUS;
	BattleClassesAttributes attributes = null;
	
	@Override
	public EnumBattleClassesAmplifierApplyType getApplyType() {
		return amplifierApplyType;
	}

	@Override
	public BattleClassesAttributes applyAttributeModifier(int targetAbilityID) {
		return attributes;
	}

	@Override
	public BattleClassesAttributes applyAttributeModifier(int targetAbilityID,
			BattleClassesAttributes totalAttributes) {
		return this.applyAttributeModifier(targetAbilityID);
	}
}
