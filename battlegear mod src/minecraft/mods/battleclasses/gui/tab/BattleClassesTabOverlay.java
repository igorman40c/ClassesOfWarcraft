package mods.battleclasses.gui.tab;

import java.util.Collection;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class BattleClassesTabOverlay extends Gui {
	
    public static final ResourceLocation resourceAttributes = new ResourceLocation("battleclasses", "textures/gui/InterfaceAttributes.png");
	
	public static void drawPotionEffectsOverlay() {
		/*
		Minecraft mc = Minecraft.getMinecraft();
		
		int i = this.guiLeft - 124;
        int j = this.guiTop;
        boolean flag = true;
        Collection collection = mc.thePlayer.getActivePotionEffects();

        if (!collection.isEmpty())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            int k = 33;

            if (collection.size() > 5)
            {
                k = 132 / (collection.size() - 1);
            }

            for (Iterator iterator = mc.thePlayer.getActivePotionEffects().iterator(); iterator.hasNext(); j += k)
            {
                PotionEffect potioneffect = (PotionEffect)iterator.next();
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(field_147001_a);
                drawTexturedModalRect(i, j, 0, 166, 140, 32);

                if (potion.hasStatusIcon())
                {
                    int l = potion.getStatusIconIndex();
                    this.drawTexturedModalRect(i + 6, j + 7, 0 + l % 8 * 18, 198 + l / 8 * 18, 18, 18);
                }

                String s1 = I18n.format(potion.getName(), new Object[0]);

                if (potioneffect.getAmplifier() == 1)
                {
                    s1 = s1 + " II";
                }
                else if (potioneffect.getAmplifier() == 2)
                {
                    s1 = s1 + " III";
                }
                else if (potioneffect.getAmplifier() == 3)
                {
                    s1 = s1 + " IV";
                }

                this.fontRendererObj.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215);
                String s = Potion.getDurationString(potioneffect);
                this.fontRendererObj.drawStringWithShadow(s, i + 10 + 18, j + 6 + 10, 8355711);
            }
        }
        */
	}
	
	public static final int GAP_BETWEEN_WINDOWS = 5;
	
	public void drawAttributesDisplayWindow(int guiLeft, int guiTop, int guiWidth, float parZLevel) {
		Minecraft mc = Minecraft.getMinecraft();
		//Draw window background
		int windowOriginX = guiLeft + guiWidth + GAP_BETWEEN_WINDOWS;
		int windowOriginY = guiTop;
		int windowWidth = 77;
		int windowHeight = 167;
		this.zLevel = 0;
		
       
        GL11.glPushMatrix();
        //GL11.glEnable(GL11.GL_BLEND);
        //OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(resourceAttributes);
		this.drawTexturedModalRect(windowOriginX, windowOriginY, 0, 0, windowWidth, windowHeight);
		
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
		
	}
}
