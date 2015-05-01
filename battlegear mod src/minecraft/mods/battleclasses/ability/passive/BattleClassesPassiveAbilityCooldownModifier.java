package mods.battleclasses.ability.passive;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.criteria.IAbilityCriteria;
import mods.battleclasses.core.ICooldownModifier;

public class BattleClassesPassiveAbilityCooldownModifier extends BattleClassesAbstractAbility implements ICooldownModifier {

	protected BattleClassesPassiveAbilityCooldownModifier(int parAbilityID) {
		super(parAbilityID);
	}
	
	public BattleClassesPassiveAbilityCooldownModifier(int parAbilityID, float cooldownModifier, IAbilityCriteria abilityCriteria) {
		this(parAbilityID);
		this.cooldownModifier = cooldownModifier;
		this.abilityCriteria = abilityCriteria;
	}
	
	protected float cooldownModifier;
	protected IAbilityCriteria abilityCriteria;

	@Override
	public float getMultiplierForAbility(BattleClassesAbstractAbilityActive ability) {
		if(abilityCriteria.isSatisfiedForAbility(ability)) {
			return cooldownModifier;
		}
		return 1;
	}
	
}
