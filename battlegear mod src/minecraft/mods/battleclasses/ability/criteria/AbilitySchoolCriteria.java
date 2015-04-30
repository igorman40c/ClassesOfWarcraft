package mods.battleclasses.ability.criteria;

import java.util.EnumSet;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;

/**
 * Ability Criteria implementation. Use it to filter attribute/effect modifiers by a set of ability school enums. 
 * @author Zsolt
 */
public class AbilitySchoolCriteria implements IAbilityCriteria {

	protected EnumSet<EnumBattleClassesAbilitySchool> abilitySchools;
	
	public AbilitySchoolCriteria(EnumBattleClassesAbilitySchool abilitySchool) {
		this.abilitySchools = EnumSet.of(abilitySchool);
	}
	
	public AbilitySchoolCriteria(EnumSet<EnumBattleClassesAbilitySchool> abilitySchools) {
		this.abilitySchools = EnumSet.copyOf(abilitySchools);
	}
	
	@Override
	public boolean isSatisfiedForAbility(BattleClassesAbstractAbility abstractAbility) {
		// TODO Auto-generated method stub
		return false;
	}

}
