package mods.battleclasses.gui.controlls;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.gui.BattleClassesGuiConfirmation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

public class BattleClassesGuiButtonConfirmation extends BattleClassesGuiButton {
	
	protected boolean destructive = false;
	protected boolean isYesButton = false;
	protected int defaultOriginU = 0;
	protected BattleClassesGuiConfirmation confirmationGui = null;
	
	public BattleClassesGuiButtonConfirmation(int id, int x, int y, String name, boolean isYes) {
		super(id, x, y, name);
		this.resource = BattleClassesGuiConfirmation.resource;
		this.setSize(98, 20);
		this.setOrigin(0, 196);
		this.setDisplayString(StatCollector.translateToLocal(name));
		this.isYesButton = isYes;
	}
	
	public void setConfirmationGui(BattleClassesGuiConfirmation confirmationGui) {
		this.confirmationGui = confirmationGui;
	}
	
	public void setDestrucive(boolean destructive) {
		this.destructive = destructive;
		this.origin_u = (destructive) ? this.width : this.defaultOriginU;
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		boolean press = inWindow;
		if (press) {
			this.confirmationGui.userDidConfirm(this.isYesButton);
		}
		return press;
	}
	
}
