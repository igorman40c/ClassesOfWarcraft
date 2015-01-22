package mods.battleclasses.gui.controlls;

import mods.battleclasses.gui.tab.BattleClassesTabClassSelector;
import mods.battlegear2.client.gui.BattleEquipGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiTabBarButtonClassSelector extends BattleClassesGuiTabBarButton {
	
	public GuiTabBarButtonClassSelector(int par1, int par2, int par3) {
		super(par1, par2, par3, "Class Selector");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void openGui(Minecraft mc) {
		BattleClassesTabClassSelector.open(mc.thePlayer);
	}

	@Override
	public Class<? extends GuiScreen> getGUIClass() {
		return BattleClassesTabClassSelector.class;
	}
	
	@Override
	public String getIconName() {
		return "tab_class";
	}

}
