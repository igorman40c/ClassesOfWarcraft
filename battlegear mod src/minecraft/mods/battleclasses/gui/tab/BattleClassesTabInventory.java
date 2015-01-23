package mods.battleclasses.gui.tab;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battlegear2.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BattleClassesTabInventory extends GuiInventory implements ITooltipDisplayGui {

    protected BattleClassesTabOverlayAttributes overlay;
	
	public BattleClassesTabInventory(EntityPlayer par1EntityPlayer) {
		super(par1EntityPlayer);
		this.overlay = new BattleClassesTabOverlayAttributes();
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public void initGui() {
        super.initGui();
        BattleClassesClientEvents.onOpenGui(this.buttonList, guiLeft-28, guiTop);
        /*
        if(ClientProxy.tconstructEnabled){
            this.buttonList.clear();
            try{
                if(equipTab==null){
                    equipTab = Class.forName("mods.battlegear2.client.gui.controls.EquipGearTab");
                }
                ClientProxy.updateTab.invoke(null, guiLeft, guiTop, equipTab);
                ClientProxy.addTabs.invoke(null, this.buttonList);
            }catch(Exception e){
                ClientProxy.tconstructEnabled = false;
            }
        }
        */
		//this.setTitlePosition(this.guiLeft + DEFAULT_GUI_WIDTH/2, this.guiTop + 8);
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3){
        super.drawScreen(par1, par2, par3);
        if(this.shouldDisplayTooltip) {
        	this.drawHoveringText(this.toolTipTextLines, this.toolTipMousePosX, this.toolTipMousePosY, fontRendererObj);
        	this.shouldDisplayTooltip = false;
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.overlay.drawAttributesDisplayWindowBackground(this.guiLeft, this.guiTop, this.xSize, this.zLevel);
        this.overlay.drawScreen(par2, par3, par1);
    }
    
    protected List toolTipTextLines;
    protected int toolTipMousePosX;
    protected int toolTipMousePosY;
    protected boolean shouldDisplayTooltip = false;
    public void displayTooltip(List textLines, int mousePosX, int mousePosY) {
    	this.toolTipTextLines = textLines;
    	this.toolTipMousePosX = mousePosX;
    	this.toolTipMousePosY = mousePosY;
    	this.shouldDisplayTooltip = true;
    }

}
