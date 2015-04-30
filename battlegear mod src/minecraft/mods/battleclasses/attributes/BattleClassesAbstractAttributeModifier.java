package mods.battleclasses.attributes;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

public abstract class BattleClassesAbstractAttributeModifier implements ICWAttributeModifier {

	protected BattleClassesAttributes attributes;
	protected IAbilityCriteria applyCriteria;
	
	protected BattleClassesAbstractAttributeModifier() {
		
	}
	
	public BattleClassesAbstractAttributeModifier(BattleClassesAttributes attributes) {
		this.attributes = attributes;
	}
	
	public BattleClassesAbstractAttributeModifier(BattleClassesAttributes attributes, IAbilityCriteria applyCriteria) {
		this.attributes = attributes;
		this.applyCriteria = applyCriteria;
	}
	
	public boolean canBeAppliedOnAbility(BattleClassesAbstractAbility ability) {
		if(applyCriteria == null) {
			return this.applyCriteria.isSatisfiedForAbility(ability);
		}
		return true;
	}
	
	@Override
	public final BattleClassesAttributes applyAttributeModifier(BattleClassesAbstractAbilityActive targetAbility,
			BattleClassesAttributes attributes) {
		if(attributes == null) {
			attributes = new BattleClassesAttributes();
		}
		this.applyAttributeModifier(attributes);
		return attributes;
	}
	
	protected abstract BattleClassesAttributes applyAttributeModifier(BattleClassesAttributes accumulatedAttributes);
	
}
