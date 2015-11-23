package mods.battleclasses.gui.controlls;

import java.util.List;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.packet.BattleClassesPacketTalentPointGainRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class BattleClassesGuiButtonTalentGain extends BattleClassesGuiButton {

	public BattleClassesGuiButtonTalentGain(int id, int x, int y) {
		super(id, x, y, 54, 20, "");
		this.setDisplayString(StatCollector.translateToLocal("bcgui.talent.gain.title"));
		this.resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceTalent.png");
		this.setOrigin(0, 196);
		this.showHoveringText = true;
	}
	/*
	@Override
	public boolean shouldBeDisabled() {
		return true;
	}
	*/
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		boolean press = inWindow;
		if (press) {
			FMLProxyPacket p = new BattleClassesPacketTalentPointGainRequest(mc.thePlayer).generatePacket();
			BattleClassesMod.packetHandler.sendPacketToServer(p);			
		}
		return press;
	}
	
	@Override
	public boolean shouldBeDisabled() {
		Minecraft mc = Minecraft.getMinecraft();
		return !BattleClassesUtils.getPlayerTalentMatrix(mc.thePlayer).canGainAdditionalTalentPoints();
	}
	
	@Override
	public boolean shouldShowHoweringText() {
		return !shouldBeDisabled();
	}
	
	@Override
	public List<String> getTooltipText() {
		Minecraft mc = Minecraft.getMinecraft();
		
		String unlocalizedMessage = "bcgui.talent.gain.message";
		String gainCostMessage = StatCollector.translateToLocal(unlocalizedMessage);
		String gainCostValue = "" + BattleClassesUtils.getPlayerTalentMatrix(mc.thePlayer).getGainTalentPointExperienceCost();
		gainCostMessage = gainCostMessage.replace("%1$s", gainCostValue);
		
		boolean hasExperienceCost = BattleClassesUtils.getPlayerTalentMatrix(mc.thePlayer).hasExperienceToGainTalentPoint(mc.thePlayer);
		
		List<String> text = BattleClassesGuiHelper.createHoveringText();
		BattleClassesGuiHelper.addParagraphWithColor(text, gainCostMessage, BattleClassesGuiHelper.getHoveringTextAvailabilityColor(hasExperienceCost));
    	text = BattleClassesGuiHelper.formatHoveringTextWidth(text);
		return text;
	}
}