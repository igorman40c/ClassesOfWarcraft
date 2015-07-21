package mods.battleclasses.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.active.BattleClassesAbilityShieldBlock;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.client.BattleClassesClientTargeting;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battleclasses.core.BattleClassesWeaponHitHandler;
import mods.battleclasses.core.CooldownClock;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.tab.BattleClassesTabSpellbook;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.RenderItemBarEvent;
import mods.battlegear2.api.core.IBattlePlayer;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import mods.battlegear2.api.quiver.IArrowContainer2;
import mods.battlegear2.api.quiver.QuiverArrowRegistry;
import mods.battlegear2.api.shield.IShield;
import mods.battlegear2.client.BattlegearClientEvents;
import mods.battlegear2.client.BattlegearClientTickHandeler;
import mods.battlegear2.client.ClientProxy;
import mods.battlegear2.client.gui.BattlegearInGameGUI;

public class BattleClassesGuiHUDOverlay extends BattlegearInGameGUI {
	
    public static Class<?> previousGui;
    public static Minecraft mc;
    public static final ResourceLocation resourceLocationHUD = new ResourceLocation("battleclasses", "textures/gui/InterfaceHUD.png");
        
    public BattleClassesGuiHUDOverlay() {
    	super();
    	this.initHighLightLabels();
    	mc = Minecraft.getMinecraft();
    }
    
    public int AbilityActionBarPosX = 0;
    public int AbilityActionBarPosY = 0;
	
