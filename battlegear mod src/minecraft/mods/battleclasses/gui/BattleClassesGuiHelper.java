package mods.battleclasses.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiHelper extends Gui {
	
	public static final int COOLDOWN_FRAMES = 16;
	public static IIcon cooldownIcons[];
	
	public static Minecraft mc = Minecraft.getMinecraft();

	public static ResourceLocation getAbilityIconResourceLocation(int abilityID) {
		return getResourceLocationOfTexture("textures/spells/icons/","ability_" + abilityID + ".png");
	}
	
	public static ResourceLocation getResourceLocationOfTexture(String path, String fileName) {
		return new ResourceLocation(BattleClassesMod.MODID, path + fileName );
	}
	
	public static void drawCooldown(int posX, int posY, float f) {
    	if(f > 0) {
    		int frameIndex = BattleClassesGuiHelper.cooldownIcons.length-1;
    		if(f < 1) {
    			frameIndex = (int) (((float) BattleClassesGuiHelper.cooldownIcons.length)*f);
    		}
    		IIcon cooldownIcon = BattleClassesGuiHelper.cooldownIcons[frameIndex];
    	    
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
		    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		    GL11.glDisable(GL11.GL_LIGHTING);
		    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
		    
		    mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
    		//this.drawTexturedModelRectFromIcon(posX, posY, cooldownIcon, cooldownIcon.getIconWidth(), cooldownIcon.getIconHeight());
    		
    		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);    		    
	        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glPopMatrix();
      	}
	}
	
	/**
	 * Draws textured modal rectangle without any disortion
	 * when binding custom sized image source to the texture manager.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param zLevel
	 */
	public static void drawTexturedRectFromCustomSource(int x, int y, int width, int height, float zLevel)
	{
		 Tessellator tessellator = Tessellator.instance;
		 tessellator.startDrawingQuads();    
		 tessellator.addVertexWithUV(x        , y + height, (double)zLevel, 0.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y + height, (double)zLevel, 1.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y         , (double)zLevel, 1.0, 0.0);
		 tessellator.addVertexWithUV(x        , y         , (double)zLevel, 0.0, 0.0);
		 tessellator.draw();
	}
}
