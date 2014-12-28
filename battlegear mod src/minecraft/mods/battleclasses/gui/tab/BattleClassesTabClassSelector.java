package mods.battleclasses.gui.tab;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.BattleClassesGUIHandler;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonClassSelector;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearGUIPacket;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

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
        this.displayTitle = "Class Selector";
        //Init Buttons
		for (BattleClassesGuiButtonClassSelector button : BattleClassesTabClassSelector.classButtonList) {
			this.buttonList.add(button);
			button.setOrigin(0, 190);
			button.setSize(22, 22);
		}
		BattleClassesTabClassSelector.classButtonList.get(0).setPosition(this.guiLeft + 77,this.guiTop + 28);
		BattleClassesTabClassSelector.classButtonList.get(1).setPosition(this.guiLeft + 113,this.guiTop + 46);
		BattleClassesTabClassSelector.classButtonList.get(2).setPosition(this.guiLeft + 122,this.guiTop + 85);
		BattleClassesTabClassSelector.classButtonList.get(3).setPosition(this.guiLeft + 97,this.guiTop + 116);
		BattleClassesTabClassSelector.classButtonList.get(4).setPosition(this.guiLeft + 56,this.guiTop + 116);
		BattleClassesTabClassSelector.classButtonList.get(5).setPosition(this.guiLeft + 31,this.guiTop + 85);
		BattleClassesTabClassSelector.classButtonList.get(6).setPosition(this.guiLeft + 41,this.guiTop + 46);
		
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
        Battlegear.packetHandler.sendPacketToServer(new BattlegearGUIPacket(BattleClassesGUIHandler.classSelectorID).generatePacket());
    }
    
    public static String getClassDescription(EnumBattleClassesPlayerClass playerClass) {
    	switch (playerClass)  {
		case HUNTER:
			return "Hunter";
		case MAGE:
			return "Mage";
		case PlayerClass_NONE:
			return "No Class";
		case PALADIN:
			return "Paladin";
		case PRIEST:
			return "Priest";
		case ROGUE:
			return "Rogue";
		case WARLOCK:
			return "Warlock";
		case WARRIOR:
			return "Warrior";
		default:
			return "Unkown Class Description";
    		
    	}
    }

}
