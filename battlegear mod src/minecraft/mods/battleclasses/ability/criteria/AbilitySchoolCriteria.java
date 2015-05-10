package mods.battleclasses.ability.criteria;

import java.util.EnumSet;

import net.minecraft.util.StatCollector;
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

	@Override
	public String getTranslatedDescription() {
		String abilitySchoolsDescription = "";
		if(abilitySchools == null || abilitySchools.size() == 0) {
			abilitySchoolsDescription = StatCollector.translateToLocal("bccriteria.abilityschool.all");
		}
		else {
			boolean allMagical = true;
			int i = 0;
			for(EnumBattleClassesAbilitySchool abilitySchool : abilitySchools) {
				if(i > 0) {
					abilitySchoolsDescription += ",";
				}
				abilitySchoolsDescription += " " + abilitySchool.getTranslatedDisplayedName();
				allMagical = allMagical && abilitySchool.isMagical();
			} 
			if(allMagical) {
				abilitySchoolsDescription += " " + StatCollector.translateToLocal("bccriteria.abilityschool.spells");
			}
			else {
				abilitySchoolsDescription += " " + StatCollector.translateToLocal("bccriteria.abilityschool.abilities");
			}
		}
		
		return abilitySchoolsDescription;
	}

}
