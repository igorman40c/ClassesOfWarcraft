package mods.battleclasses.gui.tab;

import mods.battlegear2.client.gui.BattleEquipGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiTabBarButtonConfig extends BattleClassesGuiTabBarButton {

	public GuiTabBarButtonConfig(int par1, int par2, int par3) {
		super(par1, par2, par3, "Battle Classes Mod Settings");
		// TODO Auto-generated constructor stub
	}

	public GuiTabBarButtonConfig(int par1, int par2, int par3, boolean parHorizontal) {
		super(par1, par2, par3, "Battle Classes Mod Settings", parHorizontal);
	}

	@Override
	protected void openGui(Minecraft mc) {
		BattleClassesTabConfig.open(mc.thePlayer);
	}

	@Override
	protected Class<? extends GuiScreen> getGUIClass() {
		return BattleClassesTabConfig.class;
	}

	@Override
	public String getIconName() {
		return "tab_config";
	}

}
