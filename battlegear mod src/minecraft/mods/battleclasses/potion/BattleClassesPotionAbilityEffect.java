package mods.battleclasses.potion;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import mods.battleclasses.ability.BattleClassesAbstractAbility;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesAttributes;
import mods.battleclasses.gui.BattleClassesGuiHelper;

/**
 * Potion to represent the effect of an ability.
 * After regular init, setPotionName to the name of the ability
 * Example: <...init...>.setPotionName("mage.frostbolt")
 * @author Zsolt
 *
 */
public class BattleClassesPotionAbilityEffect extends BattleClassesPotion {

	protected String abilityName;
	
	BattleClassesPotionAbilityEffect(int id, boolean badEffect, int liquidColorCode) {
		super(id, badEffect, liquidColorCode);
	}
	
	/**
     * Set the potion name to the displayed name of the ability and save the pure abilityName.
     */
	@Override
    public Potion setPotionName(String abilityName)
    {
		super.setPotionName(BattleClassesAbstractAbility.UNLOCALIZED_PREFIX_ABILITY + abilityName + ".name");
        this.abilityName = abilityName;
        return this;
    }

	/**
	 * Draws the icon of the ability the potion should represent as an effect.
	 */
	@SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) { 
		ResourceLocation potionIcon = BattleClassesGuiHelper.getAbilityIconResourceLocation(this.abilityName);
		mc.getTextureManager().bindTexture(potionIcon);
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    BattleClassesGuiHelper.drawTexturedRectFromCustomSource(x+7, y+8, 16, 16, 0);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
	}
}
