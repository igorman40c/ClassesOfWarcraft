package mods.battleclasses.ability.effect;

import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;

public class EffectFactory {
	
	public static BattleClassesAbstractAbilityEffectInstantValue createInstantAbilityEffect(
			float virtualDamage, float virtualHealing, EnumBattleClassesAbilitySchool school) {
		
		BattleClassesAbstractAbilityEffectInstantValue instantValueEffect = createInstantEffect(virtualDamage, virtualHealing, 0, school);
		
		return instantValueEffect;
	}
	
	public static BattleClassesAbilityEffectPotion createPotionAbilityEffect(int potionID, float durationInSeconds) {
		
		BattleClassesAbilityEffectPotion potionCarriedAbilityEffect = new BattleClassesAbilityEffectPotion(potionID, durationInSeconds);
		
		return potionCarriedAbilityEffect;
	}
	
	public static BattleClassesAbilityEffectValueOverTime createValueOverTimeAbilityEffect(int potionID, float durationInSeconds,
			float virtualDamage, float virtualHealing, EnumBattleClassesAbilitySchool school) {
		
		BattleClassesAbstractAbilityEffectInstantValue instantValueEffect = createInstantEffect(virtualDamage, virtualHealing, 0, school);
		
		BattleClassesAbilityEffectValueOverTime valueOverTimeEffect = new BattleClassesAbilityEffectValueOverTime(potionID, durationInSeconds, instantValueEffect);
		
		return valueOverTimeEffect;
	}
	
	private static BattleClassesAbstractAbilityEffectInstantValue createInstantEffect(float virtualDamage,
			float virtualHealing, float basePortion, EnumBattleClassesAbilitySchool school) {
		
		return null;
	}
	
	private static float getBasePortionBySchool(EnumBattleClassesAbilitySchool school) {
		float basePortion = 0;
		switch(school) {
		case PHYSICAL_MELEE_ENERGY:
			break;
		case PHYSICAL_MELEE_RAGE:
			break;
		case PHYSICAL_RANGED:
			break;
		case SPELL_ARCANE:
			break;
		case SPELL_FIRE:
			break;
		case SPELL_FROST:
			break;
		case SPELL_HOLY:
			break;
		case SPELL_SHADOW:
			break;
		case UNKNOWN:
			break;
		default:
			break;
		}
		return basePortion;
	}
	
}
