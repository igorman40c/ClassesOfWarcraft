package mods.battleclasses.gui.controlls;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.BattleClassesGuiConfirmation;
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

public class BattleClassesGuiButtonClassSelector extends BattleClassesGuiButton implements BattleClassesGuiConfirmation.Handler {
	
	public EnumBattleClassesPlayerClass playerClass;
	
	public static final ResourceLocation classselectionGuiResource = BattleClassesTabClassSelector.resource;
	
	public BattleClassesGuiButtonClassSelector(int id, ResourceLocation resource, EnumBattleClassesPlayerClass parPlayerClass) {
		super(id, resource);
		this.playerClass = parPlayerClass;
		this.showHoveringText = true;
		
		this.setOrigin(0, 216);
		this.setSize(32, 32);
		this.horizontalOrigin = true;
		this.drawButtonTexture = false;
	}
	
	public boolean shouldBeDisabled() {
		return isClassSelected();
	}
	
	public boolean isClassSelected() {
		return BattleClassesUtils.getPlayerClassEnum(Minecraft.getMinecraft().thePlayer) == this.playerClass;
	}
	
	public boolean hasAnyClassSelected() {
		return BattleClassesUtils.getPlayerClassEnum(Minecraft.getMinecraft().thePlayer) != EnumBattleClassesPlayerClass.NONE;
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
            
            
            int hoverState = this.getHoverState(this.field_146123_n);
            if(hoverState == 0) {
            	
            }
            
            //Drawing class icon
            int classIconWidth = 32;
            int classIconHeight = 32;
            mc.getTextureManager().bindTexture(this.playerClass.getIconResourceLocation());
            BattleClassesGuiHelper.drawTexturedRectFromCustomSource(this.xPosition + this.width/2 - classIconWidth/2, this.yPosition + this.height/2 -  classIconHeight/2, classIconWidth, classIconHeight, this.zLevel);
            
            //Drawing selector
            if (this.isClassSelected()) {
            	int selectorWidth = 40;
                int selectorHeight = 40;
                int selectorX = this.xPosition + this.width/2 - classIconWidth/2 - ((selectorWidth-classIconWidth)/2);
                int selectorY = this.yPosition + this.height/2 -  classIconHeight/2 -((selectorHeight-classIconHeight)/2);
                int selectorU = 0;
                int selectorV = 255 - 40;
                mc.getTextureManager().bindTexture(classselectionGuiResource);
                this.drawTexturedModalRect(selectorX, selectorY, selectorU, selectorV, selectorWidth, selectorHeight);
            }
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
		boolean press = inWindow;
		boolean shouldSelectClass = !this.hasAnyClassSelected();
		boolean showConfirmationDialog = this.hasAnyClassSelected() && !isClassSelected();
		if (press) {
			if(shouldSelectClass) {
				this.selectPlayerClass(mc, this.playerClass);
			}
			else if (showConfirmationDialog) {
				System.out.println("Show confirmation dialog, return and reset class");
				//this.selectPlayerClass(mc, EnumBattleClassesPlayerClass.NONE);
				mc.displayGuiScreen(new BattleClassesGuiConfirmation("bcgui.confirmation.class_reset.title", "bcgui.confirmation.class_reset.message", true, this, mc.currentScreen));
			}
		}
		return press;
	}
	
	protected void selectPlayerClass(Minecraft mc, EnumBattleClassesPlayerClass playerClass) {
		FMLProxyPacket p = new BattleClassesPacketPlayerClassSnyc(mc.thePlayer, playerClass).generatePacket();
		BattleClassesMod.packetHandler.sendPacketToServer(p);
	}
	
	@Override
    public List<String> getTooltipText() {
    	return this.playerClass.getTooltipText();
	}

	@Override
	public void confirmationDialogDidFinish(BattleClassesGuiConfirmation confirmationGuiScreen, boolean result) {
		Minecraft mc = Minecraft.getMinecraft();
		if(result) {
			this.selectPlayerClass(mc, EnumBattleClassesPlayerClass.NONE);
		}
	}

}
