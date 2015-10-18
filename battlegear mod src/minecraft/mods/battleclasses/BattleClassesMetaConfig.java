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
	public static final float PlayerMaxHealthVanilla = 20F;
	public static final float PlayerArmorPieceCount = 4F;
	
	
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
		//float valueBase = PlayerMaxHealthBase * Relative_EPS * useDuration;
		float valueBase = 0F;
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
	
	public static abstract class AttributeConfig {
		
		//Total Power Scaling
		protected float TotalPowerScaling_HandheldDamage;
		protected float TotalPowerScaling_Attributes;
		
		//Attribute Distribution
		protected float ItemAttributeDistribution_Handheld;
		protected float ItemAttributeDistribution_Armor;
		
		//Attribute Generation
		protected float AttributeCreditsPerTier;
		protected float AttributePointsPerCredit;
		
		protected final float getAttributePointsPerTier() {
			return AttributeCreditsPerTier * AttributePointsPerCredit;
		}
		
		public final float getAttributeCreditsForHandheld(int itemLevel) {
			return AttributeCreditsPerTier * getAttributeMultiplierForItemLevel(itemLevel) * ItemAttributeDistribution_Handheld;
		}
		
		protected final float getAttributeCreditsForArmorSet(int itemLevel) {
			return AttributeCreditsPerTier * getAttributeMultiplierForItemLevel(itemLevel) * ItemAttributeDistribution_Armor;
		}
		
		public final float getAttributeCreditsForArmorPiece(int itemLevel) {
			return getAttributeCreditsForArmorSet(itemLevel) / BattleClassesMetaConfig.PlayerArmorPieceCount;
		}
		
		public final float createSecondaryAttributeValueForCredits(float credits) {
			return 1F * credits;
		}
		
		protected final float getAttributeMultiplierForItemLevel(int itemLevel) {
			return (BattleClassesMetaConfig.PlayerMaxHealthBase / BattleClassesMetaConfig.PlayerMaxHealthTierStep) + itemLevel;
		}
		
		protected final float getAttributePointsForEquipment(int itemLevel) {
			return getAttributePointsPerTier() * getAttributeMultiplierForItemLevel(itemLevel);
		}
				
		protected final float getAttributePointsForArmorSet(int itemLevel) {
			return getAttributePointsForEquipment(itemLevel) * ItemAttributeDistribution_Armor;
		}
		
		public final float getAttributePointsForArmorPiece(int itemLevel) {
			return getAttributePointsForArmorSet(itemLevel) / BattleClassesMetaConfig.PlayerArmorPieceCount;
		}
		
		public final float getAttributePointsForHandheld(int itemLevel) {
			return getAttributePointsForEquipment(itemLevel) * ItemAttributeDistribution_Handheld;
		}
		
		protected final float getHealthAttributePointsForArmorSet(int itemLevel) {
			return BattleClassesMetaConfig.PlayerMaxHealthBase - BattleClassesMetaConfig.PlayerMaxHealthVanilla + BattleClassesMetaConfig.PlayerMaxHealthTierStep * itemLevel;
		}
		
		public final float getHealthAttributePointsForArmorPiece(int itemLevel) {
			return getHealthAttributePointsForArmorSet(itemLevel) / BattleClassesMetaConfig.PlayerArmorPieceCount; 
		}
		
		public float getStandardWeaponSpeed_OneHanded() {
			return 1.25F;
		}
		
		public float getStandardWeaponSpeed_TwoHanded() {
			return 1.5F;
		}
		
		public float getWeaponDamage_OneHanded(int itemLevel, float weaponSpeed) {
			return 0;
		}
		
		public float getWeaponDamage_TwoHanded(int itemLevel, float weaponSpeed) {
			return 0;
		}
		
		public static class PhysicalMelee extends AttributeConfig {
			public static final PhysicalMelee INSTANCE = new PhysicalMelee();
			
			private PhysicalMelee() { 
				//Total Power Scaling
				TotalPowerScaling_HandheldDamage = 0.75F;
				TotalPowerScaling_Attributes = 1F - TotalPowerScaling_HandheldDamage;
				//Attribute Distribution
				ItemAttributeDistribution_Handheld = 0.2F;
				ItemAttributeDistribution_Armor = 1F - ItemAttributeDistribution_Handheld;
				//Attribute Generation
				AttributeCreditsPerTier = 1;
				AttributePointsPerCredit = 5;
			}
		}
		
		public static class PhysicalRanged extends AttributeConfig {
			public static final PhysicalRanged INSTANCE = new PhysicalRanged();
			
			private PhysicalRanged() { 
				//Total Power Scaling
				TotalPowerScaling_HandheldDamage = 0.75F;
				TotalPowerScaling_Attributes = 1F - TotalPowerScaling_HandheldDamage;
				//Attribute Distribution
				ItemAttributeDistribution_Handheld = 0.2F;
				ItemAttributeDistribution_Armor = 1F - ItemAttributeDistribution_Handheld;
				//Attribute Generation
				AttributeCreditsPerTier = 1;
				AttributePointsPerCredit = 5;
			}
		}
		
		public static class MagicalSpell extends AttributeConfig {
			public static final MagicalSpell INSTANCE = new MagicalSpell();
			
			private MagicalSpell() { 
				//Total Power Scaling
				TotalPowerScaling_HandheldDamage = 0F;
				TotalPowerScaling_Attributes = 1F - TotalPowerScaling_HandheldDamage;
				//Attribute Distribution
				ItemAttributeDistribution_Handheld = 0.5F;
				ItemAttributeDistribution_Armor = 1F - ItemAttributeDistribution_Handheld;
				//Attribute Generation
				AttributeCreditsPerTier = 2;
				AttributePointsPerCredit = 4;
			}
		}
		
	}
	

}
