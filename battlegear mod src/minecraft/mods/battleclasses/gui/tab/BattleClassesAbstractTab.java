package mods.battleclasses.gui.tab;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battleclasses.gui.BattleClassesGuiHandler;
import mods.battlegear2.Battlegear;
import mods.battlegear2.client.BattlegearClientEvents;
import mods.battlegear2.client.ClientProxy;
import mods.battlegear2.gui.BattlegearGUIHandeler;
import mods.battlegear2.gui.ContainerBattle;
import mods.battlegear2.packet.BattlegearGUIPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class BattleClassesAbstractTab extends InventoryEffectRenderer implements ITooltipDisplayGui {
	
	public ResourceLocation resource;
    public Class equipTab;
    public int guiHandlerID;
    
    public int title_position_X = 0;
    public int title_position_Y = 0;
    public int DEFAULT_GUI_WIDTH = 176;
    public int DEFAULT_GUI_HEIGHT = 166;
    public String displayTitle = "";
    
    public EntityPlayer ownerPlayer;
    
    /**
     * x size of the inventory window in pixels. Defined as float, passed as int
     */
    public float xSize_lo;

    /**
     * y size of the inventory window in pixels. Defined as float, passed as int.
     */
    public float ySize_lo;
    
    protected BattleClassesTabOverlayAttributes overlay;

    public BattleClassesAbstractTab(EntityPlayer entityPlayer, boolean isRemote, Container container) {
        //super(new ContainerBattle(entityPlayer.inventory, !isRemote, entityPlayer));
    	super(container);
    	this.ownerPlayer = entityPlayer; 
        this.allowUserInput = true;
        this.overlay = new BattleClassesTabOverlayAttributes();
        //Don't need this, however maybe we can add a stat later on. I will keep it comented out for now
        //entityPlayer.addStat(AchievementList.openInventory, 1);
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3){
        super.drawScreen(par1, par2, par3);
        this.xSize_lo = (float) par1;
        this.ySize_lo = (float) par2;
        if(this.shouldDisplayTooltip) {
        	this.drawHoveringText(this.toolTipTextLines, this.toolTipMousePosX, this.toolTipMousePosY, fontRendererObj);
        	this.shouldDisplayTooltip = false;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        BattleClassesClientEvents.onOpenGui(this.buttonList, guiLeft-28, guiTop);
		this.setTitlePosition(this.guiLeft + DEFAULT_GUI_WIDTH/2, this.guiTop + 8);
    }
    
    public void setTitlePosition(int x, int y) {
    	title_position_X = x;
    	title_position_Y = y;
    }
    
    public void drawTitle() {
    	this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, this.displayTitle, title_position_X, title_position_Y, 0xFFFFFF);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
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
