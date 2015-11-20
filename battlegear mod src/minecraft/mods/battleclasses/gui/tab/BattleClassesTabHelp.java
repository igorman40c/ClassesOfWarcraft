package mods.battleclasses.gui.tab;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.gui.BattleClassesGuiHandler;
import mods.battleclasses.packet.BattleClassesPacketGuiTabSwitch;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearGUIPacket;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class BattleClassesTabHelp extends BattleClassesAbstractTab {
	
	public static final ResourceLocation resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceHelp.png");
	public static final ResourceLocation dressroomResource = new ResourceLocation("battleclasses", "textures/gui/InterfaceDressroom.png");
	
	private static final boolean drawDressroom = true;

    public BattleClassesTabHelp(EntityPlayer entityPlayer, boolean isRemote) {
        super(entityPlayer, isRemote, new BattleClassesContainerEmpty(entityPlayer.inventory, !isRemote, entityPlayer));
        this.allowUserInput = true;
    }
    
    
    @Override
    public void initGui ()
    {
        super.initGui();
        //this.displayTitle = "Help";
    }

    /**
     * Draws the screen and all the components in it.
     */
    /*
    @Override
    public void drawScreen(int par1, int par2, float par3){
        super.drawScreen(par1, par2, par3);
    }
    */

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(this.drawDressroom) {
        	this.mc.renderEngine.bindTexture(dressroomResource);
        }
        else {
        	this.mc.renderEngine.bindTexture(resource);        	
        }
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        this.drawTitle();
        
        if(this.drawDressroom) {
        	int borderWidth = 3;
        	GuiInventory.func_147046_a((int) (this.guiLeft + this.DEFAULT_GUI_WIDTH/2F), (int) (this.guiTop + this.DEFAULT_GUI_HEIGHT - 16), 70, (float) (var5 + 51) - this.xSize_lo, (float) (var6 + 75 - 50) - this.ySize_lo, mc.thePlayer);
        }
    }
    
    public static void open(EntityPlayer player){
    	//send packet to open container on server
        //Battlegear.packetHandler.sendPacketToServer(new BattlegearGUIPacket(BattleClassesGUIHandler.helpID).generatePacket());
    	BattleClassesMod.packetHandler.sendPacketToServer(new BattleClassesPacketGuiTabSwitch(BattleClassesGuiHandler.helpID).generatePacket());

    }
}