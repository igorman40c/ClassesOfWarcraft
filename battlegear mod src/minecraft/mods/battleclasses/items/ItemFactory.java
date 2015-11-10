package mods.battleclasses.items;

import java.util.EnumMap;
import java.util.EnumSet;

import cpw.mods.fml.common.registry.GameRegistry;
import mods.battleclasses.BattleClassesMetaConfig.AttributeConfig;
import mods.battleclasses.attributes.AttributesFactory;
import mods.battleclasses.attributes.AttributesFactory.WeaponDamageCreationMode;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesArmorType;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.items.weapons.BattleClassesItemLongBow;
import mods.battleclasses.items.weapons.BattleClassesItemWeapon;
import mods.battleclasses.items.weapons.BattleClassesItemWeaponTwoHanded;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Utility class to create armors and weapons.
 * @author Zsolt Molnár
 */
public class ItemFactory {
	
	public static final ArmorMaterial ARMOR_MATERIAL_CLOTH = EnumHelper.addArmorMaterial(EnumBattleClassesArmorType.CLOTH.getUnlocalizedName(), 33, new int[]{1, 2, 1, 1}, 10);
	public static final ArmorMaterial ARMOR_MATERIAL_LEATHER = EnumHelper.addArmorMaterial(EnumBattleClassesArmorType.LEATHER.getUnlocalizedName(), 33, new int[]{1, 4, 3, 2}, 10);
	public static final ArmorMaterial ARMOR_MATERIAL_MAIL = EnumHelper.addArmorMaterial(EnumBattleClassesArmorType.MAIL.getUnlocalizedName(), 33, new int[]{2, 6, 5, 2}, 10);
	public static final ArmorMaterial ARMOR_MATERIAL_PLATE = EnumHelper.addArmorMaterial(EnumBattleClassesArmorType.PLATE.getUnlocalizedName(), 33, new int[]{3, 8, 6, 3}, 10);
	
	public static void registerItem(Item item) {
		GameRegistry.registerItem(item, item.getUnlocalizedName());
	}
	
	public static void registerItems(Item[] items) {
		for(int i = 0; i < items.length; ++i) {
			registerItem(items[i]);
		}
	}

	
	public static BattleClassesItemArmor[] createArmorSet(EnumSet<EnumBattleClassesPlayerClass> classes, ArmorMaterial material, int itemLevel, String name, String MODID) {
		BattleClassesItemArmor[] armorSet = new BattleClassesItemArmor[4];
		for(int i = 0; i < 4; ++i) {
			int armorType = i; //Slot tpye integer 
			armorSet[i] = createArmorItem(classes, material, armorType, itemLevel, name, MODID);
		}
		return armorSet;
	}
	
	public static BattleClassesItemArmor createArmorItem(EnumSet<EnumBattleClassesPlayerClass> classes, ArmorMaterial material, int armorType, int itemLevel, String name, String MODID) {
		return new BattleClassesItemArmor(classes, material, armorType, itemLevel, MODID, name);
	}
	
	/*
	public static BattleClassesItemWeapon createWeapon(EnumSet<EnumBattleClassesPlayerClass> classes, EnumBattleClassesHandHeldType handHeldType, String MODID, String name, int itemLevel, EnumSet<EnumBattleClassesAttributeType> types, float weaponSpeed, WeaponDamageCreationMode weaponDamageMode) {
		BattleClassesItemWeapon weapon = new BattleClassesItemWeapon();
		weapon.setName(MODID, name);
		weapon.setClassAccess(classes);
		weapon.setItemLevelAndHeldType(itemLevel, handHeldType, weaponSpeed);
		return weapon;
	}
	*/
	
	public static void setPhysicalMeleeAttributesForWeapon(BattleClassesItemWeapon weapon, 
			EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes) {
		AttributeConfig attributeConfigurator = AttributeConfig.PhysicalMelee.INSTANCE;
		float weaponSpeed = (weapon.getHeldType() == EnumBattleClassesHandHeldType.TWO_HANDED) ? attributeConfigurator.getStandardWeaponSpeed_TwoHanded() : attributeConfigurator.getStandardWeaponSpeed_OneHanded();
		weapon.setWeaponSpeed(weaponSpeed);
		BattleClassesAttributes attributes = AttributesFactory.createMeleeWeaponAttributes(weapon.getItemLevel(), weaponSpeed, primaryTypes, secondaryTypes, weapon.getHeldType(), attributeConfigurator);
		weapon.setAttributes(attributes);
	}
	
