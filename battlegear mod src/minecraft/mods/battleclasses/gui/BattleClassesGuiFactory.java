package mods.battleclasses.gui;

import net.minecraft.client.gui.GuiScreen;
import mods.battlegear2.client.gui.BattlegearConfigGUI;
import mods.battlegear2.gui.BattlegearGuiFactory;

public class BattleClassesGuiFactory extends BattlegearGuiFactory {
	
	@Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return BattlegearConfigGUI.class;
    }
	
}
