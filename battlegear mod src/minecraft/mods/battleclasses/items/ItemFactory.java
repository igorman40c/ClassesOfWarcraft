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

	public static BattleClassesItemArmor[] createArmorSet(EnumSet<EnumBattleClassesPlayerClass> classes, ArmorMaterial material, String MODID, String name, int itemLevel, EnumSet<EnumBattleClassesAttributeType> types) {
		BattleClassesItemArmor[] armorSet = new BattleClassesItemArmor[4];
		for(int i = 0; i < 4; ++i) {
			int armorType = i; //Slot tpye integer 
			armorSet[i] = createArmorItem(classes, material, armorType, MODID, name, itemLevel, types);
		}
		return armorSet;
	}
	
	public static BattleClassesItemArmor createArmorItem(EnumSet<EnumBattleClassesPlayerClass> classes, ArmorMaterial material, int armorType, String MODID, String name, int itemLevel, EnumSet<EnumBattleClassesAttributeType> types) {
		return new BattleClassesItemArmor(classes, material, armorType, MODID, name, itemLevel, types);
	}
	
	public static BattleClassesItemWeapon createWeapon(EnumSet<EnumBattleClassesPlayerClass> classes, EnumBattleClassesHandHeldType handHeldType, String MODID, String name, int itemLevel, EnumSet<EnumBattleClassesAttributeType> types, float weaponSpeed, WeaponDamageCreationMode weaponDamageMode) {
		BattleClassesItemWeapon weapon = new BattleClassesItemWeapon();
		weapon.setName(MODID, name);
		weapon.setClassAccess(classes);
		weapon.setItemLevelAndHeldType(itemLevel, handHeldType, weaponSpeed);
		return weapon;
	}
	
	
	public static void setPhysicalMeleeAttributesForWeapon(BattleClassesItemWeapon weapon, EnumSet<EnumBattleClassesPlayerClass> classes,
			EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes,
			EnumBattleClassesHandHeldType handHeldType, int itemLevel, String name, String MODID) {
		AttributeConfig attributeConfigurator = AttributeConfig.PhysicalMelee.INSTANCE;
		float weaponSpeed = (handHeldType == EnumBattleClassesHandHeldType.TWO_HANDED) ? attributeConfigurator.getStandardWeaponSpeed_TwoHanded() : attributeConfigurator.getStandardWeaponSpeed_OneHanded();
		BattleClassesAttributes attributes = AttributesFactory.createMeleeWeaponAttributes(itemLevel, weaponSpeed, primaryTypes, secondaryTypes, handHeldType, attributeConfigurator);
		weapon.setAttributes(attributes);
	}
	
	public static void setPhysicalRangedAttributesForWeapon(BattleClassesItemWeapon weapon, EnumSet<EnumBattleClassesPlayerClass> classes,
			EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes,
			int itemLevel, String name, String MODID) {
		AttributeConfig attributeConfigurator = AttributeConfig.PhysicalRanged.INSTANCE;
		float weaponSpeed = attributeConfigurator.getStandardWeaponSpeed_TwoHanded();
		BattleClassesAttributes attributes = AttributesFactory.createRangedWeaponAttributes(itemLevel, weaponSpeed, primaryTypes, secondaryTypes, EnumBattleClassesHandHeldType.TWO_HANDED, attributeConfigurator);
		weapon.setAttributes(attributes);
	}
	
	public static void setMagicalSpellAttributesForWeapon(BattleClassesItemWeapon weapon, EnumSet<EnumBattleClassesPlayerClass> classes,
			EnumSet<EnumBattleClassesAttributeType> primaryTypes, EnumMap<EnumBattleClassesAttributeType, Float> secondaryTypes,
			EnumBattleClassesHandHeldType handHeldType, int itemLevel, String name, String MODID) {
		AttributeConfig attributeConfigurator = AttributeConfig.MagicalSpell.INSTANCE;
		float weaponSpeed = (handHeldType == EnumBattleClassesHandHeldType.TWO_HANDED) ? attributeConfigurator.getStandardWeaponSpeed_TwoHanded() : attributeConfigurator.getStandardWeaponSpeed_OneHanded();
		BattleClassesAttributes attributes = AttributesFactory.createMeleeWeaponAttributes(itemLevel, weaponSpeed, primaryTypes, secondaryTypes, handHeldType, attributeConfigurator);
		weapon.setAttributes(attributes);
	}
	
	public static BattleClassesItemWeapon createWeapon(EnumSet<EnumBattleClassesPlayerClass> classes, EnumBattleClassesHandHeldType handHeldType, float weaponSpeed, int itemLevel, String name, String MODID) {
		BattleClassesItemWeapon weapon = new BattleClassesItemWeapon();
		weapon.setName(MODID, name);
		weapon.setClassAccess(classes);
		weapon.setItemLevelAndHeldType(itemLevel, handHeldType, weaponSpeed);
		return weapon;
	}
	
	public static BattleClassesItemWeaponTwoHanded createTwoHandedWeapon(EnumSet<EnumBattleClassesPlayerClass> classes, float weaponSpeed, int itemLevel, String name, String MODID) {
		BattleClassesItemWeaponTwoHanded weapon = new BattleClassesItemWeaponTwoHanded();
		weapon.setName(MODID, name);
		weapon.setClassAccess(classes);
		weapon.setItemLevelAndHeldType(itemLevel, EnumBattleClassesHandHeldType.TWO_HANDED, weaponSpeed);
		return weapon;
	}
	
	public static Item createShield() {
		//TODO
		return null;
	}
	
	public static Item createHeldInOffhand() {
		//TODO
		return null;
	}
	
	public static Item createBow() {
		//TODO
		return null;
	}
}
