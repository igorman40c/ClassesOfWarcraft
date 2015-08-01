package mods.battleclasses.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.active.BattleClassesAbilityShieldBlock;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.core.BattleClassesWeaponHitHandler;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.gui.BattleClassesGuiHUDOverlay;
import mods.battleclasses.gui.BattleClassesGuiKeyHandler;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonClassSelector;
import mods.battleclasses.gui.controlls.BattleClassesGuiTabBarButton;
import mods.battleclasses.gui.controlls.GuiTabBarButtonBattleInventory;
import mods.battleclasses.gui.controlls.GuiTabBarButtonClassSelector;
import mods.battleclasses.gui.controlls.GuiTabBarButtonConfig;
import mods.battleclasses.gui.controlls.GuiTabBarButtonHelp;
import mods.battleclasses.gui.controlls.GuiTabBarButtonSpellbook;
import mods.battleclasses.gui.controlls.GuiTabBarButtonTalentSelector;
import mods.battleclasses.gui.controlls.GuiTabBarButtonVanillaInventory;
import mods.battleclasses.gui.tab.BattleClassesTabInventory;
import mods.battleclasses.gui.tab.BattleClassesTabClassSelector;
import mods.battleclasses.items.IAttributeProviderItem;
import mods.battleclasses.packet.BattleClassesPacketAttributeChanges;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battleclasses.packet.BattleClassesPacketPlayerDataSync;
import mods.battlegear2.Battlegear;
import mods.battlegear2.BattlemodeHookContainerClass;
import mods.battlegear2.api.RenderItemBarEvent;
import mods.battlegear2.api.RenderItemBarEvent.ShieldBar;
import mods.battlegear2.api.core.IBattlePlayer;
import mods.battlegear2.api.weapons.IBackStabbable;
import mods.battlegear2.api.weapons.IExtendedReachWeapon;
import mods.battlegear2.api.weapons.IHitTimeModifier;
import mods.battlegear2.api.weapons.IPenetrateWeapon;
import mods.battlegear2.client.BattlegearClientEvents;
import mods.battlegear2.client.gui.BattlegearInGameGUI;
import mods.battlegear2.client.gui.controls.GuiBGInventoryButton;
import mods.battlegear2.client.gui.controls.GuiPlaceableButton;
import mods.battlegear2.client.gui.controls.GuiSigilButton;
import mods.battlegear2.items.ItemWeapon;
import mods.battlegear2.utils.BattlegearConfig;
import mods.battlegear2.utils.EnumBGAnimations;

public class BattleClassesClientEvents {
	
	public static final BattleClassesClientEvents INSTANCE = new BattleClassesClientEvents(); 
	private final BattleClassesGuiHUDOverlay inGameGUI = new BattleClassesGuiHUDOverlay();
	public static BattleClassesClientAbilityActivityRegistry activityRegistry = new BattleClassesClientAbilityActivityRegistry();
	public static BattleClassesGuiTabBarButton lastUsedTabButton = null;
	public static List<BattleClassesGuiTabBarButton> tabsButtonList = new ArrayList<BattleClassesGuiTabBarButton>();
	static {
		tabsButtonList.add(new GuiTabBarButtonVanillaInventory(0, 10, 10));
		tabsButtonList.add(new GuiTabBarButtonBattleInventory(1, 20, 20));
		tabsButtonList.add(new GuiTabBarButtonSpellbook(2, 30, 30));
		tabsButtonList.add(new GuiTabBarButtonTalentSelector(3, 30, 30));
		tabsButtonList.add(new GuiTabBarButtonClassSelector(4, 40, 40));
		
		//tabsButtonList.add(new GuiTabBarButtonConfig(5, 60, 60, false));
		//tabsButtonList.add(new GuiTabBarButtonHelp(6, 60, 60, false));
	}
		
	/*
	@SubscribeEvent
	public void replaceOpenGuiInventory(PlayerOpenContainerEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.currentScreen != null && mc.thePlayer != null) {
			if(mc.currentScreen.getClass() == GuiInventory.class) {
				mc.currentScreen = new BattleClassesGuiInventory(mc.thePlayer);
			}
		}
	}
	*/
	