	public static void setPhysicalRangedAttributesForWeapon(BattleClassesItemWeapon weapon, 
			EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes) {
		AttributeConfig attributeConfigurator = AttributeConfig.PhysicalRanged.INSTANCE;
		float weaponSpeed = attributeConfigurator.getStandardWeaponSpeed_TwoHanded();
		weapon.setWeaponSpeed(weaponSpeed);
		BattleClassesAttributes attributes = AttributesFactory.createRangedWeaponAttributes(weapon.getItemLevel(), weaponSpeed, primaryTypes, secondaryTypes, weapon.getHeldType(), attributeConfigurator);
		weapon.setAttributes(attributes);
	}
	
	public static void setMagicalSpellAttributesForWeapon(BattleClassesItemWeapon weapon, 
			EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes) {
		AttributeConfig attributeConfigurator = AttributeConfig.MagicalSpell.INSTANCE;
		float weaponSpeed = (weapon.getHeldType() == EnumBattleClassesHandHeldType.TWO_HANDED) ? attributeConfigurator.getStandardWeaponSpeed_TwoHanded() : attributeConfigurator.getStandardWeaponSpeed_OneHanded();
		weapon.setWeaponSpeed(weaponSpeed);
		BattleClassesAttributes attributes = AttributesFactory.createMeleeWeaponAttributes(weapon.getItemLevel(), weaponSpeed, primaryTypes, secondaryTypes, weapon.getHeldType(), attributeConfigurator);
		weapon.setAttributes(attributes);
	}
	
	public static void setPhysicalMeleAttributesForArmor(BattleClassesItemArmor armor, EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes) {
		AttributeConfig attributeConfigurator = AttributeConfig.PhysicalMelee.INSTANCE;
		setArmorAttributes(armor, primaryTypes, secondaryTypes, attributeConfigurator);
	}
	
	public static void setPhysicalRangeAttributesForArmor(BattleClassesItemArmor armor, EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes) {
		AttributeConfig attributeConfigurator = AttributeConfig.PhysicalRanged.INSTANCE;
		setArmorAttributes(armor, primaryTypes, secondaryTypes, attributeConfigurator);
	}
	
	public static void setMagicalSpellAttributesForArmor(BattleClassesItemArmor armor, EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes) {
		AttributeConfig attributeConfigurator = AttributeConfig.MagicalSpell.INSTANCE;
		setArmorAttributes(armor, primaryTypes, secondaryTypes, attributeConfigurator);
	}
	
	private static void setArmorAttributes(BattleClassesItemArmor armor, EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes, AttributeConfig attributeConfigurator) {
		BattleClassesAttributes attributes = AttributesFactory.createArmorAttributes(armor.getItemLevel(), primaryTypes, secondaryTypes, attributeConfigurator);
		armor.setAttributes(attributes);
	}
	
	public static BattleClassesItemWeapon createWeapon(EnumSet<EnumBattleClassesPlayerClass> classes, EnumBattleClassesHandHeldType handHeldType, int itemLevel, String name, String MODID) {
		BattleClassesItemWeapon weapon = new BattleClassesItemWeapon();
		weapon.setName(MODID, name);
		weapon.setClassAccess(classes);
		weapon.setItemLevelAndHeldType(itemLevel, handHeldType);
		return weapon;
	}
	
	public static BattleClassesItemWeaponTwoHanded createTwoHandedWeapon(EnumSet<EnumBattleClassesPlayerClass> classes, int itemLevel, String name, String MODID) {
		BattleClassesItemWeaponTwoHanded weapon = new BattleClassesItemWeaponTwoHanded();
		weapon.setName(MODID, name);
		weapon.setClassAccess(classes);
		weapon.setItemLevelAndHeldType(itemLevel, EnumBattleClassesHandHeldType.TWO_HANDED);
		return weapon;
	}
	
	public static BattleClassesItemLongBow createBow(EnumSet<EnumBattleClassesPlayerClass> classes, int itemLevel, String name, String MODID) {
		BattleClassesItemLongBow bow = new BattleClassesItemLongBow(4);
		bow.setName(MODID, name);
		bow.setClassAccess(classes);
		bow.setItemLevelAndHeldType(itemLevel, EnumBattleClassesHandHeldType.TWO_HANDED);
		return bow;
	}
	
	public static Item createShield() {
		//TODO
		return null;
	}
	
	public static Item createHeldInOffhand() {
		//TODO
		return null;
	}
	
}
