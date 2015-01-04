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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.client.BattleClassesClientTargeting;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battleclasses.enumhelper.EnumBattleClassesCooldownType;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
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
                
                AbilityActionBarPosX = event.xOffset+width/2;
                AbilityActionBarPosY = event.yOffset;
                renderAbilityActionBar(frame, event.xOffset+width/2, event.yOffset);

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
                        renderBlockBar(width / 2 - 91 + event.xOffset, height - 35 + event.yOffset);
                }

                ItemStack mainhand = mc.thePlayer.getCurrentEquippedItem();
                               
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
            this.drawTexturedModalRect(x + offsetX-1 + (mc.thePlayer.inventory.currentItem - InventoryPlayerBattle.OFFSET) * 20,
                    y - 1, 0, 22, 24, SLOT_H);
        }
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        if(mc.thePlayer!=null){
            for (int i = 0; i < 1 /*InventoryPlayerBattle.WEAPON_SETS*/; ++i) {
                int varx = x + i * 20 + 3 + offsetX;
                this.renderInventorySlot(i + InventoryPlayerBattle.OFFSET+(isMainHand?0:InventoryPlayerBattle.WEAPON_SETS), varx, y+3, frame);
            }
        }
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static final int ABILITY_ACTIONBAR_NODE_WIDTH = 20;
    public static final int ABILITY_ACTIONBAR_HEIGHT = 22;

    public void renderAbilityActionBar(float frame, int xOffset, int yOffset) {
    	ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);	
    	int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
    	ArrayList<BattleClassesAbstractAbilityActive> actionbarAbilities = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getActionbarAbilities();
    	if(actionbarAbilities.size() == 0) {
    		return;
    	}
    	int actionbarHeight = ABILITY_ACTIONBAR_HEIGHT;
    	int actionbarWidth = 1 + actionbarAbilities.size()*ABILITY_ACTIONBAR_NODE_WIDTH + 1;
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(resourceLocationHUD);
        
        int actionbarPosX = width/2 - actionbarWidth/2;
        int actionbarPosY = 0;
        int currentX = actionbarPosX;
        for(int i = 0; i < actionbarAbilities.size(); ++i) {
        	int drawNodeWith = ABILITY_ACTIONBAR_NODE_WIDTH;
        	int drawNodeHeight = ABILITY_ACTIONBAR_HEIGHT;
        	int u = 21;
        	int v = 0;
        	if(i == 0) {
        		++drawNodeWith;
        		u = 0;
            	v = 0;
        	}
        	if(i == (actionbarAbilities.size() - 1)) {
        		++drawNodeWith;
        		u += 20;
            	v = 0;
        	}
            this.drawTexturedModalRect(currentX, actionbarPosY, u, v, drawNodeWith, drawNodeHeight);
            currentX += drawNodeWith;
        }
        
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        
        for(int i = 0; i < actionbarAbilities.size(); ++i) {
        	this.drawAbilityIcon(actionbarPosX+3 + i*20, actionbarPosY+3, actionbarAbilities.get(i));
        }
        
        this.mc.renderEngine.bindTexture(resourceLocationHUD);
        boolean hasClass = BattleClassesUtils.getPlayerClass(mc.thePlayer) != EnumBattleClassesPlayerClass.NONE;
        if (hasClass && ((IBattlePlayer) mc.thePlayer).isBattlemode()) {
        	int chosenIndex = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbilityIndex();
            this.drawTexturedModalRect(actionbarPosX-1 + chosenIndex*20, actionbarPosY-1, 232, 0, 24, 24);
        }
        RenderHelper.enableGUIStandardItemLighting();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
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
            
            byte b0 = 12 + BattleClassesGuiHUDOverlay.ABILITY_ACTIONBAR_HEIGHT;	//MAIN Y coord
            
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
    
    public static final int CAST_BAR_WIDTH = 182;
    public static final int CAST_BAR_HEIGHT = 5;
    public static final int CAST_BAR_ZONE_HEIGHT = 18;
    public static final int CAST_BAR_LABEL_OFFSET = -10;
    
    public void drawCastbar() {
    	
    	if(BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).isCastingInProgress() ||
    			BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass.getCooldownClock().isOnCooldown()) {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    		int x = scaledresolution.getScaledWidth()/2 - CAST_BAR_WIDTH/2;
    		int y = 12 + BattleClassesGuiHUDOverlay.ABILITY_ACTIONBAR_HEIGHT;
    		if(this.shouldDrawBossHealthBar()) {
    			y += CAST_BAR_ZONE_HEIGHT;
    		}
    		int v = 48;
    		int u = 0;
    		int vStateOffset = 8;
    		String chosenAbilityName = "";
    		float f = 0;
    		if( BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility() != null) {
    			chosenAbilityName = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getName();
    			f = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getCastPercentage(mc.thePlayer);
    		} 
    		
    		if(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass.getCooldownClock().isOnCooldown()) {
    			chosenAbilit_HLL.hide();
    			f = 1.0F - BattleClassesUtils.getCooldownPercentage(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass);
    			if(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass.getCooldownClock().getLastUsedType() == EnumBattleClassesCooldownType.CooldownType_CLASS_SWITCH) {
    				chosenAbilityName = "Switching Class...";
    			}
    			if(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass.getCooldownClock().getLastUsedType() == EnumBattleClassesCooldownType.CooldownType_TALENT_CHANGE) {
    				chosenAbilityName = "Applying talent changes...";
    			}
    		}
    		else {
    			if(BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility() != null) {
        			v = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getSchool().getCastBarColoringV();
        		}
    		}
    		
    		int state = (int)(f * (float)CAST_BAR_WIDTH );
    		state = (state > CAST_BAR_WIDTH) ? CAST_BAR_WIDTH : state;
    		
    		this.mc.renderEngine.bindTexture(resourceLocationHUD);
    		
    		//DRAWING CASTBAR
            this.drawTexturedModalRect(x, y, u, v, CAST_BAR_WIDTH, CAST_BAR_HEIGHT);
            if (state > 0)
            {
                this.drawTexturedModalRect(x, y, u, v + vStateOffset, state, CAST_BAR_HEIGHT);
            }
            //DRAWING CHOSEN SPELL NAME
            FontRenderer fontrenderer = this.mc.fontRenderer;
            fontrenderer.drawStringWithShadow(chosenAbilityName,
            		scaledresolution.getScaledWidth()/2 - fontrenderer.getStringWidth(chosenAbilityName)/2,
            		y + CAST_BAR_LABEL_OFFSET, 0xFFFFFF);
            //DRAW CHOSEN SPELL ICON
            float castBarIconScale = 0.6F;
            if(BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass.getCooldownClock().isOnCooldown()) {
            	castBarIconScale = 1.0F;
            	this.drawAbilityIconCentered(x + CAST_BAR_WIDTH, y + CAST_BAR_HEIGHT/4,
						castBarIconScale, BattleClassesUtils.getPlayerHooks(mc.thePlayer).playerClass.getIconResourceLocation());
    		}
    		else {
    			if(BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility() != null) {
        			if(BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().isChanneled()) {
        				int channelTicks =  BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility().getChannelTicks();
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
            
            this.mc.getTextureManager().bindTexture(icons);
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
    
    public void drawAbilityIcon(int x, int y, BattleClassesAbstractAbilityActive ability ) {
    	if(ability != null && ability.getIconResourceLocation() != null) {
    		mc.getTextureManager().bindTexture(ability.getIconResourceLocation());
    		myDrawTexturedModalRect(x, y, 16, 16);
        	//drawTexturedModelRectFromIcon(x, y, ability.getAbilityIcon(), 16, 16);
        	drawCooldown(x, y, BattleClassesUtils.getCooldownPercentage(ability));
        	
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
    		
    		myDrawTexturedModalRect(scaledPosX - iconScaledWidth/2, scaledPosY - iconScaledHeight/2, 16, 16);
    		
    		GL11.glScalef(1, 1, 1);
    		GL11.glPopMatrix();
        	
    	}
    }
	
    public void drawCooldown(int posX, int posY, float f) {
    	if(f > 0) {
    		int frameIndex = BattleClassesGuiHelper.cooldownIcons.length-1;
    		if(f < 1) {
    			frameIndex = (int) (((float) BattleClassesGuiHelper.cooldownIcons.length)*f);
    		}
    		IIcon cooldownIcon = BattleClassesGuiHelper.cooldownIcons[frameIndex];
    	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
    	    
            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
    		this.drawTexturedModelRectFromIcon(posX, posY, cooldownIcon, cooldownIcon.getIconWidth(), cooldownIcon.getIconHeight());
    		
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	}
	}
    
	 // 3.  You'll need to write your own version of the Gui.drawTexturedModalRect() method
	//  This method can go into your own Gui class:
	public void myDrawTexturedModalRect(int x, int y, int width, int height)
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
		
		int y = 12 + BattleClassesGuiHUDOverlay.ABILITY_ACTIONBAR_HEIGHT;
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

	public static final String HUD_W_CLASS_REQUIRED = "You need to choose a class to use that!";
	public static final String HUD_W_BATTLEMODE_REQUIRED = "You need to be in Battle mode to use that!";
	public static final String HUD_W_ON_COOLDOWN = "That ability is not ready yet!";
	public static final String HUD_W_ON_CLASS_COOLDOWN = "Your class is not ready yet!";
	public static final String HUD_W_WEAPON_WRONG_CLASS = "That weapon is not for your class!";
	public static final String HUD_W_WEAPON_LOW_LEVEL = "That ability requires a better weapon'";
	public static final String HUD_W_WEAPON_NO_AMMO = "That ability requires ammo!";
	
	public static void displayWarning(String message) {
		warningDisplay_HLL.setText(message);
		warningDisplay_HLL.show();
	}
	
	public static void displayChosenAbilityName(String message) {
		chosenAbilit_HLL.setText(message);
		chosenAbilit_HLL.show();
	}
	
	public static void displayTargetInfo(String message) {
		targetDisplay_HLL.setText(message);
		targetDisplay_HLL.show();
	}
}
