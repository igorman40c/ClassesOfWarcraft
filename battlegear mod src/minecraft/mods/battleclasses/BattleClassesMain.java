package mods.battleclasses;

import java.net.URL;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.blocks.BattleClassesBlocks;
import mods.battleclasses.items.BattleClassesItemGem;
import mods.battleclasses.items.BattleClassesItemTestingStaff;
import mods.battlegear2.api.core.BattlegearTranslator;
import mods.battlegear2.api.weapons.CommandWeaponWield;
import mods.battlegear2.utils.BattlegearConfig;

//BattleClasses registration goes here
public class BattleClassesMain {
	
	public static final BattleClassesItemGem itemGem = new BattleClassesItemGem();
	
	public static void preInit(FMLPreInitializationEvent event) {
		//Testing
		BattleClassesBlocks.registerBlocks();
		BattleClassesBlocks.registerWorldGenerator();
		
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
	
	public static void init() {
		BattleClassesUtils.Log("Starting Battle Classes initizalization!", LogType.INIT);
		
	}
	
	public static void serverStart(FMLServerStartingEvent event){
		BattleClassesUtils.Log("FMLServerStartingEvent!", LogType.INIT);
        BattleClassesCommand.registerCommands();
    }
	
}
