package mods.battleclasses.attributes;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;

public class BattleClassesAttributeModifierTotalCrossBonus extends BattleClassesAbstractAttributeModifier {

	protected EnumBattleClassesAttributeType fromType;
	protected EnumBattleClassesAttributeType toType;
	protected float multiplier = 0;
	
	public BattleClassesAttributeModifierTotalCrossBonus(EnumBattleClassesAttributeType fromType, EnumBattleClassesAttributeType toType, 
			float multiplier) {
		super();
		this.fromType = fromType;
		this.toType = toType;
		this.multiplier = multiplier;
	}
	
	public BattleClassesAttributeModifierTotalCrossBonus(EnumBattleClassesAttributeType fromType, EnumBattleClassesAttributeType toType, 
			float multiplier, IAbilityCriteria applyCriteria) {
		this(fromType, toType, multiplier);
		this.applyCriteria = applyCriteria;
	}

	@Override
	public EnumBattleClassesAmplifierApplyType getApplyType() {
		return EnumBattleClassesAmplifierApplyType.TOTAL_BASED_ATTRIBUTE_BONUS;
	}
	
	@Override
	protected BattleClassesAttributes applyAttributeModifier(BattleClassesAttributes accumulatedAttributes) {
		float bonusValue =  accumulatedAttributes.getValueByType(fromType) * this.multiplier;
		accumulatedAttributes.setValueByType(toType, accumulatedAttributes.getValueByType(toType) + bonusValue);
		return accumulatedAttributes;
	}
	
}