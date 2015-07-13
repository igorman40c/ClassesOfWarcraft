package mods.battleclasses.attributes;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesWeaponHeldType;
import net.minecraft.util.StatCollector;

public class AttributesFactory {
	
	public static final float PRIMARY_SECONDARY_DEFAULT_BALANCE = 0.5F;
	
	public static final float MULTIPLIER_ITEMLEVEL = 2;
	
	/**
	 * 
	 * @param itemLevel
	 * @param types
	 * @return
	 */
	public static BattleClassesAttributes createForArmor(int itemLevel, EnumSet<EnumBattleClassesAttributeType> types) {
		return createAttributes(itemLevel, PRIMARY_SECONDARY_DEFAULT_BALANCE, 1F, types);
	}
	
	/**
	 * 
	 * @param itemLevel
	 * @param heldType
	 * @param types
	 * @return
	 */
	public static BattleClassesAttributes createForHandheld(int itemLevel, EnumBattleClassesWeaponHeldType heldType, EnumSet<EnumBattleClassesAttributeType> types) {
		return createAttributes(itemLevel, PRIMARY_SECONDARY_DEFAULT_BALANCE, heldType.getAttributeMultiplier() ,types);
	}
	
	/**
	 * 
	 * @param itemLevel
	 * @param balancePrimarySecondary
	 * @param handHeldContextMultiplier
	 * @param types
	 * @return
	 */
	public static BattleClassesAttributes createAttributes(int itemLevel, float balancePrimarySecondary, float handHeldContextMultiplier, EnumSet<EnumBattleClassesAttributeType> types) {
		BattleClassesAttributes attributes = new BattleClassesAttributes();
		float primaryMultiplier = 1F;
		float secondaryMultiplier = 1F;
		if(types.contains(EnumBattleClassesAttributeType.HEALTH)) {
			primaryMultiplier *= 0.5F;
			secondaryMultiplier *= 0.5F;
			
			float healthMultiplier = (!EnumBattleClassesAttributeType.getPrimaryTypesOfSet(types).isEmpty() || !EnumBattleClassesAttributeType.getSecondaryTypesOfSet(types).isEmpty()) ? 0.5F : 1F;
			attributes.setValueByType(EnumBattleClassesAttributeType.HEALTH, createPrimaryAttributeValueForType(itemLevel, healthMultiplier * handHeldContextMultiplier, EnumBattleClassesAttributeType.HEALTH));
			types.remove(EnumBattleClassesAttributeType.HEALTH);
		}
		if(!EnumBattleClassesAttributeType.getPrimaryTypesOfSet(types).isEmpty() && !EnumBattleClassesAttributeType.getSecondaryTypesOfSet(types).isEmpty()) {
			primaryMultiplier *= balancePrimarySecondary;
			secondaryMultiplier *= 1F - balancePrimarySecondary;
		}
		for(EnumBattleClassesAttributeType attributeType : types) {
			EnumSet<EnumBattleClassesAttributeType> concurrents = getConcurrentsOfType(attributeType);
			concurrents.retainAll(types);
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
		
	protected static float createPrimaryAttributeValueForType(int itemLevel, float contextMultiplier, EnumBattleClassesAttributeType type) {
		float value = getConstantCreateBonus(type) + ((float)itemLevel) * getPrimaryAttributeValuePerItemLevel() * contextMultiplier  * MULTIPLIER_ITEMLEVEL;
		value = (value > 0) ? value : 0;
		return value;
	}
	
	protected static float createSecondaryAttributeValueForType(int itemLevel, float contextMultiplier, EnumBattleClassesAttributeType type) {
		float value = getConstantCreateBonus(type) + ((float)itemLevel) * getSecondaryAttributeValuePerItemLevel() * contextMultiplier  * MULTIPLIER_ITEMLEVEL;
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
	
}
