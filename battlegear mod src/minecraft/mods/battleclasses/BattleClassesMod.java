package mods.battleclasses;

import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.blocks.BattleClassesBlocks;
import mods.battleclasses.gui.BattleClassesGuiHandler;
import mods.battleclasses.items.BattleClassesItemGem;
import mods.battleclasses.items.BattleClassesItemTestingStaff;
import mods.battleclasses.items.BattleClassesItems;
import mods.battleclasses.packet.BattleClassesPacketHandeler;
import mods.battleclasses.potion.BattleClassesPotion;
import mods.battlegear2.Battlegear;
import mods.battlegear2.utils.BattlegearConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = BattleClassesMod.MODID, version = BattleClassesMod.VERSION)
public class BattleClassesMod
{
    public static final String MODID = "battleclasses";
    public static final String VERSION = "0.0.2";
    @Mod.Instance(MODID)
    public static BattleClassesMod INSTANCE;
    @SidedProxy(modId = MODID, clientSide = "mods.battleclasses.client.BattleClassesClientProxy", serverSide = "mods.battleclasses.BattleClassesCommonProxy")
    public static BattleClassesCommonProxy proxy;
    
    public static BattleClassesPacketHandeler packetHandler;

    
    @EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
    	BattleClassesPotion.increasePotionTypesCapacity();
    	BattleClassesItems.registerItems();
		BattleClassesBlocks.registerBlocks();
		BattleClassesBlocks.registerWorldGenerator();
		
		
		proxy.registerKeyHandelers();
        proxy.registerTickHandelers();
        proxy.registerItemRenderers();
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)  {
    	//BattlegearConfig.registerRecipes();
        packetHandler = new BattleClassesPacketHandeler();
        packetHandler.registerChannels();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new BattleClassesGuiHandler());
    }
		
	@EventHandler
	public static void serverStart(FMLServerStartingEvent event){
        BattleClassesCommand.registerCommands();
    }
}
