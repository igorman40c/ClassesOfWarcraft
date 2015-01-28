package mods.battleclasses.gui.controlls;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
import mods.battleclasses.gui.tab.BattleClassesTabTalents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiTabBarButtonSpellbook extends BattleClassesGuiTabBarButton {

	public GuiTabBarButtonSpellbook(int par1, int par2, int par3) {
		super(par1, par2, par3, "spellbook");
	}

	@Override
	public void openGui(Minecraft mc) {
		BattleClassesTabSpellbook.open(mc.thePlayer);
	}

	@Override
	public Class<? extends GuiScreen> getGUIClass() {
		return BattleClassesTabSpellbook.class;
	}
	
	@Override
	public boolean isAccessAble() {
		return BattleClassesUtils.getPlayerClassEnum(Minecraft.getMinecraft().thePlayer) != EnumBattleClassesPlayerClass.NONE ;
	}
}
