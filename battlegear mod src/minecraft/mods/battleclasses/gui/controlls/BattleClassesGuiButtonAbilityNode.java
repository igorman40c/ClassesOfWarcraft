package mods.battleclasses.gui.controlls;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiButtonAbilityNode extends BattleClassesGuiButton
{
	public BattleClassesAbstractAbilityActive ability;
	public BattleClassesGuiButtonAbilityNode(BattleClassesAbstractAbilityActive parAbility) {
		super(parAbility.getAbilityID(), BattleClassesTabSpellbook.resource);
		this.ability = parAbility;
		this.showHoveringText = true;
		this.setSize(16, 16);
	}
	
	public boolean isAddButton = false;
	
	/**
	 * Should only be used for creating temp draggable nodes
	 * @param copyNode
	 * @param newID
	 */
	public BattleClassesGuiButtonAbilityNode(BattleClassesGuiButtonAbilityNode copyNode, int id) {
		super(id, BattleClassesTabSpellbook.resource);
		this.ability = copyNode.ability;
		this.showHoveringText = false;
		this.setSize(16, 16);
		
		this.xPosition = copyNode.xPosition;
		this.yPosition = copyNode.yPosition;
	}
	
	public BattleClassesGuiButtonAbilityNode(int id) {
		super(id, BattleClassesTabSpellbook.resource);
		this.showHoveringText = false;
		this.setSize(16, 16);
		this.isAddButton = true;
		this.setOrigin(240, 240);
	}
	
		
	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
    	if(this.visible && isAddButton) {
    		GL11.glPushMatrix();
    		GL11.glEnable(GL11.GL_BLEND);
    		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            mc.getTextureManager().bindTexture(this.resource);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.origin_u, this.origin_v, this.width, this.height);
            
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
    	}
    	else if (this.visible && ability != null)
        {	
            FontRenderer fontrenderer = mc.fontRenderer;
            
            GL11.glPushMatrix();
            
            //InWindow
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            
          //Draw talent icon (alpha by availability)
            if(isSelectedNode()) {
            	GL11.glColor4f(0.4F, 0.4F, 0.4F, 1.0F);
            }
            else {
            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            
            GL11.glEnable(GL11.GL_BLEND);
    		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            
            BattleClassesGuiHelper.INSTANCE.drawAbilityIcon(this.xPosition, this.yPosition, ability);
            
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_BLEND);
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            //Draw hover-over square
            if (this.field_146123_n && !isSelectedNode() && !isTempMovingNode())
            {
            	GL11.glPushMatrix();
                int j1 = this.xPosition;
                int k1 = this.yPosition;
                GL11.glColorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GL11.glColorMask(true, true, true, true);
                GL11.glPopMatrix();
                
                if(!isDragInProgress()) {
                	this.renderHoveringText(mouseX, mouseY);
                }
            }
            
    		GL11.glPopMatrix();
        }
    }
    
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		if(inWindow && !isDragInProgress() && mc.currentScreen instanceof BattleClassesTabSpellbook) {
    		((BattleClassesTabSpellbook)mc.currentScreen).onAbilityNodeSelected(this);
    	}
		else if(isSelectedNode() && mc.currentScreen instanceof BattleClassesTabSpellbook) {
			((BattleClassesTabSpellbook)mc.currentScreen).mouseClickedDelegate(mouseX, mouseY);
		}
		return inWindow;
	}
    
    public boolean isDragInProgress() {
    	Minecraft mc = Minecraft.getMinecraft();
    	if(mc.currentScreen instanceof BattleClassesTabSpellbook) {
    		return ((BattleClassesTabSpellbook)mc.currentScreen).selectedNode != null;
    	}
    	return false;
    }
        
    public boolean isSelectedNode() {
    	Minecraft mc = Minecraft.getMinecraft();
    	if(mc.currentScreen instanceof BattleClassesTabSpellbook) {
    		return ((BattleClassesTabSpellbook)mc.currentScreen).selectedNode == this;
    	}
    	return false;
    }
    
    public boolean isTempMovingNode() {
    	Minecraft mc = Minecraft.getMinecraft();
    	if(mc.currentScreen instanceof BattleClassesTabSpellbook) {
    		return ((BattleClassesTabSpellbook)mc.currentScreen).tempMovingNode == this;
    	}
    	return false;
    }
    
    @Override
    public List<String> getHoveringTextStringList() {
		List<String> hoveringTextList = BattleClassesGuiHelper.createHoveringText();
    	BattleClassesGuiHelper.addTitle(hoveringTextList, this.ability.getTranslatedName());
    	BattleClassesGuiHelper.addParagraph(hoveringTextList, this.ability.getTranslatedDescription());
    	return hoveringTextList;
	}
}
