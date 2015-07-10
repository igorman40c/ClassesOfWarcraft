package mods.battleclasses.items;

import java.util.ArrayList;
import java.util.EnumSet;

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
	public static ArmorMaterial BC_ARMOR_MATERIAL_PLATE = EnumHelper.addArmorMaterial("BC_PLATE_TYPE", 33, new int[]{3, 8, 6, 3}, 10);
	
	//MISC ITEMS
    public static final BattleClassesItemGem itemGem = new BattleClassesItemGem();
    
    //WEAPON ITEMS
    public static final BattleClassesItemTestingStaff itemTestingStaff = new BattleClassesItemTestingStaff(ToolMaterial.WOOD);
    public static ArrayList<IHighDetailWeapon> TwoHandedWeaponList = new ArrayList<IHighDetailWeapon>(); 
    public static final BattleClassesItemWeaponTwoHanded itemTestingGreatStaffWooden = new BattleClassesItemWeaponTwoHanded(ToolMaterial.EMERALD, "StaffWood", 0.45F, true);
    public static final BattleClassesItemLongBow itemLongBowWooden = new BattleClassesItemLongBow(ToolMaterial.EMERALD, "LongBowWooden", 4);
    public static final BattleClassesItemLongBow itemLongBowComposite = new BattleClassesItemLongBow(ToolMaterial.EMERALD, "LongBowComposite", 4);
    public static final BattleClassesItemLongBow itemLongBowMechanic = new BattleClassesItemLongBow(ToolMaterial.EMERALD, "LongBowMechanic", 4);
    public static final BattleClassesItemLongBow itemLongBowBirch = new BattleClassesItemLongBow(ToolMaterial.EMERALD, "LongBowBirch", 4);
    public static final BattleClassesItemWeaponTwoHanded itemBroadSword = new BattleClassesItemWeaponTwoHanded(ToolMaterial.EMERALD, "BroadSwordDiamond");
    
    //ARMOR ITEMS
    public static BattleClassesItemArmor testingHelmet = new BattleClassesItemArmor(BC_ARMOR_MATERIAL_PLATE, 0, 1, "testing");
    public static BattleClassesItemArmor testingChestplate = new BattleClassesItemArmor(BC_ARMOR_MATERIAL_PLATE, 1, 1, "testing");
    public static BattleClassesItemArmor testingLeggings = new BattleClassesItemArmor(BC_ARMOR_MATERIAL_PLATE, 2, 1, "testing");
    public static BattleClassesItemArmor testingBoots = new BattleClassesItemArmor(BC_ARMOR_MATERIAL_PLATE, 3, 1, "testing");

    
	public static void registerItems() {
		GameRegistry.registerItem(itemGem, "gemItem");
		GameRegistry.registerItem(itemTestingStaff, itemTestingStaff.getUnlocalizedName());
		GameRegistry.registerItem(itemTestingGreatStaffWooden, itemTestingGreatStaffWooden.getUnlocalizedName());
		GameRegistry.registerItem(itemLongBowWooden, itemLongBowWooden.getUnlocalizedName());
		GameRegistry.registerItem(itemLongBowComposite, itemLongBowComposite.getUnlocalizedName());
		GameRegistry.registerItem(itemLongBowMechanic, itemLongBowMechanic.getUnlocalizedName());
		GameRegistry.registerItem(itemLongBowBirch, itemLongBowBirch.getUnlocalizedName());
		GameRegistry.registerItem(itemBroadSword, itemBroadSword.getUnlocalizedName());
		
		GameRegistry.registerItem(testingHelmet, testingHelmet.getUnlocalizedName());
		GameRegistry.registerItem(testingChestplate, testingChestplate.getUnlocalizedName());
		GameRegistry.registerItem(testingLeggings, testingLeggings.getUnlocalizedName());
		GameRegistry.registerItem(testingBoots, testingBoots.getUnlocalizedName());

		/*
		item.metaitem_white.name=Metaitem 0 (White)
		item.metaitem_black.name=Metaitem 1 (Black)
		item.metaitem_red.name=Metaitem 2 (Red)
		item.metaitem_green.name=Metaitem 3 (Green)
		item.metaitem_yellow.name=Metaitem 4 (Yellow)
		item.metaitem_blue.name=Metaitem 5 (Blue)
		*/
		
		LanguageRegistry.addName(new ItemStack(itemBroadSword), "Broadsword of testing and debugging");
		LanguageRegistry.addName(new ItemStack(itemTestingStaff), "Staff of testing and debugging");
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
		
		TabWeapons.tabIconItem = itemTestingStaff;
		TabArmors.tabIconItem = testingHelmet;
	}
	
	public static BattleClassesItemWeapon createWeapon(String name, int itemLevel, EnumSet<EnumBattleClassesPlayerClass> classes) {
		return null;
	}
	
}
