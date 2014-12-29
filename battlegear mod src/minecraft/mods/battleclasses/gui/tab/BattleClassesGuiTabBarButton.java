package mods.battleclasses.gui.tab;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import scala.reflect.internal.Trees.This;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battlegear2.client.gui.BattleEquipGUI;
import mods.battlegear2.client.gui.controls.GuiPlaceableButton;

public abstract class BattleClassesGuiTabBarButton extends GuiPlaceableButton {
	
	public IIcon tabButtonIcon;
	
	public boolean horizontal = true;
	
    protected static final ResourceLocation barButtonTexture = new ResourceLocation("battleclasses", "textures/gui/InterfaceOverlay.png");
    
	public BattleClassesGuiTabBarButton(int par1, int par2, int par3,
			String name) {
		super(par1, par2, par3, name);
		this.setContentPositionAndSize();
	}

	public BattleClassesGuiTabBarButton(int par1, int par2, int par3,
			String name, boolean parHorizontal) {
		super(par1, par2, par3, name);
		this.horizontal = parHorizontal;
		this.setContentPositionAndSize();
	}
	
	public static final int BAR_BUTTON_GAP = 1;
	
	public static final int BAR_BUTTON_SIZE_W_H = 32;
	public static final int BAR_BUTTON_SIZE_H_H = 28;
	public static final int BAR_BUTTON_SIZE_W_V = 28;
	public static final int BAR_BUTTON_SIZE_H_V = 31;
	
	protected int origin_X = 0;
	protected int origin_Y = 0;
	
	public void setContentPositionAndSize() {
		if(this.horizontal) {
			origin_X = 0;
			origin_Y = 10;
			
			this.width = BAR_BUTTON_SIZE_W_H;
			this.height = BAR_BUTTON_SIZE_H_H;
		}
		else {
			origin_X = 40;
			origin_Y = 163;
			
			this.width = BAR_BUTTON_SIZE_W_V;
			this.height = BAR_BUTTON_SIZE_H_V;
		}
	}
	
	
    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {	
            FontRenderer fontrenderer = mc.fontRenderer;
            if(isAccessAble()) {
            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            else {
            	GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            }
            
            mc.getTextureManager().bindTexture(barButtonTexture);
            
            //InWindow
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            
            //Rendering Bar Button Texture
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int selectedOffset = (this.isSelected()) ? this.width : 0;
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.origin_X + selectedOffset, this.origin_Y, this.width, this.height);
            
            //Rendering Tab Icon
            int iconOffsetX = (this.horizontal) ? 7 : 6;
            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
            this.drawTexturedModelRectFromIcon(this.xPosition  + iconOffsetX, this.yPosition + this.height - 16 - 6, tabButtonIcon, 16, 16);
                        
            //Rendering Tab Name
            if( field_146123_n /*k == 2*/) {
            	ArrayList stringList = new ArrayList();
            	stringList.add(this.displayString);
            	//mc.currentScreen.drawHoveringText
            	this.drawHoveringText(stringList, p_146112_2_, p_146112_3_, fontrenderer);
            }
        }
    }
    
    /*
    public void drawHoveringTextInvoked(List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font) {
    	Minecraft mc =  Minecraft.getMinecraft();
    	if( mc.currentScreen != null) {
			try {
				Class<?> params[] = {List.class, int.class, int.class, FontRenderer.class};
				Method e = GuiScreen.class.getDeclaredMethod("drawHoveringText", params);
				e.setAccessible(true);
				e.invoke(mc.currentScreen, p_146283_1_, p_146283_2_, p_146283_3_, font);
				//e.invoke(p_146283_1_, p_146283_2_, p_146283_3_, font);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    */
    
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
            /*
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
            */
            /*
            if(itemRender != null) {
            	//BattleClassesUtils.Log("Button Z:" + this.zLevel + ", IR Z:" + itemRender.zLevel, LogType.GUI);
            	//this.zLevel += 10000.0F;
            	//itemRender.zLevel += 300.0F;
            }
            */
            
            float zTemp = this.zLevel;
            
            if( mc.currentScreen != null) {
				try {
					Field f = Gui.class.getDeclaredField("zLevel");
					f.setAccessible(true);
					//this.zLevel = f.getFloat(((Gui)mc.currentScreen)) + 1000.0F;
					this.zLevel += 1000.0;
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
                        
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
            this.zLevel = zTemp;
            /*
            if(itemRender != null) {
            	//BattleClassesUtils.Log("Button Z:" + this.zLevel + ", IR Z:" + itemRender.zLevel, LogType.GUI);
            	//itemRender.zLevel -= 300.0F;
            }
            */
            
            //this.zLevel = zTemp;
            
            //GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            //RenderHelper.enableStandardItemLighting();
            //GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
    
    /*
    public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par5), (double)this.zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + par5), (double)this.zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + 0), (double)this.zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMinV());
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMinV());
        tessellator.draw();
    }
    */
    
	public String getIconRegisterPath() {
		return ( "battleclasses:sharedicons/gui/"+this.getIconName() );
	}
    
    public boolean isSelected() {
    	Minecraft mc = Minecraft.getMinecraft();
    	return (mc.currentScreen.getClass() == this.getGUIClass());
    }

	@Override
	protected abstract Class<? extends GuiScreen> getGUIClass();

	@Override
	protected abstract void openGui(Minecraft mc);
	
	public abstract String getIconName();

}
