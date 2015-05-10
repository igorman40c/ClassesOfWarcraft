package mods.battleclasses.ability.criteria;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.StatCollector;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;

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

	@Override
	public String getTranslatedDescription() {
		String abilityList = "";
		boolean allMagical = true;
		int i = 0;
		for(int abilityID : this.abilityIDs) {
			BattleClassesAbstractAbility ability = BattleClassesAbstractAbility.registredAbilities.get(abilityID);
			if(i > 0) {
				abilityList += ",";
			}
			if(ability != null) {
				abilityList += " " + ability.getUnlocalizedDisplayName();
				if(ability instanceof BattleClassesAbstractAbilityActive) {
					allMagical = allMagical && ((BattleClassesAbstractAbilityActive)ability).getSchool().isMagical();
				}
				else {
					allMagical = false;
				}
			}
			else {
				abilityList += " ABILITY WITH UNREGISTERED ID: " + abilityID;
			}
			++i;
		}
		
		if(allMagical) {
			if(abilityIDs.size() == 1) {
				abilityList += " " + StatCollector.translateToLocal("bccriteria.abilityschool.spell");
			}
			else {
				abilityList += " " + StatCollector.translateToLocal("bccriteria.abilityschool.spells");
			}
		}
		else {
			if(abilityIDs.size() == 1) {
				abilityList += " " + StatCollector.translateToLocal("bccriteria.abilityschool.ability");
			}
			else {
				abilityList += " " + StatCollector.translateToLocal("bccriteria.abilityschool.abilities");
			}
		}
		
		return abilityList;
	}
	
}
