package mods.battleclasses.attributes;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

public class BattleClassesAttributeModifierAmplifier extends BattleClassesAbstractAttributeModifier {

	public BattleClassesAttributeModifierAmplifier(BattleClassesAttributes attributes) {
		super(attributes);
	}
	
	public BattleClassesAttributeModifierAmplifier(BattleClassesAttributes attributes, IAbilityCriteria applyCriteria) {
		super(attributes, applyCriteria);
	}

	@Override
	public EnumBattleClassesAmplifierApplyType getApplyType() {
		return EnumBattleClassesAmplifierApplyType.BASE_ATTRIBUTE_AMPLIFIER;
	}

	@Override
	protected BattleClassesAttributes applyAttributeModifier(BattleClassesAttributes accumulatedAttributes) {
		accumulatedAttributes.multiply(this.attributes);
		return accumulatedAttributes;
	}
	
}