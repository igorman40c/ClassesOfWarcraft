package mods.battleclasses.gui.controlls;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.gui.tab.BattleClassesTabClassSelector;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiButtonClassSelector extends BattleClassesGuiButton {
	
	public EnumBattleClassesPlayerClass playerClass;
	
	public BattleClassesGuiButtonClassSelector(int id, ResourceLocation resource, EnumBattleClassesPlayerClass parPlayerClass) {
		super(id, resource);
		this.playerClass = parPlayerClass;
		this.showHoveringText = true;
		this.hoveringTextString = BattleClassesTabClassSelector.getClassDescription(playerClass);
	}
	
	public boolean shouldBeDisabled() {
		return isClassSelected();
	}
	
	public boolean isClassSelected() {
		return BattleClassesUtils.getPlayerClassEnum(Minecraft.getMinecraft().thePlayer) == this.playerClass;
	}
	
	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int currentMousePosX, int currentMousePosY)
    {
        if (this.visible)
        {	
            super.drawButton(mc, currentMousePosX, currentMousePosY);
            FontRenderer fontrenderer = mc.fontRenderer;
            //Drawing Tab Icon
            mc.getTextureManager().bindTexture(this.playerClass.getIconResourceLocation());
            int classIconWidth = 16;
            int classIconHeight = 16;
            BattleClassesGuiHelper.drawTexturedRectFromCustomSource(this.xPosition + this.width/2 - classIconWidth/2, this.yPosition + this.height/2 -  classIconHeight/2, classIconWidth, classIconHeight, this.zLevel);
            /*this.drawTexturedModelRectFromIcon(this.xPosition + this.width/2 - classIcon.getIconWidth()/2, 
            								this.yPosition + this.height/2 -  classIcon.getIconHeight()/2,
            								classIcon, classIcon.getIconWidth(), classIcon.getIconHeight());
             */
            //Drawing Cooldown
            /*
            if(shouldBeDisabled()) {
            	drawCooldown(this.xPosition + this.width/2 - classIconWidth/2, 
						this.yPosition + this.height/2 -  classIconHeight/2,
						BattleClassesUtils.getCooldownPercentage(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass));
            }
            */
        }
    }
    
    public void drawCooldown(int posX, int posY, float f) {
    	if(f > 0) {
    		int frameIndex = BattleClassesGuiHelper.cooldownIcons.length-1;
    		if(f < 1) {
    			frameIndex = (int) (((float) BattleClassesGuiHelper.cooldownIcons.length)*f);
    		}
    		IIcon cooldownIcon = BattleClassesGuiHelper.cooldownIcons[frameIndex];
    	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);

    		this.drawTexturedModelRectFromIcon(posX, posY, cooldownIcon, cooldownIcon.getIconWidth(), cooldownIcon.getIconHeight());
    		
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	}
	}
    
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		boolean press = inWindow && !isClassSelected();
		if (press) {
			FMLProxyPacket p = new BattleClassesPacketPlayerClassSnyc(mc.thePlayer, this.playerClass).generatePacket();
			BattleClassesMod.packetHandler.sendPacketToServer(p);
		}
		return press;
	}

}
