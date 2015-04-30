package mods.battleclasses.attributes;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesWeaponHeldType;
import net.minecraft.util.StatCollector;

public class FactoryAttributes {
	public static final float MULTIPLIER_ITEMLEVEL = 2;
	public static final float MULTIPLIER_HANDHELD = 2;
	
	public static float createValueForType(int itemLevel, EnumBattleClassesAttributeType type) {
		float value = type.getConstantCreateBonus() + itemLevel * MULTIPLIER_ITEMLEVEL / type.getRelativeCreateCost();
		value = (value > 0) ? value : 0;
		return value;
	}
		
	public static BattleClassesAttributes createForArmor(int itemLevel, EnumSet<EnumBattleClassesAttributeType> types) {
		BattleClassesAttributes attributes = new BattleClassesAttributes();
		for(EnumBattleClassesAttributeType attributeType : types) {
			attributes.setValueByType(attributeType, createValueForType(itemLevel, attributeType));
		}
		return attributes;
	}
	
	public static BattleClassesAttributes createForHandheld(int itemLevel, EnumSet<EnumBattleClassesAttributeType> types, EnumBattleClassesWeaponHeldType heldType) {
		BattleClassesAttributes attributes = new BattleClassesAttributes();
		for(EnumBattleClassesAttributeType attributeType : types) {
			attributes.setValueByType(attributeType, createValueForType(itemLevel, attributeType)*heldType.getAttributeMultiplier()*MULTIPLIER_HANDHELD);
		}
		return attributes;
	}
	
	
	
}
