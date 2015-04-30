package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.criteria.IAbilityCriteria;

public class BattleClassesAbilityEffectModifier implements ICWEffectModifier {
	
	protected boolean input = false;
	protected float valueMultiplier = 1;
	protected float criticalBonus = 0;
	protected IAbilityCriteria applyCriteria;
	
	public BattleClassesAbilityEffectModifier(boolean input, float valueMultiplier, IAbilityCriteria applyCriteria) {
		this.input = input;
		this.valueMultiplier = valueMultiplier;
		this.applyCriteria = applyCriteria;
	}
	
	public BattleClassesAbilityEffectModifier(boolean input, float valueMultiplier, float criticalBonus, IAbilityCriteria applyCriteria) {
		this(input, valueMultiplier, applyCriteria);
		this.criticalBonus = criticalBonus;
	}
	
	@Override
	public boolean isInputModifier() {
		return input;
	}
	
	@Override
	public float getValueMultiplier() {
		return valueMultiplier;
	}

	@Override
	public float getCriticalChanceBonus() {
		return criticalBonus;
	}

	@Override
	public boolean canBeAppliedOnEffect(BattleClassesAbstractAbilityEffect effect) {
		if(applyCriteria != null) {
			return applyCriteria.isSatisfiedForAbility(effect.getParentAbility());
		}
		return true;
	}

}
