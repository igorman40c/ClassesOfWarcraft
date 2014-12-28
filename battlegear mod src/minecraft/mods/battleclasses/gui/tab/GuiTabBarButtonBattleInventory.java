package mods.battleclasses.gui.tab;

import mods.battleclasses.client.BattleClassesInGameGUI;
import mods.battlegear2.client.gui.BattleEquipGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiTabBarButtonBattleInventory extends BattleClassesGuiTabBarButton {

	public GuiTabBarButtonBattleInventory(int par1, int par2, int par3) {
		super(par1, par2, par3, "Battle Equipment");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void openGui(Minecraft mc) {
		BattleClassesTabEquipment.open(mc.thePlayer);
	}

	@Override
	protected Class<? extends GuiScreen> getGUIClass() {
		return BattleClassesTabEquipment.class;
	}
	
	@Override
	public String getIconName() {
		return "tab_equipment";
	}
}