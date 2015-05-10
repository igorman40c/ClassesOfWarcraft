package mods.battleclasses.ability.effect;

import net.minecraft.util.StatCollector;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.client.IDescriptionProvider;
import mods.battleclasses.core.IStackableModifier;

public abstract class BattleClassesAbstractEffectModifier implements ICWEffectModifier, IStackableModifier, IDescriptionProvider {
	
	protected boolean input = false;
	protected IAbilityCriteria applyCriteria;
		
	public BattleClassesAbstractEffectModifier(boolean input, IAbilityCriteria applyCriteria) {
		this.input = input;
		this.applyCriteria = applyCriteria;
	}
	
	@Override
	public boolean isInputModifier() {
		return input;
	}
	
	@Override
	public void applyOnEffect(BattleClassesAbstractAbilityEffect effect) {
		if(this.applyCriteria == null ||this.applyCriteria.isSatisfiedForAbility(effect.getParentAbility())) {
			applyOnEffectWithStackedBonus(effect, this.getStackBonus());
		}
	}
	
	protected abstract void applyOnEffectWithStackedBonus(BattleClassesAbstractAbilityEffect effect, float stackBonusMultiplier);
	
	//----------------------------------------------------------------------------------
	//						SECTION - IStackableModifier Implementation
	//----------------------------------------------------------------------------------
	
	private int currentStackCount = 0;
	private float stackBonusMultiplier = 0;
	private int maxStackCount = 1;
	
	@Override
	public void setStackableProperties(int maxStackCount, float stackBonusMultiplier) {
		this.maxStackCount = maxStackCount;
		this.stackBonusMultiplier = stackBonusMultiplier;
	}
	
	@Override
	public int getMaxStackCount() {
		return this.maxStackCount;
	}
	
	@Override
	public void updateStackCount(int stackCount) {
		this.currentStackCount = stackCount;
	}

	@Override
	public void resetStackCount() {
		this.currentStackCount = 0;
	}
	
	@Override
	public float getStackBonus() {
		return 1F + stackBonusMultiplier * currentStackCount;
	}
	
	/**
	 * Generates description about the stackability of the modifier. Output example: "stacks up to 5 times"
	 * @return
	 */
	public String getStackingDescription() {
		if(maxStackCount > 1) {
			String stackingDescription = StatCollector.translateToLocal("bceffect.modifier.stacks");
			stackingDescription.replace("%1$s", Integer.toString(this.maxStackCount));
			return stackingDescription;
		}
		
		return null;
	}
}
