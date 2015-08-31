package mods.battleclasses;

public class BattleClassesMetaConfig {
	
	/**
	 * Ammount of HP a player virtually has (according to the meta game) without gear / in to T0 set.
	 */
	public static final float PlayerMaxHealthBase = 16F;
	
	/**
	 * Ammount of HP the player's maxHealth gets increased by, when incrementing gear itemLevel (Tier). 
	 */
	public static final float PlayerMaxHealthTierStep = 8F;
	
	
	public static final float RelativeUnmodifiedAverage_EPS = 0.20F;

	/**
	 * RelativeUnmodifiedAverage Damage Per Second
	 */
	public static final float RelativeUnmodifiedAverage_DPS = RelativeUnmodifiedAverage_EPS * 1F;
	
	/**
	 * RelativeUnmodifiedAverage Healing Per Second
	 */
	public static final float RelativeUnmodifiedAverage_HPS = RelativeUnmodifiedAverage_EPS * 1F;
	
	/**
	 * RelativeUnmodifiedAverage Protection Per Second
	 */
	public static final float RelativeUnmodifiedAverage_PPS = RelativeUnmodifiedAverage_EPS * 1F;
	
	public static final float DamageTypeAmplifier_PhysicalMelee = 1 + 0.20F;
	public static final float DamageTypeAmplifier_PhysicalRanged = 1 + 0F;
	public static final float DamageTypeAmplifier_Spell = 1 - 0.20F;	
	
	public static final float RelativeAverage_DPS_PhysicalMelee = RelativeUnmodifiedAverage_DPS * DamageTypeAmplifier_PhysicalMelee;
	public static final float RelativeAverage_DPS_PhysicalRanged = RelativeUnmodifiedAverage_DPS * DamageTypeAmplifier_PhysicalRanged;
	public static final float RelativeAverage_DPS_Spell = RelativeUnmodifiedAverage_DPS * DamageTypeAmplifier_Spell;
	
	public static EffectValueSet getAttributeScaled_Damage_PhysicalMelee_EffectValue(float Virtual_DPS, float useDuration, float attributePointsTierStep) {
		return getAttributeScaledEffectValue(Virtual_DPS * RelativeAverage_DPS_PhysicalMelee, useDuration, attributePointsTierStep);
	}
	
	public static EffectValueSet getAttributeScaled_Damage_PhysicalRanged_EffectValue(float Virtual_DPS, float useDuration, float attributePointsTierStep) {
		return getAttributeScaledEffectValue(Virtual_DPS * RelativeAverage_DPS_PhysicalRanged , useDuration, attributePointsTierStep);
	}
	
	public static EffectValueSet getAttributeScaled_Damage_Spell_EffectValue(float Virtual_DPS, float useDuration, float attributePointsTierStep) {
		return getAttributeScaledEffectValue(Virtual_DPS * RelativeAverage_DPS_Spell, useDuration, attributePointsTierStep);
	}
	
	public static EffectValueSet getAttributeScaled_Healing_EffectValue(float Virtual_HPS, float useDuration, float attributePointsTierStep) {
		return getAttributeScaledEffectValue(Virtual_HPS * RelativeUnmodifiedAverage_HPS, useDuration, attributePointsTierStep);
	}
	
	public static EffectValueSet getAttributeScaled_Protection_EffectValue(float Virtual_PPS, float useDuration, float attributePointsTierStep) {
		return getAttributeScaledEffectValue(Virtual_PPS * RelativeUnmodifiedAverage_PPS, useDuration, attributePointsTierStep);
	}
	
	private static EffectValueSet getAttributeScaledEffectValue(float Relative_EPS, float useDuration, float attributePointsTierStep) {
		float valueBase = PlayerMaxHealthBase * Relative_EPS * useDuration;
		float valueBonusCoefficient = (PlayerMaxHealthTierStep  * Relative_EPS * useDuration) / attributePointsTierStep;
		return new EffectValueSet(valueBase, valueBonusCoefficient);
	}
	
	public static final class EffectValueSet {
		public float valueBase = 0F;
		public float valueBonusCoefficient = 0F;
		public EffectValueSet(float valueBase, float valueBonusCoefficient) {
			this.valueBase = valueBase;
			this.valueBonusCoefficient = valueBonusCoefficient;
		}
	}

}
