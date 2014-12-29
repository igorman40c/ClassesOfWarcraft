package mods.battleclasses.gui.tab;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battleclasses.client.BattleClassesInGameGUI;
import mods.battleclasses.gui.BattleClassesGUIHandler;
import mods.battlegear2.Battlegear;
import mods.battlegear2.client.gui.BattleEquipGUI;
import mods.battlegear2.gui.BattlegearGUIHandeler;
import mods.battlegear2.packet.BattlegearGUIPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class GuiTabBarButtonVanillaInventory extends BattleClassesGuiTabBarButton {
	
	public GuiTabBarButtonVanillaInventory(int par1, int par2, int par3) {
		super(par1, par2, par3, "Inventory");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void openGui(Minecraft mc) {
		BattleClassesUtils.Log("GuiTabBarButtonVanillaInventory openGui", LogType.GUI);
		BattleClassesInGameGUI.previousGui = mc.currentScreen.getClass();
        mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
		GuiInventory guiInventory = new GuiInventory(mc.thePlayer);
		mc.displayGuiScreen(guiInventory);
	}

	@Override
	protected Class<? extends GuiScreen> getGUIClass() {
		return BattleClassesGuiInventory.class;
	}
	
	@Override
	public String getIconName() {
		return "tab_inventory";
	}
}
