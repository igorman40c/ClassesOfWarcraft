package mods.battleclasses.items;

import java.util.ArrayList;
import java.util.EnumSet;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.attributes.AttributesFactory.WeaponDamageCreationMode;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraftforge.common.util.EnumHelper;

public class BattleClassesItem {
	
	//CREATIVE TABS
	public static final BattleClassesCreativeTabs TabWeapons = new BattleClassesCreativeTabs("bcweapons");
	public static final BattleClassesCreativeTabs TabArmors = new BattleClassesCreativeTabs("bcarmor");
	
	//ARMOR MATERIALS
	public static final ArmorMaterial BC_ARMOR_MATERIAL_PLATE = EnumHelper.addArmorMaterial("BC_PLATE_TYPE", 33, new int[]{3, 8, 6, 3}, 10);
	public static final ArmorMaterial BC_ARMOR_MATERIAL_MAIL = EnumHelper.addArmorMaterial("BC_MAIL_TYPE", 33, new int[]{2, 6, 5, 2}, 10);
	public static final ArmorMaterial BC_ARMOR_MATERIAL_LEATHER = EnumHelper.addArmorMaterial("BC_LEATHER_TYPE", 33, new int[]{1, 4, 3, 2}, 10);
	public static final ArmorMaterial BC_ARMOR_MATERIAL_CLOTH = EnumHelper.addArmorMaterial("BC_CLOTH_TYPE", 33, new int[]{1, 2, 1, 1}, 10);
	
	//MISC ITEMS
    public static final BattleClassesItemGem itemGem = new BattleClassesItemGem();
    
    //WEAPON ITEMS
    public static ArrayList<IHighDetailWeapon> TwoHandedWeaponList = new ArrayList<IHighDetailWeapon>();
    public static final BattleClassesItemWeaponTwoHanded itemStaffWood = ItemFactory.createTwoHandedWeapon(EnumSet.of(EnumBattleClassesPlayerClass.MAGE), 0, "StaffWood", BattleClassesMod.MODID).setAnchorAndInvertSheat(0.45F, true);
    public static final BattleClassesItemWeaponTwoHanded itemStaffCoal = ItemFactory.createTwoHandedWeapon(EnumSet.of(EnumBattleClassesPlayerClass.MAGE), 1, "StaffCoal", BattleClassesMod.MODID).setAnchorAndInvertSheat(0.45F, true);
    public static final BattleClassesItemWeaponTwoHanded itemStaffRedstone = ItemFactory.createTwoHandedWeapon(EnumSet.of(EnumBattleClassesPlayerClass.MAGE), 2, "StaffRedstone", BattleClassesMod.MODID).setAnchorAndInvertSheat(0.45F, true);
    public static final BattleClassesItemWeaponTwoHanded itemStaffEmerald = ItemFactory.createTwoHandedWeapon(EnumSet.of(EnumBattleClassesPlayerClass.MAGE), 3, "StaffEmerald", BattleClassesMod.MODID).setAnchorAndInvertSheat(0.45F, true);
    
    public static final BattleClassesItemLongBow itemLongBowWooden = ItemFactory.createBow(EnumSet.of(EnumBattleClassesPlayerClass.HUNTER), 0, "LongBowWooden", BattleClassesMod.MODID); 
    public static final BattleClassesItemLongBow itemLongBowComposite = ItemFactory.createBow(EnumSet.of(EnumBattleClassesPlayerClass.HUNTER), 1, "LongBowComposite", BattleClassesMod.MODID);
    public static final BattleClassesItemLongBow itemLongBowMechanic = ItemFactory.createBow(EnumSet.of(EnumBattleClassesPlayerClass.HUNTER), 2, "LongBowMechanic", BattleClassesMod.MODID);
    public static final BattleClassesItemLongBow itemLongBowBirch = ItemFactory.createBow(EnumSet.of(EnumBattleClassesPlayerClass.HUNTER), 3, "LongBowBirch", BattleClassesMod.MODID);
    
    public static final BattleClassesItemWeaponTwoHanded itemBroadSwordWooden = ItemFactory.createTwoHandedWeapon(EnumSet.of(EnumBattleClassesPlayerClass.WARRIOR), 0, "BroadSwordWood", BattleClassesMod.MODID);
    public static final BattleClassesItemWeaponTwoHanded itemBroadSwordStone = ItemFactory.createTwoHandedWeapon(EnumSet.of(EnumBattleClassesPlayerClass.WARRIOR), 1, "BroadSwordStone", BattleClassesMod.MODID);
    public static final BattleClassesItemWeaponTwoHanded itemBroadSwordIron = ItemFactory.createTwoHandedWeapon(EnumSet.of(EnumBattleClassesPlayerClass.WARRIOR), 2, "BroadSwordIron", BattleClassesMod.MODID);
    public static final BattleClassesItemWeaponTwoHanded itemBroadSwordDiamond = ItemFactory.createTwoHandedWeapon(EnumSet.of(EnumBattleClassesPlayerClass.WARRIOR), 3, "BroadSwordDiamond", BattleClassesMod.MODID);
    public static final BattleClassesItemWeaponTwoHanded itemBroadSwordGold = ItemFactory.createTwoHandedWeapon(EnumSet.of(EnumBattleClassesPlayerClass.WARRIOR), 0, "BroadSwordGold", BattleClassesMod.MODID);
    
