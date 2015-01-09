package mods.battleclasses.gui.controlls;

import org.lwjgl.opengl.GL11;

import mods.battleclasses.enumhelper.EnumBattleClassesAttributeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiAttributeDisplay extends BattleClassesGuiButton {

	public static final ResourceLocation resourceAttributes = new ResourceLocation("battleclasses", "textures/gui/InterfaceAttributes.png");

	public static final int GUI_ELEMENT_WIDTH = 70;
	public static final int GUI_ELEMENT_Height = 10;
	
	public BattleClassesGuiAttributeDisplay(int id, int x, int y, EnumBattleClassesAttributeType type) {
		super(id, x, y, GUI_ELEMENT_WIDTH, GUI_ELEMENT_Height, "attributeDisplayNode");
		this.displayTooltip = true;
		this.displayString = type.toString();
	}

	protected EnumBattleClassesAttributeType displayType;
	protected float displayAmmount;
	
	public void setDisplayedAmmount(float value) {
		this.displayAmmount = value;
	}
	
	public float getDisplayedAmmount() {
		return this.displayAmmount;
	}
	
	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {	
            FontRenderer fontrenderer = mc.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if(this.displayType == EnumBattleClassesAttributeType.ARMOR )
            mc.getTextureManager().bindTexture(this.resourceAttributes);
            
            //InWindow
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            
            //Bar Button Texture
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(	this.xPosition,
            							this.yPosition,
            							this.displayType.getDisplayIconU(),
            							this.displayType.getDisplayIconV(),
            							this.displayType.getDisplayIconSquareSize(),
            							this.displayType.getDisplayIconSquareSize());
            
            //Rendering Button display string
            int l = 14737632;
            if (packedFGColour != 0) {
                l = packedFGColour;
            }
            else if (!this.enabled) {
                l = 10526880;
            }
            else if (this.field_146123_n) {
                l = 16777120;
            }
            this.drawString(fontrenderer, "", this.xPosition, this.yPosition, 1);

            //Rendering Tooltip
            if( this.field_146123_n && this.displayTooltip) {
            	this.drawHoveringText(this.getDescriptionList(), p_146112_2_, p_146112_3_, fontrenderer);
            }
        }
    }

}
