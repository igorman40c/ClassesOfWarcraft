package mods.battleclasses.ability.passive;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.core.ICooldownModifier;

public class BattleClassesPassiveAbilityCooldownModifier extends BattleClassesAbstractAbility implements ICooldownModifier {

	protected BattleClassesPassiveAbilityCooldownModifier() {
		super();
	}
	
	public BattleClassesPassiveAbilityCooldownModifier(float cooldownModifier, IAbilityCriteria abilityCriteria) {
		this();
		this.cooldownModifier = cooldownModifier;
		this.abilityCriteria = abilityCriteria;
	}
	
	protected float cooldownModifier;
	protected IAbilityCriteria abilityCriteria;

	@Override
	public float getMultiplierForAbility(BattleClassesAbstractAbility ability) {
		if(abilityCriteria.isSatisfiedForAbility(ability)) {
			return cooldownModifier;
		}
		return 1;
	}
	
}
