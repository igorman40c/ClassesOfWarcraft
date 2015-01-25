package mods.battleclasses.gui.tab;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.BattleClassesAbstractTalent;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battleclasses.core.BattleClassesTalentMatrix;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.gui.BattleClassesGuiHandler;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonAbilityNode;
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
	
	public static ArrayList<BattleClassesGuiButtonAbilityNode> actionbarAbilityNodes = new ArrayList<BattleClassesGuiButtonAbilityNode>();
	public static ArrayList<BattleClassesGuiButtonAbilityNode> spellbookAbilityNodes = new ArrayList<BattleClassesGuiButtonAbilityNode>();
	
    @Override
    public void initGui()
    {
        super.initGui();
        this.initActionbarAbilityNodes();
        this.initSpellbookAbilityNodes();
    }
    
    public void initActionbarAbilityNodes() {
    	BattleClassesSpellBook spellbook = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer);
    	for(BattleClassesGuiButtonAbilityNode actionbarAbilityNode : actionbarAbilityNodes) {
    		if(this.buttonList.contains(actionbarAbilityNode)) {
    			this.buttonList.remove(actionbarAbilityNode);
    		}
    	}
    	actionbarAbilityNodes.clear();
    	if(spellbook != null) {
    		for(BattleClassesAbstractAbilityActive actionbarAbility : spellbook.getActionbarAbilities()) {
    			BattleClassesGuiButtonAbilityNode actionbarAbilityNode = new BattleClassesGuiButtonAbilityNode(actionbarAbility);
        		actionbarAbilityNodes.add(actionbarAbilityNode);
    			this.buttonList.add(actionbarAbilityNode);
        	}
    	}
    }
    
    public void initSpellbookAbilityNodes() {
    	BattleClassesSpellBook spellbook = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer);
    	for(BattleClassesGuiButtonAbilityNode spellbookAbilityNode : spellbookAbilityNodes) {
    		if(this.buttonList.contains(spellbookAbilityNode)) {
    			this.buttonList.remove(spellbookAbilityNode);
    		}
    	}
    	spellbookAbilityNodes.clear();
    	if(spellbook != null) {
    		for(BattleClassesAbstractAbilityActive activeAbility : spellbook.getActionbarAbilities()) {
    			BattleClassesGuiButtonAbilityNode spellbookAbilityNode = new BattleClassesGuiButtonAbilityNode(activeAbility);
    			spellbookAbilityNodes.add(spellbookAbilityNode);
    			this.buttonList.add(spellbookAbilityNode);
        	}
    	}
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
    protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY) {
    	super.drawGuiContainerBackgroundLayer(par1, mouseX, mouseY);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //Drawing background
        this.mc.renderEngine.bindTexture(resource);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        
        this.drawActionbar();
        
        if(this.tempMovingNode != null) {
        	this.tempMovingNode.setPosition(mouseX-tempMovingNode.width/2, mouseY-tempMovingNode.height/2);
        }
	}
    
    public static void open(EntityPlayer player){
    	//send packet to open container on server
        //Battlegear.packetHandler.sendPacketToServer(new BattlegearGUIPacket(BattleClassesGUIHandler.talentsID).generatePacket());
    	BattleClassesMod.packetHandler.sendPacketToServer(new BattleClassesPacketGuiTabSwitch(BattleClassesGuiHandler.spellbookID).generatePacket());
    }
    
    public void drawActionbar() {
    	int centerX = getActionbarCenterX();
    	int actionbarPosX = getActionbarPosX();
        int actionbarPosY = getActionbarPosY();
    	BattleClassesGuiHelper.INSTANCE.drawActionbarBackgroundCentered(centerX, actionbarPosY, actionbarAbilityNodes.size());
    	//Refresh actionbarAbilityNode positions
    	for(int i = 0; i < actionbarAbilityNodes.size(); ++i) {
    		actionbarAbilityNodes.get(i).setPosition(actionbarPosX+3 + i*20, actionbarPosY+3);
        }
    }

    public int getActionbarPosX() {
    	return getActionbarCenterX() - BattleClassesGuiHelper.getActionBarWidth(actionbarAbilityNodes.size())/2;
    }
    
    public int getActionbarCenterX() {
    	ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);	
    	int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        return width/2;
    }
    
    public int getActionbarPosY() {
    	return this.guiTop - BattleClassesGuiHelper.ABILITY_ACTIONBAR_HEIGHT;
    }
    
    public boolean isInsideActionbar(int x, int y) {
    	return x > getActionbarPosX() && x < (getActionbarPosX()+ BattleClassesGuiHelper.getActionBarWidth(actionbarAbilityNodes.size())) &&
    			y > getActionbarPosY() && y < (getActionbarPosY() + BattleClassesGuiHelper.ABILITY_ACTIONBAR_HEIGHT);
    }
    
    
    //--------------------------- AbilityNode drag & drop logic --------------------------
    /**
     * Called when the mouse is clicked.
     */
    public void mouseClickedDelegate(int mouseX, int mouseY)
    {
        if(selectedNode != null) {
        	if(actionbarAbilityNodes.contains(selectedNode)) {
        		//Replace abilityNodes on actionbar
        		for(BattleClassesGuiButtonAbilityNode actionbarAbilityNode : actionbarAbilityNodes) {
            		if(actionbarAbilityNode.isMouseOver(mouseX, mouseY)) {
            			if(actionbarAbilityNode.ability != selectedNode.ability) {
            				BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).repleaceAbilitiesOnActionbar(actionbarAbilityNode.ability, selectedNode.ability); 
            			}
            			this.onAbilityNodeReleased();
            			return;
            		}
            	}
        		//Remove abilityNodes from actionbar
        		if(!isInsideActionbar(mouseX, mouseY)) {
        			BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).removeAbilityFromActionbar(selectedNode.ability);
        			this.onAbilityNodeReleased();
        			return;
        		}
        	}
        	else if(spellbookAbilityNodes.contains(selectedNode)) {
        		if(isInsideActionbar(mouseX, mouseY)) {
        			
        			this.onAbilityNodeReleased();
        			return;
        		}
        	}
        }
    }
    
    public BattleClassesGuiButtonAbilityNode selectedNode;
    public BattleClassesGuiButtonAbilityNode tempMovingNode;
    /**
     * Called from BattleClassesGuiButtonAbilityNode
     * @param abilityNode
     */
    public void onAbilityNodeSelected(BattleClassesGuiButtonAbilityNode abilityNode) {
    	this.selectedNode = abilityNode;
    	tempMovingNode = new BattleClassesGuiButtonAbilityNode(abilityNode, 9999);
    	tempMovingNode.setPosition(abilityNode.getPositionX(), abilityNode.getPositionY());
    	
    	this.buttonList.add(tempMovingNode);
    }
    
    /**
     * Called from this.mouseClicked
     */
    public void onAbilityNodeReleased() {
    	this.selectedNode = null;
    	this.buttonList.remove(tempMovingNode);
    	tempMovingNode = null;
    	this.initActionbarAbilityNodes();
    }

}
