package mods.battleclasses.ability.effect.modifier;

import net.minecraft.util.StatCollector;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffect;

public class BattleClassesEffectCritModifier extends BattleClassesAbstractEffectModifier {
	
	public BattleClassesEffectCritModifier(boolean input, IAbilityCriteria applyCriteria, float criticalBonus) {
		super(input, applyCriteria);
		this.criticalBonus = criticalBonus;
	}
	protected float criticalBonus = 0;
	public float getCriticalChanceBonus() {
		return criticalBonus;
	}
	
	@Override
	public String getTranslatedDescription() {
		if(criticalBonus != 0) {
			String critBonusDescription;
			critBonusDescription = StatCollector.translateToLocal(((getCriticalChanceBonus()>0) ? 
					"bceffect.modifier.increase" : "bceffect.modifier.reduce"));
			critBonusDescription += " " + StatCollector.translateToLocal(((this.isInputModifier()) ? 
					"bceffect.modifier.crit.input" : "bceffect.modifier.crit.output"));
			critBonusDescription += " " + ((this.applyCriteria == null) ? 
					StatCollector.translateToLocal("bceffect.modifier.allabilities") : this.applyCriteria.getTranslatedDescription()); 
			String byPercentageString = StatCollector.translateToLocal("bceffect.modifier.bypercentage");
			byPercentageString.replace("%1$s", String.format("%.0f", 100* Math.abs(this.getCriticalChanceBonus() )));
			critBonusDescription += " " +  byPercentageString;
			
			String stackingDescription = this.getStackingDescription();
			if(stackingDescription != null) {
				critBonusDescription += ", " + stackingDescription;
			}
			
			return critBonusDescription;
		}
		return "Error! Description of EffectCritModifier, where criticalBonus = 0";
	}
	
	@Override
	protected void applyOnEffectWithStackedBonus(BattleClassesAbstractAbilityEffect effect, float stackBonusMultiplier) {
		effect.modifierCriticalBonus += this.getCriticalChanceBonus() * stackBonusMultiplier;
	}
	
}
