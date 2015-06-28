package mods.battleclasses.ability.effect.modifier;

import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffect;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;

public class BattleClassesEffectDamageModifier extends BattleClassesAbstractEffectValueModifier {

	public BattleClassesEffectDamageModifier(boolean input, IAbilityCriteria applyCriteria, float valueMultiplier) {
		super(input, applyCriteria, valueMultiplier);
		this.unlocalizedDescriptionSubject = "bceffect.mulitplier.damage";
	}
	
	@Override
	protected void applyOnEffectWithStackedBonus(BattleClassesAbstractAbilityEffect effect, float stackBonusMultiplier) {
		if(effect.getIntent() == EnumBattleClassesAbilityIntent.OFFENSIVE) {
			super.applyOnEffectWithStackedBonus(effect, stackBonusMultiplier);
		}
	}

}
