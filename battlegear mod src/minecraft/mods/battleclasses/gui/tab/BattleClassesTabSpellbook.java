package mods.battleclasses.gui.tab;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.talent.BattleClassesAbstractTalent;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battleclasses.core.BattleClassesTalentMatrix;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.gui.BattleClassesGuiHandler;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonAbilityNode;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonAbilityRank;
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
	public static ArrayList<BattleClassesGuiButtonAbilityRank> rankButtons = new ArrayList<BattleClassesGuiButtonAbilityRank>();
	
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
    			BattleClassesGuiButtonAbilityNode actionbarAbilityNode = new BattleClassesGuiButtonAbilityNode(actionbarAbility, 8000+actionbarAbility.getAbilityHashCode());
        		actionbarAbilityNodes.add(actionbarAbilityNode);
    			this.buttonList.add(actionbarAbilityNode);
        	}
    	}
    }
    
    public void updateRankButtons() {
    	for(BattleClassesGuiButtonAbilityRank rankButton : rankButtons) {
    		rankButton.updateDisplayTitle();
    	}
    }
    
    public void initSpellbookAbilityNodes() {
    	BattleClassesSpellBook spellbook = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer);
    	for(BattleClassesGuiButtonAbilityNode spellbookAbilityNode : spellbookAbilityNodes) {
    		if(this.buttonList.contains(spellbookAbilityNode)) {
    			this.buttonList.remove(spellbookAbilityNode);
    		}
    	}
    	for(BattleClassesGuiButtonAbilityRank upgradeButton : rankButtons) {
    		if(this.buttonList.contains(upgradeButton)) {
    			this.buttonList.remove(upgradeButton);
    		}
    	}
    	spellbookAbilityNodes.clear();
    	rankButtons.clear();
    	if(spellbook != null) {
    		int i = 0;
    		for(BattleClassesAbstractAbilityActive activeAbility : spellbook.getActiveAbilitiesInArray()) {
    			BattleClassesGuiButtonAbilityNode spellbookAbilityNode = new BattleClassesGuiButtonAbilityNode(activeAbility);
    			BattleClassesGuiButtonAbilityRank upgradeButton = new BattleClassesGuiButtonAbilityRank(9900+i, activeAbility);
    			spellbookAbilityNodes.add(spellbookAbilityNode);
    			rankButtons.add(upgradeButton);
    			this.buttonList.add(spellbookAbilityNode);
    			this.buttonList.add(upgradeButton);
    			++i;
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
        
        //Refresh spellbookAbilityNode positions
        for(int i = 0; i < spellbookAbilityNodes.size(); ++i) {
        	int posX = this.guiLeft + ((i<4) ? 20 : 95);
        	int posY = this.guiTop + 23 + ((i<4) ? i*(12+18) : (i-4)*(12+18)) ;
        	this.drawAbilityFrame(posX, posY);
        	//this.drawAbilityRank(posX + 19, posY, spellbookAbilityNodes.get(i).ability);
        	spellbookAbilityNodes.get(i).setPosition(posX+1, posY+1);
        	rankButtons.get(i).setPosition(posX+19, posY);
        }
        
        if(this.tempMovingNode != null) {
        	this.tempMovingNode.setPosition(mouseX-tempMovingNode.width/2, mouseY-tempMovingNode.height/2);
        }
	}
    
    public static void open(EntityPlayer player){
    	//send packet to open container on server
        //Battlegear.packetHandler.sendPacketToServer(new BattlegearGUIPacket(BattleClassesGUIHandler.talentsID).generatePacket());
    	BattleClassesMod.packetHandler.sendPacketToServer(new BattleClassesPacketGuiTabSwitch(BattleClassesGuiHandler.spellbookID).generatePacket());
    }
    
    public void drawAbilityFrame(int x, int y) {
    	GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        mc.getTextureManager().bindTexture(this.resource);
        this.drawTexturedModalRect(x, y, 199, 0, 18, 18);
        
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
    
    public void drawAbilityRank(int x, int y, BattleClassesAbstractAbilityActive activeAbility) {
    	this.drawString(Minecraft.getMinecraft().fontRenderer, "Rank 1", x, y, 0xFFFFFF);
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
    	return this.guiTop - (BattleClassesGuiHelper.ABILITY_ACTIONBAR_HEIGHT + BattleClassesTabOverlayAttributes.GAP_BETWEEN_WINDOWS);
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
        		int i = 0;
        		for(BattleClassesGuiButtonAbilityNode actionbarAbilityNode : actionbarAbilityNodes) {
            		if(actionbarAbilityNode.isMouseOver(mouseX, mouseY)) {
            			if(actionbarAbilityNode.ability != selectedNode.ability) {
            				BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).insertAbilityToActionbarAtIndex(selectedNode.ability, i); 
            			}
            			this.onAbilityNodeReleased();
            			return;
            		}
            		++i;
            	}
        		if(isInsideActionbar(mouseX, mouseY)) {
        			BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).addAbilityToActionbar(selectedNode.ability);
        			this.onAbilityNodeReleased();
        			return;
        		}
        	}
        }
        this.onAbilityNodeReleased();
    }
    
    public BattleClassesGuiButtonAbilityNode selectedNode;
    public BattleClassesGuiButtonAbilityNode tempMovingNode;
    public BattleClassesGuiButtonAbilityNode tempAddNode;
    /**
     * Called from BattleClassesGuiButtonAbilityNode
     * @param abilityNode
     */
    public void onAbilityNodeSelected(BattleClassesGuiButtonAbilityNode abilityNode) {
    	this.selectedNode = abilityNode;
    	tempMovingNode = new BattleClassesGuiButtonAbilityNode(abilityNode, 9999);
    	tempMovingNode.setPosition(abilityNode.getPositionX(), abilityNode.getPositionY());
    	if(spellbookAbilityNodes.contains(this.selectedNode) && !BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).isAbilityOnActionbar(this.selectedNode.ability)) {
    		tempAddNode = new BattleClassesGuiButtonAbilityNode(9998);
    		actionbarAbilityNodes.add(tempAddNode);
    		this.buttonList.add(tempAddNode);
    	}
    	this.buttonList.add(tempMovingNode);
    }
    
    /**
     * Called from this.mouseClicked
     */
    public void onAbilityNodeReleased() {
    	this.selectedNode = null;
    	this.buttonList.remove(tempMovingNode);
    	this.buttonList.remove(tempAddNode);
    	actionbarAbilityNodes.remove(tempAddNode);
    	tempMovingNode = null;
    	tempAddNode = null;
    	this.initActionbarAbilityNodes();
    }

}
