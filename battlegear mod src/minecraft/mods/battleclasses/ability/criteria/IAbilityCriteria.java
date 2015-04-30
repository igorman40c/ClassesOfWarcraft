package mods.battleclasses.ability.criteria;

import mods.battleclasses.ability.BattleClassesAbstractAbility;

/**
 * Criteria interface, use it to make attribute or effect modifiers work for only the desired abilities.
 * Supports both, active and passive abilities.
 * @author Zsolt
 */
public interface IAbilityCriteria {
	/**
	 * Determines if the given ability satisfies a certain condition. 
	 * (Usual implementations check property equations for example: id, school, etc...)
	 * @param abstractAbility
	 * @return
	 */
	boolean isSatisfiedForAbility(BattleClassesAbstractAbility abstractAbility);
}
