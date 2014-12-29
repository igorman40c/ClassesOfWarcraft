package mods.battleclasses.gui.tab;

import mods.battlegear2.client.gui.BattleEquipGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiTabBarButtonClassSelector extends BattleClassesGuiTabBarButton {
	
	public GuiTabBarButtonClassSelector(int par1, int par2, int par3) {
		super(par1, par2, par3, "Class Selector");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void openGui(Minecraft mc) {
		BattleClassesTabClassSelector.open(mc.thePlayer);
	}

	@Override
	protected Class<? extends GuiScreen> getGUIClass() {
		return BattleClassesTabClassSelector.class;
	}
	
	@Override
	public String getIconName() {
		return "tab_class";
	}

}
