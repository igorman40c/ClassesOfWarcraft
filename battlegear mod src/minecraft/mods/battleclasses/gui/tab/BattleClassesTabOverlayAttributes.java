package mods.battleclasses.gui.tab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.enumhelper.EnumBattleClassesAttributeType;
import mods.battleclasses.gui.controlls.BattleClassesGuiAttributeDisplayNode;
import mods.battleclasses.gui.controlls.BattleClassesGuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class BattleClassesTabOverlayAttributes extends GuiScreen {
	
    public static final ResourceLocation resourceAttributes = new ResourceLocation("battleclasses", "textures/gui/InterfaceAttributes.png");
	
	public BattleClassesTabOverlayAttributes() {
		super();
		initAttributesDisplay();
	}
	
	public void initAttributesDisplay() {
		this.mc = Minecraft.getMinecraft();
		BattleClassesPlayerClass playerClassObj = BattleClassesUtils.getPlayerClassObj(mc.thePlayer);
		if(playerClassObj != null) {
			this.attributesToDisplay.clear();
			this.attributesToDisplay.addAll(playerClassObj.getDefaultAttributesToDisplay());
			this.attributesToDisplay.addAll(playerClassObj.getPrimaryAttributesToDisplay());
			this.attributesToDisplay.addAll(playerClassObj.getSecondaryAttributesToDisplay());
		}
		
		int i = 0;
		this.attributeDisplayNodes.clear();
		this.buttonList.clear();
		for(EnumBattleClassesAttributeType attributeType : this.attributesToDisplay) {
			BattleClassesGuiAttributeDisplayNode attributeDisplay = new BattleClassesGuiAttributeDisplayNode(i++, 5, 5 + i*10, attributeType);
			this.attributeDisplayNodes.add(attributeDisplay);
			this.buttonList.add(attributeDisplay);
		}
	}
	
	
	public ArrayList<EnumBattleClassesAttributeType> attributesToDisplay = new ArrayList<EnumBattleClassesAttributeType>();
	public ArrayList<BattleClassesGuiAttributeDisplayNode> attributeDisplayNodes = new ArrayList<BattleClassesGuiAttributeDisplayNode>();
	
	public void drawScreen(int mouseX, int mouseY, float p_73863_3_)
    {
        super.drawScreen(mouseX, mouseY, p_73863_3_);
    }
	
	public static final int WINDOW_HEADER_FOOTER_HEIGHT = 6;
	public static final int GAP_BETWEEN_WINDOWS = 5;
	public static final int GAP_BETWEEN_ELEMENTS = 2;
	public void drawAttributesDisplayWindowBackground(int guiLeft, int guiTop, int guiWidth, float parZLevel) {
		
		Minecraft mc = Minecraft.getMinecraft();
		int windowPositionX = guiLeft + guiWidth + GAP_BETWEEN_WINDOWS;
		int windowPositionY = guiTop;
		int windowWidth = 71;
		int windowHeight = WINDOW_HEADER_FOOTER_HEIGHT;
		this.zLevel = 0;
		
       
        GL11.glPushMatrix();
        //GL11.glEnable(GL11.GL_BLEND);
        //OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(resourceAttributes);
		
		//Calculating window height
		int i = 0;
		for(BattleClassesGuiAttributeDisplayNode attributeDisplayNode : attributeDisplayNodes) {
			windowHeight += attributeDisplayNode.height;
			windowHeight += (i>0) ? GAP_BETWEEN_ELEMENTS : 0;
			++i;
		}
		
		//Drawing window background header and main
		this.drawTexturedModalRect(windowPositionX, windowPositionY, 0, 0, windowWidth, windowHeight);
		//Drawing window background footer
		this.drawTexturedModalRect(windowPositionX, windowPositionY+windowHeight, 0, 256 - WINDOW_HEADER_FOOTER_HEIGHT, windowWidth, WINDOW_HEADER_FOOTER_HEIGHT);
		
		//Setting attribute display nodes offset and position
		for (int k = 0; k < this.buttonList.size(); ++k)
        {
			if(this.buttonList.get(k) instanceof BattleClassesGuiButton) {
				((BattleClassesGuiButton)this.buttonList.get(k)).setPositionOffset(windowPositionX, windowPositionY);
				((BattleClassesGuiButton)this.buttonList.get(k)).setPosition(WINDOW_HEADER_FOOTER_HEIGHT, WINDOW_HEADER_FOOTER_HEIGHT + k*GAP_BETWEEN_ELEMENTS + k*BattleClassesGuiAttributeDisplayNode.GUI_ELEMENT_HEIGHT);
			}
        }
		
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
	}
}