	@SubscribeEvent
	public void replaceGuiInventory(GuiOpenEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		//Replacing GuiInventory when needed
		BattleClassesUtils.Log("GuiOpenEvent!", LogType.GUI);
		if(event.gui != null && event.gui.getClass().equals(GuiInventory.class)) {
			event.setCanceled(true);
			mc.displayGuiScreen(new BattleClassesTabInventory(mc.thePlayer));
			BattleClassesUtils.Log("GuiInventory replaced!", LogType.GUI);
			return;
			//this.onOpenGui(mc.currentScreen., guiLeft, guiTop);
		}
		if(event.gui != null) {
			returnToLastUsedTab(event);
		}
	}
	
	public static void returnToLastUsedTab(GuiOpenEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean canReturnEventGuiScreen = false;
		for (BattleClassesGuiTabBarButton button : tabsButtonList) {
			if(event.gui.getClass() == button.getGUIClass() && !(mc.thePlayer.capabilities.isCreativeMode)) {
				canReturnEventGuiScreen = true;
			}
		}
		if(canReturnEventGuiScreen &&
			lastUsedTabButton != null &&
			lastUsedTabButton.isAccessAble() &&
			event.gui.getClass() != lastUsedTabButton.getGUIClass()) {
				event.setCanceled(true);
				lastUsedTabButton.openGui(mc);
		}
	}
	
