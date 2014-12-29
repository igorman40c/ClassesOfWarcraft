package mods.battleclasses.gui.tab;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battlegear2.client.gui.BattleEquipGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiTabBarButtonTalentSelector extends BattleClassesGuiTabBarButton {

	public GuiTabBarButtonTalentSelector(int par1, int par2, int par3) {
		super(par1, par2, par3, "Talents");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void openGui(Minecraft mc) {
		BattleClassesTabTalents.open(mc.thePlayer);
	}

	@Override
	protected Class<? extends GuiScreen> getGUIClass() {
		return BattleClassesTabTalents.class;
	}
	
	@Override
	public boolean isAccessAble() {
		return BattleClassesUtils.getPlayerClass(Minecraft.getMinecraft().thePlayer) != EnumBattleClassesPlayerClass.PlayerClass_NONE ;
	}
	
	@Override
	public String getIconName() {
		return "tab_talents";
	}

}
