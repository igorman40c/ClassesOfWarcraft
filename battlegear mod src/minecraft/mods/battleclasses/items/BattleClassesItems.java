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

public class BattleClassesItems {
	
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
//    public static final BattleClassesItemTestingStaff itemTestingStaff = new BattleClassesItemTestingStaff();
    public static ArrayList<IHighDetailWeapon> TwoHandedWeaponList = new ArrayList<IHighDetailWeapon>(); 
    public static final BattleClassesItemWeaponTwoHanded itemTestingGreatStaffWooden = new BattleClassesItemWeaponTwoHanded("StaffWood", 0.45F, true);
    public static final BattleClassesItemLongBow itemLongBowWooden = new BattleClassesItemLongBow("LongBowWooden", 4);
    public static final BattleClassesItemLongBow itemLongBowComposite = new BattleClassesItemLongBow("LongBowComposite", 4);
    public static final BattleClassesItemLongBow itemLongBowMechanic = new BattleClassesItemLongBow("LongBowMechanic", 4);
    public static final BattleClassesItemLongBow itemLongBowBirch = new BattleClassesItemLongBow("LongBowBirch", 4);
    public static final BattleClassesItemWeaponTwoHanded itemBroadSword = new BattleClassesItemWeaponTwoHanded("BroadSwordDiamond");
    
    //TESTING STUFF ITEMS
    public static BattleClassesItemArmor[] testingSet;
    public static BattleClassesItemWeapon testingStaff;
    
	public static void registerItems() {
		
		GameRegistry.registerItem(itemGem, "gemItem");
//		GameRegistry.registerItem(itemTestingStaff, itemTestingStaff.getUnlocalizedName());
		GameRegistry.registerItem(itemTestingGreatStaffWooden, itemTestingGreatStaffWooden.getUnlocalizedName());
		GameRegistry.registerItem(itemLongBowWooden, itemLongBowWooden.getUnlocalizedName());
		GameRegistry.registerItem(itemLongBowComposite, itemLongBowComposite.getUnlocalizedName());
		GameRegistry.registerItem(itemLongBowMechanic, itemLongBowMechanic.getUnlocalizedName());
		GameRegistry.registerItem(itemLongBowBirch, itemLongBowBirch.getUnlocalizedName());
		GameRegistry.registerItem(itemBroadSword, itemBroadSword.getUnlocalizedName());
		
		
		testingStaff = ItemFactory.createWeapon(EnumSet.of(EnumBattleClassesPlayerClass.MAGE), EnumBattleClassesHandHeldType.ONE_HANDED, BattleClassesMod.MODID, "WoodenStaff", 1, 
				EnumSet.of(EnumBattleClassesAttributeType.SPELLPOWER_FIRE, EnumBattleClassesAttributeType.CRITICAL_RATING), 
				2F, WeaponDamageCreationMode.REAL);
		ItemFactory.registerItem(testingStaff);
		testingSet = ItemFactory.createArmorSet(EnumSet.of(EnumBattleClassesPlayerClass.MAGE), ItemFactory.ARMOR_MATERIAL_CLOTH, BattleClassesMod.MODID, "testing", 1, 
				EnumSet.of(EnumBattleClassesAttributeType.HEALTH, EnumBattleClassesAttributeType.SPELLPOWER_FIRE, EnumBattleClassesAttributeType.CRITICAL_RATING ));
		ItemFactory.registerItems(testingSet);
		
		LanguageRegistry.addName(new ItemStack(itemBroadSword), "Broadsword of testing and debugging");
//		LanguageRegistry.addName(new ItemStack(itemTestingStaff), "Staff of testing and debugging");
		LanguageRegistry.addName(new ItemStack(itemTestingGreatStaffWooden), "Staff of testing and debugging");
			
		LanguageRegistry.addName(new ItemStack(itemLongBowWooden), "Longbow");
		LanguageRegistry.addName(new ItemStack(itemLongBowWooden), "Longbow");
		LanguageRegistry.addName(new ItemStack(itemLongBowWooden), "Longbow");
		LanguageRegistry.addName(new ItemStack(itemLongBowWooden), "Longbow");
		
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 0), "True Diamond");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 1), "Ruby");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 2), "Shappire");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 3), "Lion's eye");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 4), "Amethyst");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 5), "Topaz");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 6), "Talasite");
		
		TabWeapons.tabIconItem = testingStaff;
		TabArmors.tabIconItem = testingSet[0];
	}
	
	public static BattleClassesItemWeapon createWeapon(String name, int itemLevel, EnumSet<EnumBattleClassesPlayerClass> classes) {
		return null;
	}
	
}
