package mods.battleclasses.attributes;

import net.minecraft.util.StatCollector;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;

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

	@Override
	public String getTranslatedDescription() {
		String description = "";
		int i = 0;
		for(EnumBattleClassesAttributeType attributeType : this.attributes.getActiveTypes()) {
			if(i > 0) {
				description += ", ";
			}
			float bonusValue = this.attributes.getValueByType(attributeType);
			description += StatCollector.translateToLocal((bonusValue > 1) ? 
					"bceffect.modifier.increase" : "bceffect.modifier.reduce");
			String amplifierString = StatCollector.translateToLocal("bcattribute.modifier.bonus");
			amplifierString.replace("%1$s", attributeType.getTranslatedName());
			amplifierString.replace("%2$s", String.format("%.0f", bonusValue));
			description += " " + amplifierString;
			++i;
		}
		
		if(this.applyCriteria != null) {
			description+= " " + StatCollector.translateToLocal("bcattribute.modifier.precriteria");
			description+= " " + this.applyCriteria.getTranslatedDescription();
		}
		
		return description;
	}
	
}
