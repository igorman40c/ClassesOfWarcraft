package mods.battleclasses.gui.controlls;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import scala.reflect.internal.Trees.This;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battleclasses.gui.tab.ITooltipDisplayGui;
import mods.battlegear2.client.gui.BattleEquipGUI;
import mods.battlegear2.client.gui.controls.GuiPlaceableButton;

public abstract class BattleClassesGuiTabBarButton extends BattleClassesGuiButton {
	
	public IIcon tabButtonIcon;
	
	public boolean horizontal = true;
	
    protected static final ResourceLocation barButtonTexture = new ResourceLocation("battleclasses", "textures/gui/InterfaceOverlay.png");
    
	public BattleClassesGuiTabBarButton(int par1, int par2, int par3,
			String name) {
		this(par1, par2, par3, name, true);
	}

	public BattleClassesGuiTabBarButton(int par1, int par2, int par3,
			String name, boolean parHorizontal) {
		super(par1, par2, par3, name);
		this.tooltipDescription = name;
		this.horizontal = parHorizontal;
		this.setContentPositionAndSize();
		this.displayTooltip = true;
	}
	
	public static final int BAR_BUTTON_GAP = 1;
	
	public static final int BAR_BUTTON_SIZE_W_H = 32;
	public static final int BAR_BUTTON_SIZE_H_H = 28;
	public static final int BAR_BUTTON_SIZE_W_V = 28;
	public static final int BAR_BUTTON_SIZE_H_V = 31;
	
	protected int origin_X = 0;
	protected int origin_Y = 0;
	
	public void setContentPositionAndSize() {
		this.zLevel = 0;
		if(this.horizontal) {
			origin_X = 0;
			origin_Y = 10;
			
			this.width = BAR_BUTTON_SIZE_W_H;
			this.height = BAR_BUTTON_SIZE_H_H;
		}
		else {
			origin_X = 40;
			origin_Y = 163;
			
			this.width = BAR_BUTTON_SIZE_W_V;
			this.height = BAR_BUTTON_SIZE_H_V;
		}
	}
	
	public boolean isAccessAble() {
		return true;
	}
	
	private boolean isInGui(GuiScreen currentScreen) {
		return currentScreen.getClass()==getGUIClass();
	}

	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		if (inWindow && !isInGui(mc.currentScreen) && isAccessAble()) {
			BattleClassesClientEvents.lastUsedTabButton = this;
			this.openGui(mc);
		}
		return inWindow;
	}	
    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int currentMousePosX, int currentMousePosY)
    {
        if (this.visible)
        {	
            FontRenderer fontrenderer = mc.fontRenderer;
            if(isAccessAble()) {
            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            else {
            	GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            }
            
            mc.getTextureManager().bindTexture(barButtonTexture);
            
            //InWindow
            this.field_146123_n = currentMousePosX >= this.xPosition && currentMousePosY >= this.yPosition && currentMousePosX < this.xPosition + this.width && currentMousePosY < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            
            //Rendering Bar Button Texture
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int selectedOffset = (this.isSelected()) ? this.width : 0;
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.origin_X + selectedOffset, this.origin_Y, this.width, this.height);
            
            //Rendering Tab Icon
            int iconOffsetX = (this.horizontal) ? 7 : 6;
            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
            this.drawTexturedModelRectFromIcon(this.xPosition  + iconOffsetX, this.yPosition + this.height - 16 - 6, tabButtonIcon, 16, 16);
                        
            this.renderHoveringText(currentMousePosX, currentMousePosY);
        }
    }    

	public String getIconRegisterPath() {
		return ( "battleclasses:sharedicons/gui/"+this.getIconName() );
	}
    
    public boolean isSelected() {
    	Minecraft mc = Minecraft.getMinecraft();
    	return (mc.currentScreen.getClass() == this.getGUIClass());
    }

	public abstract Class<? extends GuiScreen> getGUIClass();

	public abstract void openGui(Minecraft mc);
	
	public abstract String getIconName();

}
