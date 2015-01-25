package mods.battleclasses.gui.tab;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.BattleClassesAbstractTalent;
import mods.battleclasses.core.BattleClassesTalentMatrix;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.gui.BattleClassesGuiHandler;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonTalentNode;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonTalentReset;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonTalentTree;
import mods.battleclasses.packet.BattleClassesPacketGuiTabSwitch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class BattleClassesTabSpellbook extends BattleClassesAbstractTab {

	public static final ResourceLocation resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceSpellbook.png");
	
	public BattleClassesTabSpellbook(EntityPlayer entityPlayer, boolean isRemote) {
        super(entityPlayer, isRemote, new BattleClassesContainerEmpty(entityPlayer.inventory, !isRemote, entityPlayer));
        this.allowUserInput = true;
    }
	
    @Override
    public void initGui ()
    {
        super.initGui();
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
        //Drawing background
        this.mc.renderEngine.bindTexture(resource);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        
        
        
        ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);	
    	int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
    	ArrayList<BattleClassesAbstractAbilityActive> actionbarAbilities = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getActionbarAbilities();
    	if(actionbarAbilities.size() == 0) {
    		return;
    	}

    	int centerX = width/2;
    	int actionbarPosX = centerX - BattleClassesGuiHelper.getActionBarWidth(actionbarAbilities.size())/2;
        int actionbarPosY = this.guiTop - BattleClassesGuiHelper.ABILITY_ACTIONBAR_HEIGHT;
    	BattleClassesGuiHelper.INSTANCE.drawActionbarBackgroundCentered(centerX, actionbarPosY, actionbarAbilities.size());
    	
        for(int i = 0; i < actionbarAbilities.size(); ++i) {
        	BattleClassesGuiHelper.INSTANCE.drawAbilityIcon(actionbarPosX+3 + i*20, actionbarPosY+3, actionbarAbilities.get(i));
        }
	}
    
    public static void open(EntityPlayer player){
    	//send packet to open container on server
        //Battlegear.packetHandler.sendPacketToServer(new BattlegearGUIPacket(BattleClassesGUIHandler.talentsID).generatePacket());
    	BattleClassesMod.packetHandler.sendPacketToServer(new BattleClassesPacketGuiTabSwitch(BattleClassesGuiHandler.spellbookID).generatePacket());
    }

}