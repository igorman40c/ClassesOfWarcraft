package mods.battleclasses.client.renderer;

import mods.battleclasses.items.weapons.BattleClassesItemWeaponTwoHanded;
import mods.battleclasses.items.weapons.IBattleClassesBow;
import mods.battleclasses.items.weapons.IHighDetailWeapon;
import mods.battlegear2.client.utils.BattlegearRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

import org.lwjgl.opengl.GL11;

public class BattleClassesHDWeaponRenderer implements IItemRenderer {
    private RenderItem itemRenderer;

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return item!=null && item.getItem() instanceof IHighDetailWeapon && (type == ItemRenderType.INVENTORY /*|| type == ItemRenderType.ENTITY*/ | type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }
    
    public static float getRenderScalefactor(IIcon icon, IHighDetailWeapon HD_weapon) {
    	float scaleFactor = 1;
    	int defaultSize = 16;
    	if(icon.getIconWidth() > icon.getIconHeight()) {
    		scaleFactor = ((float)icon.getIconWidth())/ ((float)defaultSize);
    	}
    	else {
    		scaleFactor = ((float)icon.getIconHeight())/ ((float)defaultSize);
    	}
    	scaleFactor *= HD_weapon.getScalefactor();
    	return scaleFactor;
    }
    
    public static boolean isCurrentlyHeldBy(ItemStack itemStack, EntityLivingBase entityLivingBase) {
    	return entityLivingBase.getHeldItem() == itemStack;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {

        if (itemRenderer == null) {
            itemRenderer = new RenderItem();
        }
        GL11.glPushMatrix();
        IIcon icon = itemStack.getIconIndex();
        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
        	IHighDetailWeapon HD_weapon = (IHighDetailWeapon)itemStack.getItem();
        	
        	float scaleFactor = getRenderScalefactor(icon, HD_weapon);
        	float scaleDifference = scaleFactor - 1;
        	GL11.glTranslatef( - 0.25F - (scaleDifference) + scaleFactor*HD_weapon.getRelativeAnchorPointX(), + 0.25F - scaleFactor*HD_weapon.getRelativeAnchorPointY(), 0);
            GL11.glScalef(scaleFactor, scaleFactor, 1);
            if(itemStack.getItem() instanceof IBattleClassesBow) {
            	if(isCurrentlyHeldBy(itemStack,(EntityLivingBase) data[1])) {
        			icon = ((IBattleClassesBow)itemStack.getItem()).getItemIconForUse((EntityLivingBase) data[1], itemStack);
        			float f  = ((IBattleClassesBow)itemStack.getItem()).getInUseRatio((EntityLivingBase) data[1], itemStack);
        			
        			if(type == ItemRenderType.EQUIPPED) {
        				GL11.glTranslatef(0,0, 3F/16F);
            		}
        			if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
        				GL11.glTranslatef(0, f*1F/16F*scaleFactor, 0);
            			//GL11.glTranslatef(0, f*0.25F*scaleFactor ,0);
            		}
        		}
        	}
            
            Tessellator tessellator = Tessellator.instance;
            ItemRenderer.renderItemIn2D(tessellator,
                    icon.getMaxU(),
                    icon.getMinV(),
                    icon.getMinU(),
                    icon.getMaxV(),
                    icon.getIconWidth(),
                    icon.getIconHeight(), 1F / 16F);
            
            if (itemStack.hasEffect(0)) {
                BattlegearRenderHelper.renderEnchantmentEffects(tessellator);
            }

        }else if (type == ItemRenderType.INVENTORY) {
        	IHighDetailWeapon HD_weapon = (IHighDetailWeapon)itemStack.getItem();
        	float scaleFactor = 1F / getRenderScalefactor(icon, HD_weapon);
        	GL11.glScalef(scaleFactor, scaleFactor, 1);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            //GL11.glRotatef(90, 0, 0, 1);
            //MOJANG derp fixes:
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            //    GL11.glEnable(GL11.GL_BLEND);
            itemRenderer.renderIcon(0, 0, icon, icon.getIconWidth(), icon.getIconHeight());
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            if(itemStack.hasEffect(0))
                itemRenderer.renderEffect(Minecraft.getMinecraft().getTextureManager(), 0, 0);
        }
        /*
        else if (type == ItemRenderType.ENTITY) {
        	float scaleFactor = 1;
        	int defaultSize = 16;
        	if(icon.getIconWidth() > icon.getIconHeight()) {
        		scaleFactor = ((float)icon.getIconWidth())/ ((float)defaultSize);
        	}
        	else {
        		scaleFactor = ((float)icon.getIconHeight())/ ((float)defaultSize);
        	}
        	GL11.glScalef(scaleFactor, scaleFactor, 1);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            //GL11.glRotatef(90, 0, 0, 1);
            //MOJANG derp fixes:
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            //    GL11.glEnable(GL11.GL_BLEND);
            itemRenderer.renderIcon(0, 0, icon, icon.getIconWidth(), icon.getIconHeight());
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            if(itemStack.hasEffect(0))
                itemRenderer.renderEffect(Minecraft.getMinecraft().getTextureManager(), 0, 0);
        }
        */
        GL11.glPopMatrix();
    }
}