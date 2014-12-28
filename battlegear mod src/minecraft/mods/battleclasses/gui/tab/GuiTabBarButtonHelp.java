package mods.battleclasses.gui.tab;

import mods.battlegear2.client.gui.BattleEquipGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiTabBarButtonHelp extends BattleClassesGuiTabBarButton {
	
	public GuiTabBarButtonHelp(int par1, int par2, int par3) {
		super(par1, par2, par3, "Help");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void openGui(Minecraft mc) {
		BattleClassesTabHelp.open(mc.thePlayer);
	}

	@Override
	protected Class<? extends GuiScreen> getGUIClass() {
		return BattleClassesTabHelp.class;
	}
	
	@Override
	public String getIconName() {
		return "tab_help";
	}
}
