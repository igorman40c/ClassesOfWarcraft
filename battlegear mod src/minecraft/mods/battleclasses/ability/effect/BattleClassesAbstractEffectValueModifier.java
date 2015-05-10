package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import net.minecraft.util.StatCollector;

public abstract class BattleClassesAbstractEffectValueModifier extends BattleClassesAbstractEffectModifier {

	protected String unlocalizedDescriptionSubject = "";
	
	public BattleClassesAbstractEffectValueModifier(boolean input, IAbilityCriteria applyCriteria, float valueMultiplier) {
		super(input, applyCriteria);
		this.valueMultiplier = valueMultiplier;
	}

	protected float valueMultiplier = 1;
	
	public float getValueMultiplier() {
		return Math.abs(valueMultiplier);
	}
	
	@Override
	public String getTranslatedDescription() {
		if(getValueMultiplier() != 1) {
			String valueMultiplyDescription;
			valueMultiplyDescription = StatCollector.translateToLocal(((getValueMultiplier()>1) ? 
					"bceffect.modifier.increase" : "bceffect.modifier.reduce"));
			valueMultiplyDescription += " " + StatCollector.translateToLocal(this.unlocalizedDescriptionSubject);
			valueMultiplyDescription += " " + StatCollector.translateToLocal(((this.isInputModifier()) ? 
					"bceffect.modifier.value.input" : "bceffect.modifier.value.output"));
			valueMultiplyDescription += " " + ((this.applyCriteria == null) ? 
					StatCollector.translateToLocal("bceffect.modifier.allabilities") : this.applyCriteria.getTranslatedDescription()); 
			String byPercentageString = StatCollector.translateToLocal("bceffect.modifier.bypercentage");
			byPercentageString.replace("%1$s", String.format("%.0f", 100*this.getValueMultiplier()));
			valueMultiplyDescription += " " +  byPercentageString;
			
			String stackingDescription = this.getStackingDescription();
			if(stackingDescription != null) {
				valueMultiplyDescription += ", " + stackingDescription;
			}
			
			return valueMultiplyDescription;
		}
		return "Error! Description of EffectValueModifier, where valueMultiplier = 1";
	}

	@Override
	protected void applyOnEffectWithStackedBonus(BattleClassesAbstractAbilityEffect effect, float stackBonusMultiplier) {
		effect.modifierMultiplier += this.getValueMultiplier() * stackBonusMultiplier;
	}
	
}