    //TESTING STUFF ITEMS
    public static BattleClassesItemArmor[] testingSet;
    
	public static void registerItems() {
		
		EnumSet<EnumBattleClassesAttributeType> magePrimaryTypes = EnumSet.of(EnumBattleClassesAttributeType.SPELLPOWER_FIRE, EnumBattleClassesAttributeType.SPELLPOWER_ARCANE, EnumBattleClassesAttributeType.SPELLPOWER_FROST);
		ItemFactory.setMagicalSpellAttributesForWeapon(itemStaffWood, magePrimaryTypes, null);
		ItemFactory.registerItem(itemStaffWood);
		ItemFactory.setMagicalSpellAttributesForWeapon(itemStaffCoal, magePrimaryTypes, null);
		ItemFactory.registerItem(itemStaffCoal);
		ItemFactory.setMagicalSpellAttributesForWeapon(itemStaffRedstone, magePrimaryTypes, null);
		ItemFactory.registerItem(itemStaffRedstone);
		ItemFactory.setMagicalSpellAttributesForWeapon(itemStaffEmerald, magePrimaryTypes, null);
		ItemFactory.registerItem(itemStaffEmerald);
		
		EnumSet<EnumBattleClassesAttributeType> hunterPrimaryTypes = EnumSet.of(EnumBattleClassesAttributeType.AGILITY);
		ItemFactory.setPhysicalRangedAttributesForWeapon(itemLongBowWooden, hunterPrimaryTypes, null);
		ItemFactory.registerItem(itemLongBowWooden);
		ItemFactory.setPhysicalRangedAttributesForWeapon(itemLongBowComposite, hunterPrimaryTypes, null);
		ItemFactory.registerItem(itemLongBowComposite);
		ItemFactory.setPhysicalRangedAttributesForWeapon(itemLongBowMechanic, hunterPrimaryTypes, null);
		ItemFactory.registerItem(itemLongBowMechanic);
		ItemFactory.setPhysicalRangedAttributesForWeapon(itemLongBowBirch, hunterPrimaryTypes, null);
		ItemFactory.registerItem(itemLongBowBirch);
		
		EnumSet<EnumBattleClassesAttributeType> warriorPrimaryTypes = EnumSet.of(EnumBattleClassesAttributeType.STRENGTH);
		ItemFactory.setPhysicalMeleeAttributesForWeapon(itemBroadSwordWooden, warriorPrimaryTypes, null);
		ItemFactory.registerItem(itemBroadSwordWooden);
		ItemFactory.setPhysicalMeleeAttributesForWeapon(itemBroadSwordStone, warriorPrimaryTypes, null);
		ItemFactory.registerItem(itemBroadSwordStone);
		ItemFactory.setPhysicalMeleeAttributesForWeapon(itemBroadSwordIron, warriorPrimaryTypes, null);
		ItemFactory.registerItem(itemBroadSwordIron);
		ItemFactory.setPhysicalMeleeAttributesForWeapon(itemBroadSwordDiamond, warriorPrimaryTypes, null);
		ItemFactory.registerItem(itemBroadSwordDiamond);
		ItemFactory.setPhysicalMeleeAttributesForWeapon(itemBroadSwordGold, warriorPrimaryTypes, null);
		ItemFactory.registerItem(itemBroadSwordGold);
				
		testingSet = ItemFactory.createArmorSet(EnumSet.of(EnumBattleClassesPlayerClass.MAGE), ItemFactory.ARMOR_MATERIAL_CLOTH, BattleClassesMod.MODID, "testing", 1, 
				EnumSet.of(EnumBattleClassesAttributeType.HEALTH, EnumBattleClassesAttributeType.SPELLPOWER_FIRE, EnumBattleClassesAttributeType.CRITICAL_RATING ));
		ItemFactory.registerItems(testingSet);
		
		
		GameRegistry.registerItem(itemGem, "gemItem");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 0), "True Diamond");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 1), "Ruby");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 2), "Shappire");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 3), "Lion's eye");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 4), "Amethyst");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 5), "Topaz");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 6), "Talasite");
		
		TabWeapons.tabIconItem = itemLongBowWooden;
		TabArmors.tabIconItem = testingSet[0];
	}
	
	public static BattleClassesItemWeapon createWeapon(String name, int itemLevel, EnumSet<EnumBattleClassesPlayerClass> classes) {
		return null;
	}
	
}
