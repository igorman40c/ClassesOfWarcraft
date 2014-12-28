package mods.battleclasses.gui.controlls;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiButton extends GuiButton {
	
	public ResourceLocation resource;
	
	public String iconName = "";

	public static final int DEFAULT_X = 20;
	public static final int DEFAULT_Y = 20;
	public static final int DEFAULT_WIDHT = 20;
	public static final int DEFAULT_HEIGHT = 20;
	public static final String DEFAULT_NAME = "";
	
	public int origin_x = 0;
	public int origin_y = 0;
	
	public boolean displayTooltip = false;
	public String tooltipDescription = "";
	
	public BattleClassesGuiButton(int id, int x, int y, int width, int height, String name) {
		super(id, x, y, width, height, name);
		this.displayString = "";
	}
	
	public BattleClassesGuiButton(int id, ResourceLocation resource) {
		super(id, DEFAULT_X, DEFAULT_Y, DEFAULT_WIDHT, DEFAULT_HEIGHT, DEFAULT_NAME);
		this.resource = resource;
	}
	
	public void setOrigin(int x, int y) {
		this.origin_x = x;
		this.origin_y = y;
	}
	
	public void setPosition(int x, int y) {
		this.xPosition = x;
		this.yPosition = y;
	}
	
	public void setSize(int width, int height) {
		this.height = height;
		this.width = width;
	}
	
	public String getIconRegisterPath() {
		return ( "battleclasses:sharedicons/gui/"+this.iconName);
	}
	
	@Override
	protected int getHoverState(boolean isMouseOver)
    {
		if(!shouldBeDisabled()){
			return super.getHoverState(isMouseOver);
		}
		return 0;
    }
	
	public boolean shouldBeDisabled() {
		return false;
	}
	
	public List getDescriptionList() {
		ArrayList stringList = new ArrayList();
    	stringList.add(this.tooltipDescription);
    	return stringList;
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
            mc.getTextureManager().bindTexture(this.resource);
            
            //InWindow
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            
            //Bar Button Texture
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.origin_x, this.origin_y + k*this.height, this.width, this.height);
            
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
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);

            //Rendering Tooltip
            if( this.field_146123_n && this.displayTooltip) {
            	this.drawHoveringText(this.getDescriptionList(), p_146112_2_, p_146112_3_, fontrenderer);
            }
        }
    }
	
    protected void drawHoveringText(List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font)
    {
        if (!p_146283_1_.isEmpty())
        {
            //GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            //RenderHelper.disableStandardItemLighting();
            //GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = p_146283_1_.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int j2 = p_146283_2_ + 12;
            int k2 = p_146283_3_ - 12;
            int i1 = 8;

            if (p_146283_1_.size() > 1)
            {
                i1 += 2 + (p_146283_1_.size() - 1) * 10;
            }

            /*
            if (j2 + k > this.width)
            {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > this.height)
            {
                k2 = this.height - i1 - 6;
            }
			*/
            
            RenderItem itemRender = null; 
            Minecraft mc = Minecraft.getMinecraft();
            if( mc.currentScreen != null) {
				try {
					Field f = GuiScreen.class.getDeclaredField("itemRender");
					f.setAccessible(true);
					itemRender = (RenderItem) f.get( ((GuiScreen)mc.currentScreen) );
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            
           // this.zLevel -= 300.0F;
            float zTemp = this.zLevel;
            if(itemRender != null) {
            	//BattleClassesUtils.Log("Button Z:" + this.zLevel + ", IR Z:" + itemRender.zLevel, LogType.GUI);
            	//this.zLevel += 10000.0F;
            	//itemRender.zLevel += 300.0F;
            }
            this.zLevel -= 1000.0F;
            
            int j1 = -267386864;
            
            this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);
			
            for (int i2 = 0; i2 < p_146283_1_.size(); ++i2)
            {
                String s1 = (String)p_146283_1_.get(i2);
                font.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0)
                {
                    k2 += 2;
                }

                k2 += 10;
            }
            
            
            //this.zLevel += 300.0F;
            this.zLevel += 1000.0F;
            if(itemRender != null) {
            	//BattleClassesUtils.Log("Button Z:" + this.zLevel + ", IR Z:" + itemRender.zLevel, LogType.GUI);
            	//itemRender.zLevel -= 300.0F;
            }
            
            //this.zLevel = zTemp;
            
            //GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            //RenderHelper.enableStandardItemLighting();
            //GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
	
	// 3.  You'll need to write your own version of the Gui.drawTexturedModalRect() method
	//  This method can go into your own Gui class:
	public void myDrawTexturedModalRect(int x, int y, int width, int height)
	{
		 Tessellator tessellator = Tessellator.instance;
		 tessellator.startDrawingQuads();    
		 tessellator.addVertexWithUV(x        , y + height, (double)this.zLevel, 0.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y + height, (double)this.zLevel, 1.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y         , (double)this.zLevel, 1.0, 0.0);
		 tessellator.addVertexWithUV(x        , y         , (double)this.zLevel, 0.0, 0.0);
		 tessellator.draw();
	}
}
