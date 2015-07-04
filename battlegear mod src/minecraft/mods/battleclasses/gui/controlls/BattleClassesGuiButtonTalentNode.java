package mods.battleclasses.gui.controlls;

import java.util.List;

import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.ability.talent.BattleClassesAbstractTalent;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battleclasses.packet.BattleClassesPacketTalentNodeChosen;
import mods.battlegear2.Battlegear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class BattleClassesGuiButtonTalentNode extends BattleClassesGuiButton {
	
	public BattleClassesAbstractTalent talentAbility;

	public BattleClassesGuiButtonTalentNode(int id, BattleClassesAbstractTalent talentAbility) {
		super(id, 0, 0, 20, 20, "talentNode");
		this.talentAbility = talentAbility;
		this.showHoveringText = true;
	}

	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int currentMousePosX, int currentMousePosY)
    {
        if (this.visible && talentAbility != null)
        {	
            FontRenderer fontrenderer = mc.fontRenderer;
            
            GL11.glPushMatrix();
    		
            
            //InWindow
            this.field_146123_n = currentMousePosX >= this.xPosition && currentMousePosY >= this.yPosition && currentMousePosX < this.xPosition + this.width && currentMousePosY < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            
            //Draw talent icon (alpha by availability)
            if(talentAbility.isLitOnUI()) {
            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            else {
            	GL11.glColor4f(0.4F, 0.4F, 0.4F, 1.0F);
            }
            
            GL11.glEnable(GL11.GL_BLEND);
    		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            mc.getTextureManager().bindTexture(talentAbility.getIconResourceLocation());
            this.myDrawTexturedModalRect(this.xPosition, this.yPosition, 16, 16);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_BLEND);
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            //Draw hover-over square
            if (this.field_146123_n)
            {
            	GL11.glPushMatrix();
                int j1 = this.xPosition;
                int k1 = this.yPosition;
                GL11.glColorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GL11.glColorMask(true, true, true, true);
                GL11.glPopMatrix();
                
                this.renderHoveringText(currentMousePosX, currentMousePosY);
            }
            
    		GL11.glPopMatrix();

        }
    }
    
    @Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		boolean press = inWindow && talentAbility.isAvailableToLearn();
		if (press) {
			FMLProxyPacket p = new BattleClassesPacketTalentNodeChosen(mc.thePlayer, talentAbility.getAbilityID()).generatePacket();
			BattleClassesMod.packetHandler.sendPacketToServer(p);
		}
		return press;
	}
    
    @Override
    public List<String> getTooltipText() {
    	return this.talentAbility.getTooltipText();
	}
}
