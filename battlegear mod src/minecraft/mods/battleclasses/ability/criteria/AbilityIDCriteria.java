package mods.battleclasses.ability.criteria;

import java.util.HashSet;
import java.util.Set;

import mods.battleclasses.ability.BattleClassesAbstractAbility;

/**
 * Ability Criteria implementation. Use it to filter attribute/effect modifiers by a set of abilityIDs. 
 * @author Zsolt
 */
public class AbilityIDCriteria implements IAbilityCriteria {

	protected Set<Integer> abilityIDs = new HashSet<Integer>();
	
	public AbilityIDCriteria(int abilityID) {
		this.abilityIDs.add(abilityID);
	}
	
	public AbilityIDCriteria(Set<Integer> abilityIDs) {
		this.abilityIDs.addAll(abilityIDs);
	}
	
	@Override
	public boolean isSatisfiedForAbility(BattleClassesAbstractAbility abstractAbility) {
		return this.abilityIDs.contains(abstractAbility.getAbilityID());
	}
	
}
