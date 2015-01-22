package mods.battleclasses.gui.tab;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;

public interface ITooltipDisplayGui {
	public void displayTooltip(List textLines, int mousePosX, int mousePosY);
}