    public void renderGameOverlay(float frame, int mouseX, int mouseY) {
    	
        if(Battlegear.battlegearEnabled){
            ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();
            RenderGameOverlayEvent renderEvent = new RenderGameOverlayEvent(frame, scaledresolution, mouseX, mouseY);

            if (!this.mc.playerController.enableEverythingIsScrewedUpMode()) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                zLevel = -90.0F;

                RenderItemBarEvent event = new RenderItemBarEvent.BattleSlots(renderEvent, true);
                if(!(mc.currentScreen instanceof BattleClassesTabSpellbook)) {
                    AbilityActionBarPosX = event.xOffset+width/2;
                    AbilityActionBarPosY = event.yOffset;
                    renderAbilityActionBar(frame, event.xOffset+width/2, event.yOffset);
                }

                if(!MinecraftForge.EVENT_BUS.post(event)){
                    renderBattleSlots(width / 2 + 121 + event.xOffset, height - 22 + event.yOffset, frame, true);
                }
                event = new RenderItemBarEvent.BattleSlots(renderEvent, false);
                if(!MinecraftForge.EVENT_BUS.post(event)){
                    renderBattleSlots(width / 2 - 184 + event.xOffset, height - 22 + event.yOffset, frame, false);
                }

                ItemStack offhand = ((InventoryPlayerBattle) mc.thePlayer.inventory).getCurrentOffhandWeapon();
                if(offhand!= null && offhand.getItem() instanceof IShield){
                    event = new RenderItemBarEvent.ShieldBar(renderEvent, offhand);
                    if(!MinecraftForge.EVENT_BUS.post(event))
                        super.renderBlockBar(width / 2 - 91 + event.xOffset, height - 45 + event.yOffset);
                }

                ItemStack mainhand = mc.thePlayer.getCurrentEquippedItem();
                
                this.drawWeaponCooldowns();
                BattleClassesClientTargeting.generateTargetingInfo();
            }
        }
    }
    
    public void renderBattleSlots(int x, int y, float frame, boolean isMainHand) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                
        this.mc.renderEngine.bindTexture(resourceLocationHUD);
        int offsetX = SLOT_H;
        drawTexturedModalRect(x + offsetX, y, 180, 0, SLOT_H, SLOT_H);

        this.mc.renderEngine.bindTexture(resourceLocation);
        if (mc.thePlayer!=null && ((IBattlePlayer) mc.thePlayer).isBattlemode()) {
            this.drawTexturedModalRect(x + offsetX-1 + 0 * 20,
                    y - 1, 0, 22, 24, SLOT_H);
        }
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        if(mc.thePlayer!=null){
            for (int i = 0; i < 1 /*InventoryPlayerBattle.WEAPON_SETS*/; ++i) {
                int varx = x + i * 20 + 3 + offsetX;
                this.renderInventorySlot(i + InventoryPlayerBattle.OFFSET+(isMainHand?0:InventoryPlayerBattle.WEAPON_SETS), varx, y+3, frame);
                
                /*
                 * DRAWING HAND COOLDOWNS HERE
                 */
                BattleClassesWeaponHitHandler weaponHitHandler = BattleClassesUtils.getPlayerWeaponHandler(this.mc.thePlayer);
                if(weaponHitHandler != null) {
                	CooldownClock handClock;
                	if(isMainHand) {
                		handClock = weaponHitHandler.mainHandAttackAbility.getCooldownClock();
                	}
                	else {
                		handClock = weaponHitHandler.offHandAttackAbility.getCooldownClock();
                	}
                	BattleClassesGuiHelper.INSTANCE.drawCooldown(varx, y+3, BattleClassesUtils.getCooldownPercentage(handClock));
                }
            }
        }
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    

    public void renderAbilityActionBar(float frame, int xOffset, int yOffset) {
    	ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);	
    	int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
    	ArrayList<BattleClassesAbstractAbilityActive> actionbarAbilities = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getActionbarAbilities();
    	if(actionbarAbilities.size() == 0) {
    		return;
    	}

    	int centerX = width/2;
    	int actionbarPosX = centerX - BattleClassesGuiHelper.getActionBarWidth(actionbarAbilities.size())/2;
        int actionbarPosY = 0;
    	BattleClassesGuiHelper.INSTANCE.drawActionbarBackgroundCentered(centerX, actionbarPosY, actionbarAbilities.size());
    	
        for(int i = 0; i < actionbarAbilities.size(); ++i) {
        	BattleClassesGuiHelper.INSTANCE.drawAbilityIcon(actionbarPosX+3 + i*20, actionbarPosY+3, actionbarAbilities.get(i));
        }
        BattleClassesGuiHelper.INSTANCE.drawAbilitySelector(actionbarPosX, actionbarPosY);
    }
    
    public void drawWeaponCooldowns() {
    	
    }
    
    protected boolean shouldDrawBossHealthBar() {
    	return (BossStatus.bossName != null && BossStatus.statusBarTime > 0);
    }
    
    public void drawBossHealth()
    {
        if (shouldDrawBossHealthBar())
        {
            --BossStatus.statusBarTime;
            FontRenderer fontrenderer = this.mc.fontRenderer;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            short short1 = 182;
            int j = i / 2 - short1 / 2;
            int k = (int)(BossStatus.healthScale * (float)(short1 + 1));
            
            byte b0 = 12 + BattleClassesGuiHelper.ABILITY_ACTIONBAR_HEIGHT;	//MAIN Y coord
            
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);

            if (k > 0)
            {
                this.drawTexturedModalRect(j, b0, 0, 79, k, 5);
            }

            String s = BossStatus.bossName;
            fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, b0 - 10, 16777215);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }
    
    public void renderBlockBar(int x, int y) {
    	GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(resourceLocationShield);
        
        /*
        if(mc.thePlayer!=null){
            if(mc.thePlayer.capabilities.isCreativeMode){
                if(mc.thePlayer.isRidingHorse()){
                    y-=5;
                }
            }else{
                y-= 16;
                if(ForgeHooks.getTotalArmorValue(mc.thePlayer) > 0 || mc.thePlayer.isRidingHorse() || mc.thePlayer.getAir() < 300){
                    y-=10;
                }
            }
        }
        */

        this.drawTexturedModalRect(x, y, 0, 0, 182, 9);

        float[] colour = COLOUR_DEFAULT;
        if(BattlegearClientTickHandeler.getBlockTime() < 0.33F){
            colour = COLOUR_RED;
        }
        if(BattlegearClientTickHandeler.getFlashTimer() > 0 && (System.currentTimeMillis() / 250) % 2 == 0){
            colour = COLOUR_YELLOW;
        }
        GL11.glColor3f(colour[0], colour[1], colour[2]);
        this.drawTexturedModalRect(x, y, 0, 9, (int) (182 * BattlegearClientTickHandeler.getBlockTime()), 9);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
    
    public static final int CAST_BAR_WIDTH = 182;
    public static final int CAST_BAR_HEIGHT = 5;
    public static final int CAST_BAR_ZONE_HEIGHT = 18;
    public static final int CAST_BAR_LABEL_OFFSET = -10;
    
    public static void finishDrawing() {
    	mc.getTextureManager().bindTexture(icons);
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }
    
    public void drawCastbar() {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    	int x = scaledresolution.getScaledWidth()/2 - CAST_BAR_WIDTH/2;
		int y = 12 + BattleClassesGuiHelper.ABILITY_ACTIONBAR_HEIGHT;
		if(this.shouldDrawBossHealthBar()) {
			y += CAST_BAR_ZONE_HEIGHT;
		}
		boolean shouldDisplayBarString = false;
		String barDisplayString = "";
		
    	//DRAWING CLASS COOLDOWN
		if(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass.getCooldownClock().isOnCooldown()) {
			int v = 48;
    		int u = 0;
			chosenAbilit_HLL.hide();
			float f = 0;
			f = 1.0F - BattleClassesUtils.getCooldownPercentage(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass);
			shouldDisplayBarString = true;
			if(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass.getCooldownClock().getLastUsedType() == EnumBattleClassesCooldownType.CooldownType_CLASS_SWITCH) {
				barDisplayString = StatCollector.translateToLocal("bchud.info.changeclass");
			}
			if(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass.getCooldownClock().getLastUsedType() == EnumBattleClassesCooldownType.CooldownType_TALENT_CHANGE) {
				barDisplayString = StatCollector.translateToLocal("bchud.info.changetalents");
			}
			//DRAWING PROGRESSBAR	
    		this.renderProgressBar(x, y, u, v, f);
    		//DRAWING ICON TO THE END OF THE PROGRESSBAR
    		float castBarIconScale = 1.0F;
        	this.drawAbilityIconCentered(x + CAST_BAR_WIDTH, y + CAST_BAR_HEIGHT/4,
					castBarIconScale, BattleClassesUtils.getPlayerClassEnum(mc.thePlayer).getIconResourceLocation());
		}
		//DRAWING BLOCKBAR
		else if(BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility() != null &&
    		BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getAbilityID().equals(BattleClassesAbilityShieldBlock.INSTANCE.getAbilityID())) {
    		ItemStack offhand = ((InventoryPlayerBattle) mc.thePlayer.inventory).getCurrentOffhandWeapon();
            if(offhand!= null && offhand.getItem() instanceof IShield){
            	shouldDisplayBarString = true;
            	barDisplayString = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getTranslatedName();
            	renderBlockBar(x,y);
            }
    	}
		//DRAWING CASTBAR
    	else if(BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).isCastingInProgress()) {
    		int v = 48;
    		int u = 0;
    		shouldDisplayBarString = true;
    		float f = 0;
    		BattleClassesAbstractAbilityActive chosenAbility = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility();
    		if(chosenAbility != null) {
    			barDisplayString = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getTranslatedName();
    			f = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getCastPercentage(mc.thePlayer);
    			v = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getSchool().getCastBarColoringV();
    			
    			//DRAWING PROGRESSBAR
    			if(chosenAbility.isChanneled()) {
    				f = 1.0F - f;
    			}
        		this.renderProgressBar(x, y, u, v, f);
                
                //DRAW SPELL ICON ON THE PROGRESSBAR
                float castBarIconScale = 0.6F;
                if(chosenAbility.isChanneled()) {
    				int channelTicks =  chosenAbility.getChannelTicks();
    				for(int i = 0; i < channelTicks; ++i) {
    					this.drawAbilityIconCentered(x + CAST_BAR_WIDTH - ((i+1)*(CAST_BAR_WIDTH/(channelTicks)) ), y + CAST_BAR_HEIGHT/4,
        						castBarIconScale, BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getIconResourceLocation());
    				}
    			}
    			else {
    				this.drawAbilityIconCentered(x + CAST_BAR_WIDTH, y + CAST_BAR_HEIGHT/4,
    						castBarIconScale, BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getIconResourceLocation());
    			}
    		} 
    	}
		//DRAWING TITLE
    	if(shouldDisplayBarString) {
            FontRenderer fontrenderer = this.mc.fontRenderer;
            fontrenderer.drawStringWithShadow(barDisplayString,
            		scaledresolution.getScaledWidth()/2 - fontrenderer.getStringWidth(barDisplayString)/2,
            		y + CAST_BAR_LABEL_OFFSET, 0xFFFFFF);
        }
    	this.mc.getTextureManager().bindTexture(icons);
    }
    
    protected void renderProgressBar(int x, int y, int u, int v, float fState) {
    	int state = (int)(fState * (float)CAST_BAR_WIDTH );
		state = (state > CAST_BAR_WIDTH) ? CAST_BAR_WIDTH : state;
		int vOffset = 8;
		this.mc.renderEngine.bindTexture(resourceLocationHUD);
        this.drawTexturedModalRect(x, y, u, v, CAST_BAR_WIDTH, CAST_BAR_HEIGHT);
        if (state > 0)
        {
            this.drawTexturedModalRect(x, y, u, v + vOffset, state, CAST_BAR_HEIGHT);
        }
    }

    private void renderInventorySlot(int par1, int par2, int par3, float par4) {
        ItemStack itemstack = this.mc.thePlayer.inventory.getStackInSlot(par1);
        renderStackAt(par2, par3, itemstack, par4);
    }

    private void renderStackAt(int x, int y, ItemStack itemstack, float frame){
        if (itemstack != null) {
            float f1 = (float) itemstack.animationsToGo - frame;

            if (f1 > 0.0F) {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float) (x + 8), (float) (y + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, x, y);
            if (f1 > 0.0F) {
                GL11.glPopMatrix();
            }
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, x, y);
        }
    }
        
    public void drawAbilityIconCentered(int x, int y, float scale, ResourceLocation iconResourceLocation ) {
    	if(iconResourceLocation != null) {
    		mc.getTextureManager().bindTexture(iconResourceLocation);
    		
    		GL11.glPushMatrix();
    		GL11.glScalef(scale, scale, scale);
    		GL11.glEnable (GL11.GL_BLEND);
    		GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    		int iconScaledWidth = (int) (scale * 16);
    		int iconScaledHeight = (int) (scale * 16);
    		int scaledPosX = (int) ( ((float)x) / scale );
    		int scaledPosY = (int) ( ((float)y) / scale );
    		
    		drawTexturedRectFromCustomSource(scaledPosX - iconScaledWidth/2, scaledPosY - iconScaledHeight/2, 16, 16);
    		
    		GL11.glScalef(1, 1, 1);
    		GL11.glPopMatrix();
        	
    	}
    }
	
    /*
    public void drawCooldown(int posX, int posY, float f) {
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
    		this.drawTexturedModelRectFromIcon(posX, posY, cooldownIcon, cooldownIcon.getIconWidth(), cooldownIcon.getIconHeight());
    		
    		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);    		    
	        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glPopMatrix();
      	}
	}
	*/
    
	 // 3.  You'll need to write your own version of the Gui.drawTexturedModalRect() method
	//  This method can go into your own Gui class:
	public void drawTexturedRectFromCustomSource(int x, int y, int width, int height)
	{
		 Tessellator tessellator = Tessellator.instance;
		 tessellator.startDrawingQuads();    
		 tessellator.addVertexWithUV(x        , y + height, (double)this.zLevel, 0.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y + height, (double)this.zLevel, 1.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y         , (double)this.zLevel, 1.0, 0.0);
		 tessellator.addVertexWithUV(x        , y         , (double)this.zLevel, 0.0, 0.0);
		 tessellator.draw();
	}
	
	public static GuiHighLightLabel chosenAbilit_HLL = new GuiHighLightLabel();
	public static GuiHighLightLabel targetDisplay_HLL = new GuiHighLightLabel();
	public static GuiHighLightLabel warningDisplay_HLL = new GuiHighLightLabel();
	
	public void initHighLightLabels() {
        targetDisplay_HLL.horizontalAlignmentMode = 1;
        targetDisplay_HLL.setVisibleDuration(0.5F);
		warningDisplay_HLL.horizontalAlignmentMode = 1;
		warningDisplay_HLL.setColorHEX(0xFF0000);
		chosenAbilit_HLL.horizontalAlignmentMode = 1;
	}
	
	public void drawHighLightedLabels() {
		ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int centerGap = 5;
		targetDisplay_HLL.posX = scaledresolution.getScaledWidth() / 2; // + centerGap;
		targetDisplay_HLL.posY = scaledresolution.getScaledHeight() / 2 - centerGap - mc.fontRenderer.FONT_HEIGHT;
		warningDisplay_HLL.posX = scaledresolution.getScaledWidth() / 2; // - centerGap;
		warningDisplay_HLL.posY = scaledresolution.getScaledHeight() / 2 + centerGap;
		
		int y = 12 + BattleClassesGuiHelper.ABILITY_ACTIONBAR_HEIGHT;
		if(this.shouldDrawBossHealthBar()) {
			y += CAST_BAR_ZONE_HEIGHT;
		}
		y += CAST_BAR_LABEL_OFFSET;
		chosenAbilit_HLL.posX = scaledresolution.getScaledWidth() / 2;
		chosenAbilit_HLL.posY = y;
		
		targetDisplay_HLL.draw(mc.fontRenderer);
		warningDisplay_HLL.draw(mc.fontRenderer);
		chosenAbilit_HLL.draw(mc.fontRenderer);
	}

	public static final String HUD_W_CLASS_REQUIRED = "bchud.warning.class_required";
	public static final String HUD_W_BATTLEMODE_REQUIRED = "bchud.warning.battlemode_required";
	public static final String HUD_W_WEAPON_AMMO_REQUIRED = "bchud.warning.ammo_required";
	public static final String HUD_W_ON_COOLDOWN = "bchud.warning.on_cooldown";
	public static final String HUD_W_ON_CLASS_COOLDOWN = "bchud.warning.on_classcooldown";
	public static final String HUD_W_TARGET_OUT_OF_RANGE = "bchud.warning.target_out_of_range";
	public static final String HUD_W_SILENCED = "bchud.warning.silenced";
	public static final String HUD_W_COMBAT_DISABLED = "bchud.warning.combat_disabled";
	public static final String HUD_W_WEAPON_WRONG_CLASS = "bchud.warning.weapon_wrong_class";
	
	public static void displayWarning(String message) {
		warningDisplay_HLL.setTranslatedText(message);
		warningDisplay_HLL.show();
	}
	
	public static void displayChosenAbilityName(String message) {
		chosenAbilit_HLL.setTranslatedText(message);
		chosenAbilit_HLL.show();
	}
	
	public static void displayTargetInfo(String message) {
		targetDisplay_HLL.setTranslatedText(message);
		targetDisplay_HLL.show();
	}
}
