package mods.battleclasses.gui.controlls;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.packet.BattleClassesPacketTalentNodeChosen;
import mods.battlegear2.Battlegear;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiButtonTalentTree extends BattleClassesGuiButton {
	
	public BattleClassesGuiButtonTalentTree(int id, int x, int y) {
		super(id, x, y, 54, 20, "talentTree");
		this.resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceTalent.png");
		//TODO
		//displayTooltip = true;
	}

	public BattleClassesTalentTree talentTree;

	public void setTalentTree(BattleClassesTalentTree parTalentTree) {
		talentTree = parTalentTree;
		this.displayString = talentTree.name;
		//TODO : Set Title(displayString), description here, etc...
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		boolean press = inWindow && !BattleClassesUtils.getPlayerTalentMatrix(mc.thePlayer).hasPointsSpentAlready();
		if (press) {
			FMLProxyPacket p = new BattleClassesPacketTalentNodeChosen(mc.thePlayer,
					BattleClassesPacketTalentNodeChosen.TALENT_TREE_BUTTON_ID_OFFSET + this.talentTree.getIndexOfTree()).generatePacket();
			Battlegear.packetHandler.sendPacketToServer(p);
		}
		return press;
	}

}
