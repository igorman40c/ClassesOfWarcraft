package mods.battleclasses.attributes;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

public class BattleClassesAttributeModifierBonus extends BattleClassesAbstractAttributeModifier {

	public BattleClassesAttributeModifierBonus(BattleClassesAttributes attributes) {
		super(attributes);
	}
	
	public BattleClassesAttributeModifierBonus(BattleClassesAttributes attributes, IAbilityCriteria applyCriteria) {
		super(attributes, applyCriteria);
	}

	@Override
	public EnumBattleClassesAmplifierApplyType getApplyType() {
		return EnumBattleClassesAmplifierApplyType.BASE_ATTRIBUTE_BONUS;
	}

	@Override
	protected BattleClassesAttributes applyAttributeModifier(BattleClassesAttributes accumulatedAttributes) {
		accumulatedAttributes.add(this.attributes);
		return accumulatedAttributes;
	}
	
}
