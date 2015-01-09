package mods.battleclasses.items;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BattleClassesItems {
	
    public static final BattleClassesItemGem itemGem = new BattleClassesItemGem();
    public static final BattleClassesItemTestingStaff itemTestingStaff = new BattleClassesItemTestingStaff(ToolMaterial.WOOD);
    
    public static ArrayList<IHighDetailWeapon> TwoHandedWeaponList = new ArrayList<IHighDetailWeapon>(); 
    public static final BattleClassesItemWeaponTwoHanded itemTestingBroadSword = new BattleClassesItemWeaponTwoHanded(ToolMaterial.EMERALD, "BroadSword");
    public static final BattleClassesItemWeaponTwoHanded itemTestingGreatStaff = new BattleClassesItemWeaponTwoHanded(ToolMaterial.EMERALD, "GreatStaff", 0.45F, true);
    public static final BattleClassesItemWeaponTwoHanded itemTestingWarHammer = new BattleClassesItemWeaponTwoHanded(ToolMaterial.EMERALD, "WarHammer", 0.35F, true);
    public static final BattleClassesItemWeaponTwoHanded itemTestingGreatStaffWooden = new BattleClassesItemWeaponTwoHanded(ToolMaterial.EMERALD, "WoodenStaffGreat", 0.45F, true);

    
	public static void registerItems() {
		GameRegistry.registerItem(itemGem, "gemItem");
		GameRegistry.registerItem(itemTestingStaff, itemTestingStaff.getUnlocalizedName());
		GameRegistry.registerItem(itemTestingBroadSword, itemTestingBroadSword.getUnlocalizedName());
		GameRegistry.registerItem(itemTestingGreatStaff, itemTestingGreatStaff.getUnlocalizedName());
		GameRegistry.registerItem(itemTestingGreatStaffWooden, itemTestingGreatStaffWooden.getUnlocalizedName());
		GameRegistry.registerItem(itemTestingWarHammer, itemTestingWarHammer.getUnlocalizedName());

		/*
		item.metaitem_white.name=Metaitem 0 (White)
		item.metaitem_black.name=Metaitem 1 (Black)
		item.metaitem_red.name=Metaitem 2 (Red)
		item.metaitem_green.name=Metaitem 3 (Green)
		item.metaitem_yellow.name=Metaitem 4 (Yellow)
		item.metaitem_blue.name=Metaitem 5 (Blue)
		*/
		LanguageRegistry.addName(new ItemStack(itemTestingStaff), "Staff of testing and debugging");
		LanguageRegistry.addName(new ItemStack(itemTestingGreatStaffWooden), "Staff of testing and debugging");
		
		LanguageRegistry.addName(new ItemStack(itemTestingBroadSword), "Broadsword of testing");
		LanguageRegistry.addName(new ItemStack(itemTestingGreatStaff), "Great staff of testing");
		LanguageRegistry.addName(new ItemStack(itemTestingWarHammer), "Warhammer of testing");

		LanguageRegistry.addName(new ItemStack(itemGem, 1, 0), "True Diamond");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 1), "Ruby");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 2), "Shappire");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 3), "Lion's eye");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 4), "Amethyst");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 5), "Topaz");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 6), "Talasite");
	}
	
}
