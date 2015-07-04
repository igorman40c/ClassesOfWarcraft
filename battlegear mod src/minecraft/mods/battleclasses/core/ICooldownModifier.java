package mods.battleclasses.core;

import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;

public interface ICooldownModifier {
	public float getMultiplierForAbility(BattleClassesAbstractAbility ability);
}
