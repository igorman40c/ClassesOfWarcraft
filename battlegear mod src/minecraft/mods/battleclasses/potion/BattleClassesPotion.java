package mods.battleclasses.potion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class BattleClassesPotion extends Potion {
	
	public static BattleClassesPotion testing;// = (BattleClassesPotion) (new BattleClassesPotion(33, false, 10044730)).setPotionName("potion.bctesting");
	
	protected BattleClassesPotion(int id, boolean badEffect, int liquidColorCode) {
		super(id, badEffect, liquidColorCode);
	}
	
	@SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) { 
		ResourceLocation potionIcon = BattleClassesGuiHelper.getAbilityIconResourceLocation(102);
		//ResourceLocation potionIcon = BattleClassesGuiHelper.getResourceLocationOfTexture("textures/talents/icons/", "talent_icon_mage_2_1.png");
		mc.getTextureManager().bindTexture(potionIcon);
		//BattleClassesGuiHelper.drawTexturedRectFromCustomSource(x+7, y+8, 16, 16, 0);
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    BattleClassesGuiHelper.drawTexturedRectFromCustomSource(x+7, y+8, 16, 16, 0);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
	}

	public static final int POTION_TYPES_CAPACITY = 256;
	public static void registerPotions() {
		//Increasing size of potionTypes array
		if(Potion.potionTypes.length >= POTION_TYPES_CAPACITY) {
			System.out.println("Battle Classes mod trying to increase size of Potion.potionTypes array, but it's already modified. Current lenght:" + potionTypes.length);
		}
		for (Field f : Potion.class.getDeclaredFields()) {
	        f.setAccessible(true);
	        try {
	            if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
	                Field modfield = Field.class.getDeclaredField("modifiers");
	                modfield.setAccessible(true);
	                modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

	                //Potion.potionTypes = (Potion[])f.get(null);
	                final Potion[] newPotionTypes = new Potion[POTION_TYPES_CAPACITY];
	                System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
	                f.set(null, newPotionTypes);
	            }
	        } catch (Exception e) {
	            System.err.println("Severe error, please report this to the mod author:");
	            System.err.println(e);
	        }
	    }
		
		//Initializing potion effects
		testing = (BattleClassesPotion) (new BattleClassesPotion(33, false, 10044730)).setPotionName("bcpotion.testing");
	}
}
