package mods.battleclasses.ability.effect;

import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;

public class EffectFactory {
	
	public static BattleClassesAbstractAbilityEffectInstantValue createInstantAbilityEffect(
			float virtualDamage, float virtualHealing, EnumBattleClassesAbilitySchool school) {
		
		float effectBasePortion = getBasePortionBySchool(school);
		float effectValueRandomness = getEffectValueRandomnessBySchool(school);
		BattleClassesAbstractAbilityEffectInstantValue instantValueEffect = createInstantEffect(virtualDamage, virtualHealing, 
				effectBasePortion, effectValueRandomness, school);
		
		return instantValueEffect;
	}
	
	public static BattleClassesAbilityEffectPotion createPotionAbilityEffect(EnumBattleClassesAbilitySchool abilitySchool, int potionID, float durationInSeconds) {
		
		BattleClassesAbilityEffectPotion potionCarriedAbilityEffect = new BattleClassesAbilityEffectPotion(potionID, durationInSeconds);
		
		return potionCarriedAbilityEffect;
	}
	
	public static BattleClassesAbilityEffectValueOverTime createValueOverTimeAbilityEffect(int potionID, float durationInSeconds,
			float virtualDamage, float virtualHealing, EnumBattleClassesAbilitySchool school) {
		
		float effectBasePortion = 0;
		float effectValueRandomness = 0;
		BattleClassesAbstractAbilityEffectInstantValue instantValueEffect = createInstantEffect(virtualDamage, virtualHealing, 
				effectBasePortion, effectValueRandomness, school);
		
		BattleClassesAbilityEffectValueOverTime valueOverTimeEffect = new BattleClassesAbilityEffectValueOverTime(potionID, durationInSeconds, instantValueEffect);
		
		return valueOverTimeEffect;
	}
	
	
	public static final float ATTRIBUTE_POWER_SCALE = 36; 
	
	private static BattleClassesAbstractAbilityEffectInstantValue createInstantEffect(float virtualDamage,
			float virtualHealing, float basePortion, float randomness, EnumBattleClassesAbilitySchool school) {
		
		BattleClassesAbstractAbilityEffectInstantValue valueEffect = null;
		
		BattleClassesAbilityEffectInstantDamage damageEffect = null;
		BattleClassesAbilityEffectInstantHeal healEffect = null;
		
		if(virtualDamage > 0) {
			float valueBonusCoefficient = (virtualDamage - basePortion*virtualDamage) / ATTRIBUTE_POWER_SCALE;
			damageEffect = new BattleClassesAbilityEffectInstantDamage(basePortion*virtualDamage, valueBonusCoefficient, randomness);
			damageEffect.setValueBalancer(getEffectValueBalancerBySchool(school));
			if(virtualHealing <= 0) {
				valueEffect =  damageEffect;
			}
		}
		
		if(virtualHealing > 0) {
			float valueBonusCoefficient = (virtualHealing - basePortion*virtualHealing) / ATTRIBUTE_POWER_SCALE;
			healEffect = new BattleClassesAbilityEffectInstantHeal(basePortion*virtualDamage, valueBonusCoefficient, randomness);
			healEffect.setValueBalancer(getEffectValueBalancerBySchool(school));
			if(virtualDamage <= 0) {
				valueEffect = healEffect;
			}
		}
		
		if(virtualDamage > 0 && virtualHealing > 0) {
			BattleClassesAbilityEffectInstantDual dualEffect = new BattleClassesAbilityEffectInstantDual(damageEffect, healEffect);
			valueEffect = dualEffect;
		}
		
		return valueEffect;
	}
	
	private static float getBasePortionBySchool(EnumBattleClassesAbilitySchool school) {
		float basePortion = 0.2F;
		switch(school) {
//		case PHYSICAL_MELEE_ENERGY:
//			break;
		case PHYSICAL_MELEE:
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
	
	private static float getEffectValueRandomnessBySchool(EnumBattleClassesAbilitySchool school) {
		float randomness = 0.1F;
		switch(school) {
//		case PHYSICAL_MELEE_ENERGY:
//			randomness = 0.15F;
//			break;
		case PHYSICAL_MELEE:
			randomness = 0.10F;
			break;
		case PHYSICAL_RANGED:
			randomness = 0.10F;
			break;
		case SPELL_ARCANE:
			randomness = 0.15F;
			break;
		case SPELL_FIRE:
			randomness = 0.15F;
			break;
		case SPELL_FROST:
			randomness = 0.10F;
			break;
		case SPELL_HOLY:
			randomness = 0.10F;
			break;
		case SPELL_SHADOW:
			randomness = 0.10F;
			break;
		case UNKNOWN:
			randomness = 0.10F;
			break;
		default:
			break;
		}
		return randomness;
	}
	
	private static float getEffectValueBalancerBySchool(EnumBattleClassesAbilitySchool school) {
		float relativeBalancer = 1F;
		switch(school) {
//		case PHYSICAL_MELEE_ENERGY:
//			relativeBalancer = 1.2F;
//			break;
		case PHYSICAL_MELEE:
			relativeBalancer = 1.2F;
			break;
		case PHYSICAL_RANGED:
			relativeBalancer = 1F;
			break;
		case SPELL_ARCANE:
			relativeBalancer = 0.8F;
			break;
		case SPELL_FIRE:
			relativeBalancer = 0.8F;
			break;
		case SPELL_FROST:
			relativeBalancer = 0.8F;
			break;
		case SPELL_HOLY:
			relativeBalancer = 0.8F;
			break;
		case SPELL_SHADOW:
			relativeBalancer = 0.8F;
			break;
		case UNKNOWN:
			break;
		default:
			break;
		}
		return relativeBalancer;
	}
		
}
