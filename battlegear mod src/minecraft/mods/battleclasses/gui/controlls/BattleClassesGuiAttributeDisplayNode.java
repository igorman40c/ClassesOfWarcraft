package mods.battleclasses.gui.controlls;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.enumhelper.EnumBattleClassesAttributeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiAttributeDisplayNode extends BattleClassesGuiButton {

	public static final ResourceLocation resourceAttributes = new ResourceLocation("battleclasses", "textures/gui/InterfaceAttributes.png");

	public static final int GUI_ELEMENT_WIDTH = 59;
	public static final int GUI_ELEMENT_HEIGHT = 13;
	
	public BattleClassesGuiAttributeDisplayNode(int id, int x, int y, EnumBattleClassesAttributeType type) {
		super(id, x, y, GUI_ELEMENT_WIDTH, GUI_ELEMENT_HEIGHT, "attributeDisplayNode");
		this.displayedAttributeType = type;
		this.displayTooltip = true;
		this.tooltipDescription = type.toString();
		this.origin_u = 112;
		this.origin_v = 0;
		this.setSize(GUI_ELEMENT_WIDTH, GUI_ELEMENT_HEIGHT);
	}

	protected EnumBattleClassesAttributeType displayedAttributeType;
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
    public void drawButton(Minecraft mc, int currentMousePosX, int currentMousePosY)
    {
        if (this.visible && displayedAttributeType!=null)
        {	
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(this.resourceAttributes);
            //InWindow
            this.field_146123_n = currentMousePosX >= this.xPosition && currentMousePosY >= this.yPosition && currentMousePosX < this.xPosition + this.width && currentMousePosY < this.yPosition + this.height;
            
            GL11.glPushMatrix();
    		GL11.glEnable(GL11.GL_BLEND);
    	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	    //GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    	    GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            //Drawing node background
            this.drawTexturedModalRect(	this.xPosition,
										this.yPosition,
										this.origin_u,
										this.origin_v,
										this.width,
										this.height);
            //Drawing attribute icon
            this.drawTexturedModalRect(	this.xPosition + 2,
            							this.yPosition + 2,
            							this.displayedAttributeType.getDisplayIconU(),
            							this.displayedAttributeType.getDisplayIconV(),
            							this.displayedAttributeType.getDisplayIconSquareSize(),
            							this.displayedAttributeType.getDisplayIconSquareSize());
            
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
            //Rendering value label of the attribute
            this.drawString(fontrenderer, this.getDisplayString(), this.xPosition + 15, this.yPosition + 2, l);

            this.renderHoveringText(currentMousePosX, currentMousePosY);
            
            //GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    
	public String getDisplayString() {
		Minecraft mc = Minecraft.getMinecraft();
		float displayedValue = 0;
		if(this.displayedAttributeType == EnumBattleClassesAttributeType.ARMOR) {
			displayedValue = 0;
		}
		else {
			displayedValue = BattleClassesUtils.getPlayerHooks(mc.thePlayer).getDisplayedAttributes().getByType(this.displayedAttributeType);
		}
		String valueString;
		if(this.displayedAttributeType.isDisplayedInPercentage()) {
			if(displayedValue % 10 > 0) {
				valueString = String.format("%.2f", displayedValue);
			}
			else {
				valueString = String.format("%.0f", displayedValue);
			}
			valueString += " %";
		}
		else {
			valueString = String.format("%.0f", displayedValue);
		}
		
		return valueString;
	}
}
