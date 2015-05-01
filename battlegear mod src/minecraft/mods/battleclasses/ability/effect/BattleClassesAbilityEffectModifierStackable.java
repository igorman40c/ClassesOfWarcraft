package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.core.IStackableModifier;

public class BattleClassesAbilityEffectModifierStackable extends BattleClassesAbilityEffectModifier implements IStackableModifier {

	protected int stackCount = 0;
	protected float stackBonusMultiplier = 0;
	
	public BattleClassesAbilityEffectModifierStackable(boolean input, float valueMultiplier, float criticalBonus, IAbilityCriteria applyCriteria,
			float stackBonusMultiplier) {
		super(input, valueMultiplier, criticalBonus, applyCriteria);
		this.stackBonusMultiplier = stackBonusMultiplier;
	}
	
	@Override
	public float getValueMultiplier() {
		return super.getValueMultiplier();
	}
	
	@Override
	public float getCriticalChanceBonus() {
		return super.getCriticalChanceBonus();
	}
	
	protected float getStackBonus(){
		return 1F + stackBonusMultiplier * stackCount;
	}

	@Override
	public void applyOnEffect(BattleClassesAbstractAbilityEffect effect) {
		super.applyOnEffect(effect);
		this.resetStackCount();
	}

	@Override
	public void setStackCount(int stackCount) {
		this.stackCount = stackCount;
	}

	@Override
	public void resetStackCount() {
		this.stackCount = 0;
	}

}
