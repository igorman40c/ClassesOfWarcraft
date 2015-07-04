package mods.battleclasses.ability.talent;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.passive.BattleClassesAbstractAbilityPassive;

public class BattleClassesTalentAbilityPassiveContainer extends BattleClassesAbstractTalentAbilityContainer {

	public BattleClassesTalentAbilityPassiveContainer(String name, int parTalentLevel, BattleClassesAbstractAbilityPassive parAbility) {
		super(name, parTalentLevel, parAbility);
	}
	
	public BattleClassesAbstractAbilityPassive getContainedAbility() {
		return (BattleClassesAbstractAbilityPassive) containedAbility;
	}
	
	@Override
	public String getTranslatedDescription() {
		return this.getContainedAbility().getTranslatedDescription();
	}

}
