package mods.battleclasses.attributes;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;
import net.minecraft.util.StatCollector;

/**
 * Utility class to generate attributes for items.
 * @author Zsolt Molnár
 */
public class AttributesFactory {
	
	public static final float PRIMARY_SECONDARY_DEFAULT_BALANCE = 0.5F;
	public static final float ATTRIBUTE_VALUE_PER_ITEMLEVEL = 2F;
	
	/**
	 * Generates attributes using the default attribute balance for a piece of armor by the given itemLevel and attribute type set.
	 * @param itemLevel
	 * @param types
	 * @return
	 */
	public static BattleClassesAttributes createForArmor(int itemLevel, EnumSet<EnumBattleClassesAttributeType> types) {
		return createAttributes(itemLevel, PRIMARY_SECONDARY_DEFAULT_BALANCE, 1F, types);
	}
	
	/**
	 * Generates attributes using the default attribute balance for a handheld item by the given itemLevel, handheld type and attribute type set.
	 * @param itemLevel
	 * @param heldType
	 * @param types
	 * @return
	 */
	public static BattleClassesAttributes createForHandheld(int itemLevel, EnumBattleClassesHandHeldType heldType, EnumSet<EnumBattleClassesAttributeType> types, float weaponSpeed, WeaponDamageCreationMode weaponDamageMode) {
		//Set primary and secondary attributes
		BattleClassesAttributes attributes = createAttributes(itemLevel, PRIMARY_SECONDARY_DEFAULT_BALANCE, getHandHeldAttributeMultiplier(heldType) ,types);
		//Set weapon damage
		attributes.setValueByType(EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE, createWeaponDamageValue(itemLevel, heldType, weaponSpeed, weaponDamageMode));
		return attributes;
	}
	
	/**
	 * Generates attributes for an item by given the itemLevel, attribute type set and other parameters. Do not use this method unless you know what you are doing!
	 * Handles concurrent attribute types in the following way: [HEALTH | [[PRIMARY TYPE | PRIMARY TYPE | ...] | [SECONDARY TYPE | SECONDARY TYPE | ...]]]
	 * [ ] - Set, | - concurrency (equal division)
	 * @param itemLevel - int value, level of the item
	 * @param balancePrimarySecondary - float value, determines the multiplier of the primary attribute values and the inverse multiplier of the secondary attribute values, (determines the balance of the attribute values between primary and secondary tpyes)
	 * @param handHeldContextMultiplier - float value, context related multiplier (for example: handheld multiplier)
	 * @param allTypes - EnumSet, attribute type set
	 * @return
	 */
	public static BattleClassesAttributes createAttributes(int itemLevel, float balancePrimarySecondary, float handHeldContextMultiplier, EnumSet<EnumBattleClassesAttributeType> types) {
		//Init attriubtes and helper values/collections
		BattleClassesAttributes attributes = new BattleClassesAttributes();
		float primaryMultiplier = 1F;
		float secondaryMultiplier = 1F;
		EnumSet<EnumBattleClassesAttributeType> allTypes = types.clone();
		EnumSet<EnumBattleClassesAttributeType> primaryTypes = EnumBattleClassesAttributeType.getPrimaryTypesOfSet(allTypes);
		EnumSet<EnumBattleClassesAttributeType> secondaryTypes = EnumBattleClassesAttributeType.getSecondaryTypesOfSet(allTypes);
		//If - should generate HEALTH -> half primary and secondary values 
		if(allTypes.contains(EnumBattleClassesAttributeType.HEALTH)) {
			primaryMultiplier *= 0.5F;
			secondaryMultiplier *= 0.5F;
			//If - should generate something different to HEALTH too -> half health value
			float healthMultiplier = (!primaryTypes.isEmpty() || !secondaryTypes.isEmpty()) ? 0.5F : 1F;
			//Generate & Set HEALTH value
			
			System.out.println("healthMultiplier: " + healthMultiplier);
			
			attributes.setValueByType(EnumBattleClassesAttributeType.HEALTH, createPrimaryAttributeValueForType(itemLevel, healthMultiplier * handHeldContextMultiplier, EnumBattleClassesAttributeType.HEALTH));
			
			//Remove HEALTH from types to avoid re-generating wrong primary value for it
			allTypes.remove(EnumBattleClassesAttributeType.HEALTH);
		}
		//If - should generate primary and secondary -> half values between them
		if(!primaryTypes.isEmpty() && !secondaryTypes.isEmpty()) {
			primaryMultiplier *= balancePrimarySecondary;
			secondaryMultiplier *= (1F - balancePrimarySecondary);
		}
		//Generating attribute values
		for(EnumBattleClassesAttributeType attributeType : allTypes) {
			EnumSet<EnumBattleClassesAttributeType> concurrents = getConcurrentsOfType(attributeType);
			concurrents.retainAll(allTypes);
			//Setting multiplier for the attribute type, based on the the number of concurrent tpyes. 1F/numberOfConcurrentTypes 
			float concurrencyMultiplier = 1F / ((float) (concurrents.size() + 1));
			if(attributeType.isPrimary()) {
				attributes.setValueByType(attributeType, createPrimaryAttributeValueForType(itemLevel, concurrencyMultiplier * primaryMultiplier * handHeldContextMultiplier, attributeType));
			}
			else {
				attributes.setValueByType(attributeType, createSecondaryAttributeValueForType(itemLevel, concurrencyMultiplier * secondaryMultiplier * handHeldContextMultiplier, attributeType));
			}
		}
		return attributes;
	}
	
