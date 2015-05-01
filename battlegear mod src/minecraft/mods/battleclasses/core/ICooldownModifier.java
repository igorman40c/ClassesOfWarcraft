package mods.battleclasses.core;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;

public interface ICooldownModifier {
	public float getMultiplierForAbility(BattleClassesAbstractAbilityActive ability);
}
