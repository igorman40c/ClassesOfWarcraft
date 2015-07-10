package mods.battleclasses.attributes;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesWeaponHeldType;
import net.minecraft.util.StatCollector;

public class AttributesFactory {
	public static final float MULTIPLIER_ITEMLEVEL = 2;
	public static final float MULTIPLIER_HANDHELD = 2;
	public static final float MULTIPLIER_ARMOR = 1;
	
	protected static float createPrimaryAttributeValueForType(int itemLevel, float contextMultiplier, EnumBattleClassesAttributeType type) {
		float value = getConstantCreateBonus(type) + ((float)itemLevel) * contextMultiplier  * MULTIPLIER_ITEMLEVEL / getRelativeCreateCost(type);
		value = (value > 0) ? value : 0;
		return value;
	}
	
	protected static float createSecondaryAttributeValueForType(int itemLevel, float contextMultiplier, EnumBattleClassesAttributeType type) {
		float value = getConstantCreateBonus(type) + ((float)itemLevel) * contextMultiplier  * MULTIPLIER_ITEMLEVEL / getRelativeCreateCost(type);
		value = (value > 0) ? value : 0;
		return value;
	}
		
	public static BattleClassesAttributes createForArmor(int itemLevel, EnumSet<EnumBattleClassesAttributeType> types) {
		BattleClassesAttributes attributes = new BattleClassesAttributes();
		for(EnumBattleClassesAttributeType attributeType : types) {
			attributes.setValueByType(attributeType, createPrimaryAttributeValueForType(itemLevel, MULTIPLIER_ARMOR, attributeType));
		}
		return attributes;
	}
	
	public static BattleClassesAttributes createForHandheld(int itemLevel, EnumSet<EnumBattleClassesAttributeType> types, EnumBattleClassesWeaponHeldType heldType) {
		BattleClassesAttributes attributes = new BattleClassesAttributes();
		for(EnumBattleClassesAttributeType attributeType : types) {
			attributes.setValueByType(attributeType, createPrimaryAttributeValueForType(itemLevel, heldType.getAttributeMultiplier()*MULTIPLIER_HANDHELD, attributeType));
		}
		return attributes;
	}
	
	protected static float getRelativeCreateCost(EnumBattleClassesAttributeType type) {
		switch(type) {
			default: {
				return 1F;
			}
		}
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
	
}