	/**
	 * Generates damage value for weapon by the given itemLevel and handheld type. 
	 * @param itemLevel
	 * @param heldType
	 * @return
	 */
	public static float createWeaponDamageValue(int itemLevel, EnumBattleClassesHandHeldType heldType, float weaponSpeed, WeaponDamageCreationMode mode) {
		//TODO
		return 11.3F;
	}
		
	protected static float createPrimaryAttributeValueForType(int itemLevel, float contextMultiplier, EnumBattleClassesAttributeType type) {
		float value = getConstantCreateBonus(type) + ((float)itemLevel) * getPrimaryAttributeValuePerItemLevel() * contextMultiplier  * ATTRIBUTE_VALUE_PER_ITEMLEVEL;
		value = (value > 0) ? value : 0;
		return value;
	}
	
	protected static float createSecondaryAttributeValueForType(int itemLevel, float contextMultiplier, EnumBattleClassesAttributeType type) {
		float value = getConstantCreateBonus(type) + ((float)itemLevel) * getSecondaryAttributeValuePerItemLevel() * contextMultiplier  * ATTRIBUTE_VALUE_PER_ITEMLEVEL;
		value = (value > 0) ? value : 0;
		return value;
	}
	
	protected static float getPrimaryAttributeValuePerItemLevel() {
		return 2F;
	}
	
	protected static float getSecondaryAttributeValuePerItemLevel() {
		return 0.02F;
	}
	
	protected static float getConstantCreateBonus(EnumBattleClassesAttributeType type) {
		switch(type) {
			case HEALTH:  {
				return -1F;
			}
			default: {
				return 0F;
			}
		}
	}
	
	protected static EnumSet<EnumBattleClassesAttributeType> getConcurrentsOfType(EnumBattleClassesAttributeType type) {
		EnumSet<EnumBattleClassesAttributeType> concurrentTypes;
		switch(type) {
			case SPELLPOWER_HOLY:
				concurrentTypes = EnumSet.of(EnumBattleClassesAttributeType.STRENGTH);
				break;
			case STRENGTH:
				concurrentTypes = EnumSet.of(EnumBattleClassesAttributeType.SPELLPOWER_HOLY);
				break;
			case ARMOR_PENETRATION:
				concurrentTypes = EnumSet.of(EnumBattleClassesAttributeType.HASTE_RATING, EnumBattleClassesAttributeType.CRITICAL_RATING);
				break;
			case CRITICAL_RATING:
				concurrentTypes = EnumSet.of(EnumBattleClassesAttributeType.HASTE_RATING, EnumBattleClassesAttributeType.ARMOR_PENETRATION);
				break;
			case HASTE_RATING:
				concurrentTypes = EnumSet.of(EnumBattleClassesAttributeType.ARMOR_PENETRATION, EnumBattleClassesAttributeType.CRITICAL_RATING);
				break;
			default:
				concurrentTypes = EnumSet.noneOf(EnumBattleClassesAttributeType.class);
				break;
			}
		return concurrentTypes;
	}
	
	protected static final float ATTRIBUTE_VALUE_DEFAULT_CONTEXT = 1F;
	protected static float getHandHeldAttributeMultiplier(EnumBattleClassesHandHeldType heldType) {
		float mutltiplier;
		switch(heldType) {
			case MAIN_HANDED: {
				mutltiplier = 0.75F;
			}
			case OFF_HANDED: {
				mutltiplier = 0.25F;
			}
			case ONE_HANDED: {
				mutltiplier = 0.5F;
			}
			case TWO_HANDED: {
				mutltiplier = ATTRIBUTE_VALUE_DEFAULT_CONTEXT;
			}
			default: {
				mutltiplier = ATTRIBUTE_VALUE_DEFAULT_CONTEXT;
			}
		}
		return mutltiplier;
	}	
	
	public enum WeaponDamageCreationMode {
		/**
		 * Should be used for items those unable to deal melee damage. 
		 * For example: Held in offhands, bows, shields etc...
		 */
		ZERO,
		/**
		 * Should be used for non melee-combat purpose weapons.
		 * For example: Staves, spellblades, maces, etc... 
		 */
		COSMETIC,
		/**
		 * Should be used for melee-combat purpose weapons.
		 * For example: Swords, Axes, 2H-swords/hammers etc...
		 */
		REAL
	}
	
}
