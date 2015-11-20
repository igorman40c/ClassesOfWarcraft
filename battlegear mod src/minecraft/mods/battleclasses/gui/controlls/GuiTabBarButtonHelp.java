package mods.battleclasses.gui.controlls;

import mods.battleclasses.gui.tab.BattleClassesTabHelp;
import mods.battlegear2.client.gui.BattleEquipGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiTabBarButtonHelp extends BattleClassesGuiTabBarButton {
	
	public GuiTabBarButtonHelp(int par1, int par2, int par3) {
		super(par1, par2, par3, "info");
		// TODO Auto-generated constructor stub
	}
	
	public GuiTabBarButtonHelp(int par1, int par2, int par3, boolean parHorizontal) {
		super(par1, par2, par3, "info", parHorizontal);
	}

	@Override
	public void openGui(Minecraft mc) {
		BattleClassesTabHelp.open(mc.thePlayer);
	}

	@Override
	public Class<? extends GuiScreen> getGUIClass() {
		return BattleClassesTabHelp.class;
	}
}
