package mods.battleclasses.client;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.gui.BattleClassesGuiKeyHandler;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class BattleClassesClientTickHandler {

	public static final BattleClassesClientTickHandler INSTANCE = new BattleClassesClientTickHandler();
	/*
	private boolean previouslyOnBattleMode = false;
	@SubscribeEvent
    public void updateKeybinds(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START){
        	Minecraft mc = Minecraft.getMinecraft();
            if(mc != null && mc.thePlayer != null) {
            	boolean shouldUpdateKeybinds = previouslyOnBattleMode != BattleClassesUtils.isPlayerInBattlemode(mc.thePlayer);
            	if(shouldUpdateKeybinds) {
            		System.out.println("Updating keybinds");
            		if(BattleClassesUtils.isPlayerInBattlemode(mc.thePlayer)) {
                		BattleClassesGuiKeyHandler.INSTANCE.enableAbilityActionBarKeyHandling();
                		previouslyOnBattleMode = true;
                	}
                	else {
                		BattleClassesGuiKeyHandler.INSTANCE.disableAbilityActionBarKeyHandling();
                		previouslyOnBattleMode = false;
                	}
            	}
            }
        }
    }
    */
}
