package mods.battleclasses.gui;

import java.util.ArrayList;

import mods.battleclasses.BattleClassesUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

import org.lwjgl.input.Keyboard;

public class BattleClassesGuiKeyHandler {
	
	private final ArrayList<KeyBinding> actionbarKeybinds;
	public static final BattleClassesGuiKeyHandler INSTANCE = new BattleClassesGuiKeyHandler();
	
	public BattleClassesGuiKeyHandler() {
		//Registering ability actionbar keybinds
		actionbarKeybinds = new ArrayList<KeyBinding>();
		for(int i = 1; i <= 9; ++i) {
			KeyBinding actionbarKeybind = new KeyBinding("Ability " + i, Keyboard.getKeyIndex(Integer.toString(i)), "bcgui.keycategories.actionbar");
			actionbarKeybinds.add(actionbarKeybind);
			ClientRegistry.registerKeyBinding(actionbarKeybind);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void keyDown(InputEvent.KeyInputEvent event) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc != null && mc.thePlayer != null && mc.theWorld != null && mc.currentScreen == null) {
			if(BattleClassesUtils.isPlayerInBattlemode(mc.thePlayer)) {
				int actionbarIndexRead = -1;
				switch(Keyboard.getEventKey()) {
					case(Keyboard.KEY_1) : {
						actionbarIndexRead = 1;
					}
						break;
					case(Keyboard.KEY_2) : {
						actionbarIndexRead = 2;
					}
						break;
					case(Keyboard.KEY_3) : {
						actionbarIndexRead = 3;
					}
						break;
					case(Keyboard.KEY_4) : {
						actionbarIndexRead = 4;
					}
						break;
					case(Keyboard.KEY_5) : {
						actionbarIndexRead = 5;
					}
						break;
					case(Keyboard.KEY_6) : {
						actionbarIndexRead = 6;
					}
						break;
					case(Keyboard.KEY_7) : {
						actionbarIndexRead = 7;
					}
						break;
					case(Keyboard.KEY_8) : {
						actionbarIndexRead = 8;
					}
						break;
					case(Keyboard.KEY_9) : {
						actionbarIndexRead = 9;
					}
						break;
					default:
						break;
				}
				if(actionbarIndexRead >= 1 && actionbarIndexRead <= 9) {
					BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).setChosenAbilityIndex(actionbarIndexRead-1);
				}
			}
		}
	}
	
}
