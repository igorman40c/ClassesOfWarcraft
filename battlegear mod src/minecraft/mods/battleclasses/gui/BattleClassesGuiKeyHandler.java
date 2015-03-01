package mods.battleclasses.gui;

import java.util.ArrayList;
import java.util.Arrays;

import mods.battleclasses.BattleClassesUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

public class BattleClassesGuiKeyHandler {
	
	private ArrayList<KeyBinding> abilityHotbarKeybinds;
	public static final BattleClassesGuiKeyHandler INSTANCE = new BattleClassesGuiKeyHandler();
	
	public BattleClassesGuiKeyHandler() {
		abilityHotbarKeybinds = new ArrayList<KeyBinding>();
		for(int i = 1; i <= 9; ++i) {
			KeyBinding abilityHotbarKeybind = new KeyBinding("Ability " + i, Keyboard.getKeyIndex(Integer.toString(i)), "bcgui.keycategories.actionbar");
			abilityHotbarKeybinds.add(abilityHotbarKeybind);
			ClientRegistry.registerKeyBinding(abilityHotbarKeybind);
		}
	}
			
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void keyDown(InputEvent.KeyInputEvent event) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc != null && mc.thePlayer != null && mc.theWorld != null) {
			if(BattleClassesUtils.isPlayerInBattlemode(mc.thePlayer)) {
				int actionbarIndexRead = -1;
				int i = 1;
				for(KeyBinding keybinding : abilityHotbarKeybinds) {
					if(keybinding.getKeyCode() == Keyboard.getEventKey()) {
						actionbarIndexRead = i;
						break;
					}
					++i;
				}
				
				if(actionbarIndexRead >= 1 && actionbarIndexRead <= 9) {
					BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).setChosenAbilityIndex(actionbarIndexRead-1);
				}
			}
			else {
				for (int j = 0; j < 9; ++j)
	            {
	                if (mc.gameSettings.keyBindsHotbar[j].getKeyCode() == Keyboard.getEventKey())
	                {
	                	System.out.println("Set current item to index: " + j);
	                    mc.thePlayer.inventory.currentItem = j;
	                    //mc.playerController.syncCurrentPlayItem();
	                }
	            }
			}
		}
	}
	
}
