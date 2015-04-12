package mods.battleclasses.ability.effect;

import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;

public class EffectFactory {
	
	public static BattleClassesAbstractAbilityEffectInstantValue createInstantAbilityEffect(
			float nullPowerDamage, float nullPowerHealing, float basePortion, EnumBattleClassesAbilitySchool school) {
		return null;
	}
	
	public static BattleClassesAbilityEffectPotion createPotionAbilityEffect(int potionID, float durationInSeconds) {
		return null;
	}
	
	public static BattleClassesAbstractAbilityEffectInstantValue createValueOverTimeAbilityEffect(int potionID, float durationInSeconds,
			float nullPowerDamage, float nullPowerHealing, float basePortion, EnumBattleClassesAbilitySchool school) {
		return null;
	}
}
