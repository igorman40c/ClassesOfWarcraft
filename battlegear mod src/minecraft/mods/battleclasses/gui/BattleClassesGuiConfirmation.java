package mods.battleclasses.gui;

import java.util.Iterator;

import mods.battleclasses.gui.controlls.BattleClassesGuiButtonConfirmation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class BattleClassesGuiConfirmation extends GuiScreen {
	
	public static interface Handler{
       	public void confirmationDialogDidFinish(BattleClassesGuiConfirmation confirmationGuiScreen, boolean result);
    }
	
	public static final ResourceLocation resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceConfirmation.png");
	
	protected static final BattleClassesGuiButtonConfirmation buttonYes = new BattleClassesGuiButtonConfirmation(100, 0, 0, "bcgui.confirmation.yes", true);
	protected static final BattleClassesGuiButtonConfirmation buttonNo = new BattleClassesGuiButtonConfirmation(101, 0, 0, "bcgui.confirmation.no", false);
	
	protected String unlocalizedTitle = "";
	protected String unlocalizedMessage = "";
	
	public Handler handler = null;
	protected GuiScreen lastGuiScreen = null;
	
	public BattleClassesGuiConfirmation(String unlocalizedTitle, String unlocalizedMessage, boolean desctructiveYes, Handler handler, GuiScreen currentGuiScreen) {
		this.unlocalizedTitle = unlocalizedTitle;
		this.unlocalizedMessage = unlocalizedMessage;
		buttonYes.setDestrucive(desctructiveYes);
		this.handler = handler;
		this.lastGuiScreen = currentGuiScreen;
	}
	
	@Override
	public void initGui() {
		buttonYes.setPosition(this.width/2 - buttonYes.width - 2, this.height/2 + buttonYes.height/2 + 2);
		buttonNo.setPosition(this.width/2 + 2, this.height/2 + buttonNo.height/2 + 2);
		buttonYes.setConfirmationGui(this);
		buttonNo.setConfirmationGui(this);
		this.buttonList.add(buttonYes);
		this.buttonList.add(buttonNo);
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
		this.drawDefaultBackground();
        
		int stringX = this.width / 2;
		int stringY = this.height / 2;
				//k += this.fontRendererObj.FONT_HEIGHT
		//Drawing message
		this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal(this.unlocalizedMessage), stringX, stringY, 10526880);
		stringY -= this.fontRendererObj.FONT_HEIGHT * 1.5F;
		//Drawing title
		this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal(this.unlocalizedTitle), stringX, stringY, 16777215);
		
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
	
	public void userDidConfirm(boolean value) {
		if (this.handler!=null) {
			handler.confirmationDialogDidFinish(this, value);
		}
		if (this.lastGuiScreen != null){
			Minecraft.getMinecraft().displayGuiScreen(this.lastGuiScreen);
		}
	}
	
}
