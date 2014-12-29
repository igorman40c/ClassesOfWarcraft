package mods.battleclasses.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;

public class GuiHighLightLabel extends Gui {
	
	public final Minecraft mc;
	
	public GuiHighLightLabel() {
		super();
		mc = Minecraft.getMinecraft();
	}
	
	public int posX;
	public int posY;
	/**
	 * Alignment Mode
	 * 0 = Left
	 * 1 = Center
	 * 2 = Right
	 */
	public int horizontalAlignmentMode = 0;
	public String text = "";
	
	private static final int WHITE = 0xFFFFFF;
	public int colorHEX = WHITE;
	
	public int remainingHighlightTicks = 0;
	
	public int fadeOutDurationTicks = 10;
	public int visibleDurationTicks = 60;
	
	public void show() {
		remainingHighlightTicks = visibleDurationTicks;
	}
	
	public void setColorHEX(int parColorHEX) {
		this.colorHEX = parColorHEX;
	}
	
	public void setText(String parText) {
		this.text = parText;
	}
	
	public void setVisibleDuration(float seconds) {
		visibleDurationTicks = (int) (20.0F * seconds);
	}
	
    public void draw(FontRenderer fontrenderer)
    {
    	if (this.remainingHighlightTicks > 0 && this.text != null)
        {
            String name = this.text;

            int opacity = (int)((float)this.remainingHighlightTicks * 256.0F / 10.0F);
            if (opacity > 255) opacity = 255;
            
            int width = fontrenderer.getStringWidth(name);

            if (opacity > 0)
            {
                int y = posY;
                int x = posX;
                
                switch(this.horizontalAlignmentMode) {
                	case 0 : { } break;
                	case 1 : { x -= (width/2); } break;	
                	case 2 : { x -= (width); } break;	
                }

                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                
                fontrenderer.drawStringWithShadow(name, x, y, this.colorHEX | (opacity << 24));
                
                this.mc.getTextureManager().bindTexture(icons);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
                
                remainingHighlightTicks--;
            }
        }
    }
    
    public void hide() {
    	remainingHighlightTicks = 0;
    }

}
