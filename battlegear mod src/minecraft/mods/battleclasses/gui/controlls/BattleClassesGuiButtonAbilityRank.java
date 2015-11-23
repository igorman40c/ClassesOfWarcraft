package mods.battleclasses.gui.controlls;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
import mods.battleclasses.packet.BattleClassesPacketAbilityRankUpRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

public class BattleClassesGuiButtonAbilityRank extends BattleClassesGuiButton {

	public BattleClassesAbstractAbilityActive ability;
	public BattleClassesGuiButtonAbilityRank(int id, BattleClassesAbstractAbilityActive parAbility) {
		super(id, 0, 0, 40, 18, "");
		this.trunctationMargins = 4F;
		this.showHoveringText = true;
		this.ability = parAbility;
		this.setOrigin(36, 202);
		this.resource = BattleClassesTabSpellbook.resource;
		this.updateDisplayTitle();
	}
	
	public void updateDisplayTitle() {
		String rankString = "";
		int abilityRank = this.ability.getCurrentRank();
		if(abilityRank == 0) {
			rankString = StatCollector.translateToLocal("bcgui.ability_rank.zero");
		}
		else {
			rankString = StatCollector.translateToLocal("bcgui.ability_rank");
			String rankNumber = "" + abilityRank;
			rankString = rankString.replace("%1$s", rankNumber);
		}
		
		this.setDisplayString(rankString);
	}

	public boolean shouldBeDisabled() {
		return !this.ability.canBeRankedUpFurther();
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		boolean press = inWindow;
		if (press) {
			FMLProxyPacket p = new BattleClassesPacketAbilityRankUpRequest(mc.thePlayer, this.ability.getAbilityID()).generatePacket();
			BattleClassesMod.packetHandler.sendPacketToServer(p);
		}
		return press;
	}
	
	@Override
	public boolean shouldShowHoweringText() {
		return !this.shouldBeDisabled();
	}
	
	@Override
	
	public List<String> getTooltipText() {
		Minecraft mc = Minecraft.getMinecraft();
		String unlocalizedMessage = (this.ability.getCurrentRank() == 0) ? "bcgui.ability_rank.learn.cost" : "bcgui.ability_rank.rank_up.cost";
		String rankUpCostMessage = StatCollector.translateToLocal(unlocalizedMessage);
		String nextRankCostValue = "" + BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getRankUpExperienceCost(this.ability);
		rankUpCostMessage = rankUpCostMessage.replace("%1$s", nextRankCostValue);
		boolean hasExperienceCost = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).hasExperienceToRankUpAbility(mc.thePlayer, this.ability);
		
		List<String> text = BattleClassesGuiHelper.createHoveringText();
		BattleClassesGuiHelper.addParagraphWithColor(text, rankUpCostMessage, BattleClassesGuiHelper.getHoveringTextAvailabilityColor(hasExperienceCost));
    	text = BattleClassesGuiHelper.formatHoveringTextWidth(text);
		return text;
	}
	
 
}
