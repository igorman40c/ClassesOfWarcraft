package mods.battleclasses.gui.tab;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.BattleClassesGuiHandler;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonClassSelector;
import mods.battleclasses.packet.BattleClassesPacketGuiTabSwitch;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearGUIPacket;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class BattleClassesTabClassSelector extends BattleClassesAbstractTab  {
	
	public static final ResourceLocation resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceClassSelector.png");
	
	public static List<BattleClassesGuiButtonClassSelector> classButtonList = new ArrayList<BattleClassesGuiButtonClassSelector>();
	static {
		classButtonList.add(new BattleClassesGuiButtonClassSelector(0, resource, EnumBattleClassesPlayerClass.MAGE));
		classButtonList.add(new BattleClassesGuiButtonClassSelector(1, resource, EnumBattleClassesPlayerClass.PRIEST));
		classButtonList.add(new BattleClassesGuiButtonClassSelector(2, resource, EnumBattleClassesPlayerClass.WARLOCK));
		classButtonList.add(new BattleClassesGuiButtonClassSelector(3, resource, EnumBattleClassesPlayerClass.ROGUE));
		classButtonList.add(new BattleClassesGuiButtonClassSelector(4, resource, EnumBattleClassesPlayerClass.HUNTER));
		classButtonList.add(new BattleClassesGuiButtonClassSelector(5, resource, EnumBattleClassesPlayerClass.PALADIN));
		classButtonList.add(new BattleClassesGuiButtonClassSelector(6, resource, EnumBattleClassesPlayerClass.WARRIOR));
	}

    public BattleClassesTabClassSelector(EntityPlayer entityPlayer, boolean isRemote) {
        super(entityPlayer, isRemote, new BattleClassesContainerEmpty(entityPlayer.inventory, !isRemote, entityPlayer));
        this.allowUserInput = true;
    }
    
   @Override
    public void initGui ()
    {
        super.initGui();
        this.displayTitle = StatCollector.translateToLocal("bctab.classselector.name");
        //Init Buttons
		for (BattleClassesGuiButtonClassSelector button : BattleClassesTabClassSelector.classButtonList) {
			this.buttonList.add(button);
		}
		this.updateClassButtonPositions();
    }
   
   	public void updateClassButtonPositions() {
		int guiCenterV = 166/2;
		int buttonGapV = 20;
		int buttonGapH = 10;
		int buttonSize = 32;
		//First line
		int firstLineX = this.guiLeft + guiCenterV - buttonGapV/2 - buttonSize;
		int firstLineY = this.guiTop + 20 + buttonGapH;
		BattleClassesTabClassSelector.classButtonList.get(0).setPosition(firstLineX, firstLineY);
		BattleClassesTabClassSelector.classButtonList.get(1).setPosition(firstLineX + buttonSize*1 + buttonGapV*1, firstLineY);
		//Second line
		int secondLineX = this.guiLeft + buttonGapV*1;
		int secondLineY = firstLineY + buttonGapH + buttonSize;
		BattleClassesTabClassSelector.classButtonList.get(2).setPosition(secondLineX, secondLineY);
		BattleClassesTabClassSelector.classButtonList.get(3).setPosition(secondLineX + buttonSize*1 + buttonGapV*1, secondLineY);
		BattleClassesTabClassSelector.classButtonList.get(4).setPosition(secondLineX + buttonSize*2 + buttonGapV*2, secondLineY);
		//Third line
		int thirdLineX = firstLineX;
		int thirdLineY = secondLineY + buttonGapH + buttonSize;
		BattleClassesTabClassSelector.classButtonList.get(5).setPosition(thirdLineX, thirdLineY);
		BattleClassesTabClassSelector.classButtonList.get(6).setPosition(thirdLineX + buttonSize*1 + buttonGapV*1, thirdLineY);
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
        this.mc.renderEngine.bindTexture(resource);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        this.drawTitle();
    }
    
    public static void open(EntityPlayer player){
    	//send packet to open container on server
    	BattleClassesMod.packetHandler.sendPacketToServer(new BattleClassesPacketGuiTabSwitch(BattleClassesGuiHandler.classSelectorID).generatePacket());
        //Battlegear.packetHandler.sendPacketToServer(new BattlegearGUIPacket(BattleClassesGUIHandler.classSelectorID).generatePacket());
    }
    
}
