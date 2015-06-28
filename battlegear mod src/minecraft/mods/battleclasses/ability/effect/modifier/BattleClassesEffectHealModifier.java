package mods.battleclasses.ability.effect.modifier;

import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffect;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;

public class BattleClassesEffectHealModifier extends BattleClassesAbstractEffectValueModifier {

	public BattleClassesEffectHealModifier(boolean input, IAbilityCriteria applyCriteria, float valueMultiplier) {
		super(input, applyCriteria, valueMultiplier);
		this.unlocalizedDescriptionSubject = "bceffect.mulitplier.heal";
	}
	
	@Override
	protected void applyOnEffectWithStackedBonus(BattleClassesAbstractAbilityEffect effect, float stackBonusMultiplier) {
		if(effect.getIntent() == EnumBattleClassesAbilityIntent.SUPPORTIVE) {
			super.applyOnEffectWithStackedBonus(effect, stackBonusMultiplier);
		}
	}

}