	@SubscribeEvent
	public void playerJoinWorldEvent(EntityJoinWorldEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer)event.entity;
			if(entityPlayer == mc.thePlayer) {
				BattleClassesUtils.Log("Player joined! Requesting PlayerData", LogType.CORE);
				FMLProxyPacket p = new BattleClassesPacketPlayerDataSync().generatePacket();
				BattleClassesMod.packetHandler.sendPacketToServer(p);
			}
		}
	}
	
	@SubscribeEvent
	public void renderShieldBarEvent(ShieldBar event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).hasAbilityByID(BattleClassesAbilityShieldBlock.INSTANCE.getAbilityID())) {
			event.setCanceled(true);
		}
	}
	
	private ItemStack savedMainhandItemStack;
	private ItemStack savedOffhandItemStack;
	private ItemStack[] savedArmorItemStacks = new ItemStack[4];
	@SubscribeEvent
	public void updateEquipment(LivingUpdateEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(event.entity == mc.thePlayer && mc.thePlayer.inventory.inventoryChanged) {
			
			//Checking for equipment change
			boolean shouldUpdateEquipment = false;
			String reason = "";
			if(!ItemStack.areItemStacksEqual(BattleClassesUtils.getMainhandItemStack(mc.thePlayer),savedMainhandItemStack)) {
				shouldUpdateEquipment = true;
				reason = "reason: Mainhand item";
			}
			if(!shouldUpdateEquipment && !ItemStack.areItemStacksEqual(BattleClassesUtils.getOffhandItemStack(mc.thePlayer),savedOffhandItemStack)) {
				shouldUpdateEquipment = true;
				reason = "reason: Offhand";
			}
			if(!shouldUpdateEquipment && savedArmorItemStacks != null) {
				for(int i = 0; i < savedArmorItemStacks.length; ++i) {
					if(!ItemStack.areItemStacksEqual(savedArmorItemStacks[i],mc.thePlayer.inventory.armorInventory[i])) {
						shouldUpdateEquipment = true;
						reason = "reason: Armor at: " +i;
						break;
					}
				}
			}
			
			if(shouldUpdateEquipment) {
				//Updating attribute sources
				System.out.println("Equipment Changed! " + reason);
				FMLProxyPacket p = new BattleClassesPacketAttributeChanges().generatePacket();
				BattleClassesMod.packetHandler.sendPacketToServer(p);
				BattleClassesUtils.getPlayerHooks(mc.thePlayer).onAttributeSourcesChanged();
				//Saving current equipment
				savedMainhandItemStack = BattleClassesUtils.getMainhandItemStack(mc.thePlayer);
				savedOffhandItemStack = BattleClassesUtils.getOffhandItemStack(mc.thePlayer);
				savedArmorItemStacks = new ItemStack[4];
				for(int i = 0; i < mc.thePlayer.inventory.armorInventory.length; ++i) {
					savedArmorItemStacks[i] = mc.thePlayer.inventory.armorInventory[i];
				}
			}
		}
	}
	
	
	
	@SubscribeEvent
	public void postRenderOverlay(RenderGameOverlayEvent.Post event) {
		if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
			inGameGUI.renderGameOverlay(event.partialTicks, event.mouseX, event.mouseY);
		}
		if (event.type == RenderGameOverlayEvent.ElementType.HEALTH) {
			inGameGUI.drawCastbar();
			inGameGUI.drawHighLightedLabels();
			inGameGUI.finishDrawing();
		}
	}
	
	@SubscribeEvent
	public void preRenderOverlay(RenderGameOverlayEvent.Pre event) {
		if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
			event.setCanceled(true);
			inGameGUI.drawBossHealth();
		}
	}
	
	public static final int TABS_ON_LEFT = 5; 
	
    /**
     * Helper method to add buttons to a gui when opened
     * @param buttons the List<GuiButton> of the opened gui
     * @param guiLeft horizontal placement parameter
     * @param guiTop vertical placement parameter
     */
	public static void onOpenGui(List buttons, int guiLeft, int guiTop) {
		BattleClassesUtils.Log("Adding Extra inventory buttons", LogType.GUI);
        if(BattlegearConfig.enableGuiButtons){
			int i = 0;
			for (BattleClassesGuiTabBarButton button : tabsButtonList) {
				if(i < TABS_ON_LEFT) {
					//Placing to horizontal bar button positions
					button.setPosition(guiLeft + 0, guiTop + 11 + i * (BattleClassesGuiTabBarButton.BAR_BUTTON_GAP + BattleClassesGuiTabBarButton.BAR_BUTTON_SIZE_H_H) );
				}
				else {
					//Placing to vertical bar button positions
					button.setPosition(guiLeft + 28 + 11 + (i - 5) *(BattleClassesGuiTabBarButton.BAR_BUTTON_GAP + BattleClassesGuiTabBarButton.BAR_BUTTON_SIZE_W_V),
							guiTop + 163);
				}
				button.id = buttons.size()+70; //Due to GuiInventory and GuiContainerCreative button performed actions, without them having buttons...
				i++;
				buttons.add(button);
			}
        }
	}
	
	public static void showButtonsFromGuiInventory(GuiInventory guiInventory) {
		if(guiInventory instanceof GuiContainer ) {
			
			GuiContainer guiContiner = (GuiContainer)guiInventory;
			
			BattleClassesUtils.Log("Gui Opened1", LogType.GUI);
			
			Minecraft mc = Minecraft.getMinecraft();
			GuiContainer guiContainer = ((GuiContainer) mc.currentScreen);
        	
        	if(guiContainer != null) {
        		/** The X size of the inventory window in pixels. */
            	int xSize = 176;
                /** The Y size of the inventory window in pixels. */
                int ySize = 166;
            	int guiLeft = (guiContainer.width - xSize) / 2 - 32;
            	int guiTop = (guiContainer.height - ySize) / 2;
                
            	System.out.println("Current gui class:" + guiInventory);
            	
            	Field f;
				try {
					
					f = GuiScreen.class.getDeclaredField("buttonList");
					f.setAccessible(true);
                	List buttonListRefl = (List) f.get( ((GuiScreen)guiContainer) );
                	
                    
                    //BattleClassesClientEvents.onOpenGui(buttonListRefl, guiLeft, guiTop);
                    
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
        	}
			
		}
	}
	
	/**
     * Helper method to return to Inventory from displayed tabs
     */
	public static void returnToInventory(EntityPlayer entityPlayer) {
		BattleClassesUtils.Log("Trying to close opened gui!", LogType.GUI);
		if(entityPlayer instanceof EntityPlayerMP) {
    		
    		EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
    		entityPlayerMP.closeScreen();
    		
    		/*
			Minecraft mc =  Minecraft.getMinecraft();
			mc.displayGuiScreen(new GuiInventory(entityPlayer));
			*/
    	}
	}
	
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event){
        if(event.itemStack.getItem() instanceof IAttributeProviderItem){
        	Minecraft mc = Minecraft.getMinecraft();
        	IAttributeProviderItem attributeProviderItem = (IAttributeProviderItem)event.itemStack.getItem();
        	BattleClassesAttributes attributes = (attributeProviderItem).getAttributes();
        	
        	//Removing everything other than the first line
        	String firstLine = event.toolTip.get(0);
        	event.toolTip.clear();
        	event.toolTip.add(firstLine);
        	
        	//Add item specific info
        	event.toolTip.addAll(attributeProviderItem.getTooltipText());
        	
        	//Adding attribute bonuses
        	for(EnumBattleClassesAttributeType activeAttributeType : attributes.getActiveTypes()) {
        		float value = activeAttributeType.getValueFromAttributes(attributes);
        		event.toolTip.add(activeAttributeType.getBonusLineColor() + BattleClassesGuiHelper.getTranslatedBonusLine(value, activeAttributeType)); 
        	}
        	
        	//Adding class access set-string
        	EnumSet<EnumBattleClassesPlayerClass> classAccessSet = attributeProviderItem.getClassAccessSet();
        	if(!(classAccessSet.contains(EnumBattleClassesPlayerClass.NONE)) && classAccessSet.size() > 0) {
        		//Create class list string
        		String classAccessString = BattleClassesGuiHelper.createListWithTitle(StatCollector.translateToLocal("bcclass.classes"), classAccessSet);
        		//Create color of the line depeding on the player class
        		EnumChatFormatting classAccessSetDisplayColor = BattleClassesGuiHelper.getHoveringTextAvailabilityColor(BattleClassesUtils.getPlayerClassEnum(mc.thePlayer).isEligibleForClassAccessSet(classAccessSet));
        		//Adding line
        		event.toolTip.add(classAccessSetDisplayColor + classAccessString);
        	}
        	
        	//Item level line
        	event.toolTip.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("bcattribute.itemLevel") + ": " +  attributeProviderItem.getItemLevel());
        	
        	//Limiting width
        	List<String> limitedWidthTooltipText = BattleClassesGuiHelper.INSTANCE.formatHoveringTextWidth(event.toolTip, BattleClassesGuiHelper.HOVERINGTEXT_DEFAULT_WIDTH+5);
        	event.toolTip.clear();
        	event.toolTip.addAll(limitedWidthTooltipText);
        }
    }
	
	@SubscribeEvent
	public void preStitch(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 1) {
			//Registering Cooldown Icons
			BattleClassesGuiHelper.cooldownIcons = new IIcon[BattleClassesGuiHelper.COOLDOWN_FRAMES];
			for(int i = 0; i < BattleClassesGuiHelper.COOLDOWN_FRAMES; ++i) {
				int frameIndex = i + 1;
				BattleClassesGuiHelper.cooldownIcons[i] = event.map.registerIcon("battleclasses:sharedicons/cooldown/" + "cooldown_" + frameIndex);
			}
		}
		
	}
	
	private boolean offhandNext = false;
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void alterHandsOnClick(MouseEvent event) {
		if(event.button == 0 &&  event.buttonstate == true) {
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.currentScreen == null && mc.objectMouseOver != null) {
				if(BattleClassesUtils.isPlayerInBattlemode(mc.thePlayer) /*&& BattleClassesUtils.getOffhandItemStack(mc.thePlayer) != null*/) { 
					switch(mc.objectMouseOver.typeOfHit) {
						case ENTITY: {
							BattleClassesWeaponHitHandler weaponHitHandler = BattleClassesUtils.getPlayerWeaponHandler(mc.thePlayer);
							if(!weaponHitHandler.mainHandAttackAbility.isOnCooldown()) {
								System.out.println("MAIN");
								//Can use mainhand
								offhandNext = true;
								return;
							}
							else {
								event.setCanceled(true);
								System.out.println("OFF");
								if(!weaponHitHandler.offHandAttackAbility.isOnCooldown()) {
									//Can use offhand
									weaponHitHandler.processOffhandAttack(mc.thePlayer, mc.objectMouseOver.entityHit);
									offhandNext = false;
									return;
								}
							}
						}
							break;
						case MISS: {
							if(offhandNext) {
								event.setCanceled(true);
								((IBattlePlayer) mc.thePlayer).swingOffItem();
					            Battlegear.proxy.sendAnimationPacket(EnumBGAnimations.OffHandSwing, mc.thePlayer);
								offhandNext = false;
							}
							else {
								offhandNext = true;
							}
						}
							break;
						default:
							break;
					}
				}
			}
		}
	}
}
