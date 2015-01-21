package mods.battleclasses.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiHelper {
	
	public static final int COOLDOWN_FRAMES = 16;
	public static IIcon cooldownIcons[];

	public static ResourceLocation getAbilityIconResourceLocation(int abilityID) {
		return getResourceLocationOfTexture("textures/spells/icons/","ability_" + abilityID + ".png");
	}
	
	public static ResourceLocation getResourceLocationOfTexture(String path, String fileName) {
		return new ResourceLocation(BattleClassesMod.MODID, path + fileName );
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
