package mods.battleclasses.gui.controlls;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.packet.BattleClassesPacketTalentNodeChosen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiButtonTalentGain extends BattleClassesGuiButton {

	public BattleClassesGuiButtonTalentGain(int id, int x, int y) {
		super(id, x, y, 54, 20, "Gain");
		this.displayString = "Gain";
		this.resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceTalent.png");
		this.setOrigin(0, 196);
	}
	
	@Override
	public boolean shouldBeDisabled() {
		return true;
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		boolean press = inWindow;
		if (press) {
			/*
			FMLProxyPacket p = new BattleClassesPacketTalentNodeChosen(mc.thePlayer, BattleClassesPacketTalentNodeChosen.RESET_TALENTS_ID).generatePacket();
			BattleClassesMod.packetHandler.sendPacketToServer(p);
			*/
		}
		return press;
	}
}