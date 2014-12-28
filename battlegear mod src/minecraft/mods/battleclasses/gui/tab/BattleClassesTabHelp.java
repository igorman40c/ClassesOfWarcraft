package mods.battleclasses.gui.tab;

import mods.battleclasses.gui.BattleClassesGUIHandler;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearGUIPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class BattleClassesTabHelp extends BattleClassesAbstractTab {
	
	public static final ResourceLocation resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceHelp.png");

    public BattleClassesTabHelp(EntityPlayer entityPlayer, boolean isRemote) {
        super(entityPlayer, isRemote, new BattleClassesContainerEmpty(entityPlayer.inventory, !isRemote, entityPlayer));
        this.allowUserInput = true;
    }
    
    
    @Override
    public void initGui ()
    {
        super.initGui();
        this.displayTitle = "Help";
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3){
        super.drawScreen(par1, par2, par3);
        this.xSize_lo = (float) par1;
        this.ySize_lo = (float) par2;
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(resource);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        this.drawTitle();
    }
    
    public static void open(EntityPlayer player){
    	//send packet to open container on server
        Battlegear.packetHandler.sendPacketToServer(new BattlegearGUIPacket(BattleClassesGUIHandler.helpID).generatePacket());
    }
}