package mods.battleclasses.attributes;

import net.minecraft.util.StatCollector;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;

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

	@Override
	public String getTranslatedDescription() {
		String multipliersList = "";
		int i = 0;
		for(EnumBattleClassesAttributeType attributeType : this.attributes.getActiveMultiplierTypes()) {
			if(i > 0) {
				multipliersList += ", ";
			}
			float amplifierValue = this.attributes.getValueByType(attributeType);
			multipliersList += StatCollector.translateToLocal((amplifierValue > 1) ? 
					"bceffect.modifier.increase" : "bceffect.modifier.reduce");
			String amplifierString = StatCollector.translateToLocal("bcattribute.modifier.amplifier");
			amplifierString.replace("%1$s", attributeType.getTranslatedName());
			amplifierString.replace("%2$s", String.format("%.0f", 100F*amplifierValue));
			multipliersList += " " + amplifierString;
			++i;
		}
		return multipliersList;
	}
	
}