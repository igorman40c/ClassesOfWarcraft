package mods.battleclasses.client.renderer;

import mods.battleclasses.items.BattleClassesItemWeaponTwoHanded;
import mods.battleclasses.items.IHighDetailWeapon;
import mods.battlegear2.client.utils.BattlegearRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
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

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {

        if (itemRenderer == null) {
            itemRenderer = new RenderItem();
        }
        GL11.glPushMatrix();
        IIcon icon = itemStack.getIconIndex();
        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            
        	float scaleFactor = 1;
        	int defaultSize = 16;
        	if(icon.getIconWidth() > icon.getIconHeight()) {
        		scaleFactor = ((float)icon.getIconWidth())/ ((float)defaultSize);
        	}
        	else {
        		scaleFactor = ((float)icon.getIconHeight())/ ((float)defaultSize);
        	}

        	IHighDetailWeapon HD_weapon = (IHighDetailWeapon)itemStack.getItem();
        	//GL11.glTranslatef(-1F/scaleFactor - scaleFactor* HD_weapon.getRelativeAnchorPointX(), -1F/scaleFactor + scaleFactor* HD_weapon.getRelativeAnchorPointY(), 0);
        	//GL11.glTranslatef(-0.5F - HD_weapon.getRelativeAnchorPointX(), 0 - HD_weapon.getRelativeAnchorPointY(), 0);
        	//GL11.glTranslatef(0, -1F/scaleFactor + scaleFactor* HD_weapon.getRelativeAnchorPointY(), 0);
        	float scaleDifference = scaleFactor - 1;
        	GL11.glTranslatef( - 0.25F - (scaleDifference) + scaleFactor*HD_weapon.getRelativeAnchorPointX(), + 0.25F - scaleFactor*HD_weapon.getRelativeAnchorPointY(), 0);
            GL11.glScalef(scaleFactor, scaleFactor, 1);
            
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
        	float scaleFactor = 1;
        	int defaultSize = 16;
        	if(icon.getIconWidth() > icon.getIconHeight()) {
        		scaleFactor = ((float)defaultSize) / ((float)icon.getIconWidth());
        	}
        	else {
        		scaleFactor = ((float)defaultSize) / ((float)icon.getIconHeight());
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