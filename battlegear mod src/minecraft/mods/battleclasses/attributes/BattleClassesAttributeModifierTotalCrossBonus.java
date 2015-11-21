package mods.battleclasses.attributes;

import net.minecraft.util.StatCollector;
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
	
	@Override
	public String getTranslatedDescription() {
		String description = StatCollector.translateToLocal((this.multiplier > 1) ? 
				"bceffect.modifier.increase" : "bceffect.modifier.reduce");
		String amplifierString = StatCollector.translateToLocal("bcattribute.modifier.crossbonus");
		amplifierString = amplifierString.replace("%1$s", toType.getTranslatedName());
		amplifierString = amplifierString.replace("%2$s", String.format("%.0f", 100F*this.multiplier));
		amplifierString = amplifierString.replace("%3$s", fromType.getTranslatedName());
		description += " " + amplifierString;
		
		if(this.applyCriteria != null) {
			description+= " " + StatCollector.translateToLocal("bcattribute.modifier.precriteria");
			description+= " " + this.applyCriteria.getTranslatedDescription();
		}

		return description;
	}
}