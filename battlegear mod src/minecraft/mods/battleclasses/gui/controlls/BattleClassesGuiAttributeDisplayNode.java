package mods.battleclasses.gui.controlls;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.core.BattleClassesWeaponHitHandler;
import mods.battleclasses.enums.EnumBattleClassesAttributeType;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeHooks;

public class BattleClassesGuiAttributeDisplayNode extends BattleClassesGuiButton {

	public static final ResourceLocation resourceAttributes = new ResourceLocation("battleclasses", "textures/gui/InterfaceAttributes.png");

	public static final int GUI_ELEMENT_WIDTH = 59;
	public static final int GUI_ELEMENT_HEIGHT = 13;
	
	public BattleClassesGuiAttributeDisplayNode(int id, int x, int y, EnumBattleClassesAttributeType type) {
		super(id, x, y, GUI_ELEMENT_WIDTH, GUI_ELEMENT_HEIGHT, "attributeDisplayNode");
		this.displayedAttributeType = type;
		this.showHoveringText = true;
		this.hoveringTextString = type.toString();
		this.origin_u = 112;
		this.origin_v = 0;
		this.setSize(GUI_ELEMENT_WIDTH, GUI_ELEMENT_HEIGHT);
	}

	protected EnumBattleClassesAttributeType displayedAttributeType;
	protected float displayAmmount;
	
	public void setDisplayedAmmount(float value) {
		this.displayAmmount = value;
	}
	
	public float getDisplayedAmmount() {
		return this.displayAmmount;
	}
	
	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int currentMousePosX, int currentMousePosY)
    {
        if (this.visible && displayedAttributeType!=null)
        {	
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(this.resourceAttributes);
            //InWindow
            this.field_146123_n = currentMousePosX >= this.xPosition && currentMousePosY >= this.yPosition && currentMousePosX < this.xPosition + this.width && currentMousePosY < this.yPosition + this.height;
            
            GL11.glPushMatrix();
    		GL11.glEnable(GL11.GL_BLEND);
    	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	    //GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    	    GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            //Drawing node background
            this.drawTexturedModalRect(	this.xPosition,
										this.yPosition,
										this.origin_u,
										this.origin_v,
										this.width,
										this.height);
            //Drawing attribute icon
            this.drawTexturedModalRect(	this.xPosition + 2,
            							this.yPosition + 2,
            							this.displayedAttributeType.getDisplayIconU(),
            							this.displayedAttributeType.getDisplayIconV(),
            							this.displayedAttributeType.getDisplayIconSquareSize(),
            							this.displayedAttributeType.getDisplayIconSquareSize());
            
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
            //Rendering value label of the attribute
            this.drawString(fontrenderer, this.getDisplayString(), this.xPosition + 15, this.yPosition + 2, l);

            this.renderHoveringText(currentMousePosX, currentMousePosY);
            
            //GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    @Override
    public List<String> getTooltipText() {
    	List<String> hoveringText = BattleClassesGuiHelper.createHoveringText();
    	BattleClassesGuiHelper.addTitle(hoveringText, StatCollector.translateToLocal(this.displayedAttributeType.getUnlocalizedName()));
    	BattleClassesGuiHelper.addParagraph(hoveringText, StatCollector.translateToLocal(this.displayedAttributeType.getUnlocalizedDescription()));
    	hoveringText = BattleClassesGuiHelper.formatHoveringTextWidth(hoveringText, 30);
    	return hoveringText;
    }
    
	public String getDisplayString() {
		Minecraft mc = Minecraft.getMinecraft();
		float displayedValue = 0;
		if(this.displayedAttributeType == EnumBattleClassesAttributeType.VANILLA_ARMOR) {
			displayedValue = ForgeHooks.getTotalArmorValue(mc.thePlayer);
		}
		else if(this.displayedAttributeType == EnumBattleClassesAttributeType.HEALTH) {
			IAttributeInstance iattributeinstance = mc.thePlayer.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
			displayedValue = (float) iattributeinstance.getAttributeValue();
		}
		else if(this.displayedAttributeType == EnumBattleClassesAttributeType.MELEE_ATTACK_DAMAGE) {
			BattleClassesWeaponHitHandler weaponHitHandler = BattleClassesUtils.getPlayerWeaponHandler(mc.thePlayer);
			BattleClassesAttributes mainHandAttributes = BattleClassesUtils.getPlayerAttributes(mc.thePlayer).getDisplayedAttributes();
			weaponHitHandler.mainHandAttackAbility.setWeaponDamageOnAttributes(mainHandAttributes);
			String valueString = BattleClassesGuiHelper.formatFloatToNiceTenths(mainHandAttributes.melee_attack_damage);
			if(BattleClassesUtils.getOffhandItemHeld(mc.thePlayer) != null && BattleClassesUtils.getOffhandItemHeld(mc.thePlayer).getItem() != null) {
				BattleClassesAttributes offHandAttributes = BattleClassesUtils.getPlayerAttributes(mc.thePlayer).getDisplayedAttributes();
				weaponHitHandler.offHandAttackAbility.setWeaponDamageOnAttributes(offHandAttributes);
				valueString += "|" + BattleClassesGuiHelper.formatFloatToNiceTenths(offHandAttributes.melee_attack_damage);
			}
			
			return valueString;
		}
		else {
			displayedValue = BattleClassesUtils.getPlayerAttributes(mc.thePlayer).getDisplayedAttributes().getValueByType(this.displayedAttributeType);
		}
		String valueString;
		if(this.displayedAttributeType.isDisplayedInPercentage()) {
			valueString = BattleClassesGuiHelper.formatFloatToNiceTenths(displayedValue * 100F);
			valueString += " %";
		}
		else {
			valueString = String.format("%.0f", displayedValue);
		}
		
		return valueString;
	}
}
