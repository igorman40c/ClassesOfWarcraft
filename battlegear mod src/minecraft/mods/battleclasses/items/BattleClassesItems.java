package mods.battleclasses.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BattleClassesItems {
	
    public static final BattleClassesItemGem itemGem = new BattleClassesItemGem();
	
	public static void registerItems() {
		GameRegistry.registerItem(new BattleClassesItemTestingStaff(ToolMaterial.WOOD), "woodenStaff");
		
		 
		GameRegistry.registerItem(itemGem, "gemItem");
		/*
		item.metaitem_white.name=Metaitem 0 (White)
		item.metaitem_black.name=Metaitem 1 (Black)
		item.metaitem_red.name=Metaitem 2 (Red)
		item.metaitem_green.name=Metaitem 3 (Green)
		item.metaitem_yellow.name=Metaitem 4 (Yellow)
		item.metaitem_blue.name=Metaitem 5 (Blue)
		*/
		
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 0), "True Diamond");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 1), "Ruby");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 2), "Shappire");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 3), "Lion's eye");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 4), "Amethyst");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 5), "Topaz");
		LanguageRegistry.addName(new ItemStack(itemGem, 1, 6), "Talasite");
	}
	
}
