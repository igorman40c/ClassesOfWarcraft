package mods.battleclasses.ability;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.client.BattleClassesClientTargeting;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownOwner;
import mods.battleclasses.enumhelper.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enumhelper.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enumhelper.EnumBattleClassesAbilityCastingType;
import mods.battleclasses.enumhelper.EnumBattleClassesCooldownType;
import mods.battleclasses.enumhelper.EnumBattleClassesAbilityDirectTargetRequirement;
import mods.battleclasses.gui.BattleClassesGuiHUDOverlay;
import mods.battleclasses.items.BattleClassesItemWeapon;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;

public abstract class BattleClassesAbstractAbilityActive extends BattleClassesAbstractAbilityCooldownHolder {

	public BattleClassesAbstractAbilityActive(int parAbilityID) {
		super(parAbilityID);
		abilityIconResourceLocation = new ResourceLocation("battleclasses", getAbilityIconPath() + getAbilityIconName() );
	}
	
	protected IIcon abilityIcon;
	public ResourceLocation abilityIconResourceLocation;
	
	//Basic ability parameters
	protected EnumBattleClassesAbilitySchool school = EnumBattleClassesAbilitySchool.UNKNOWN;
	protected EnumBattleClassesAbilityDirectTargetRequirement targetType = EnumBattleClassesAbilityDirectTargetRequirement.NEEDLESS;
	protected EnumBattleClassesAbilityIntent intent = EnumBattleClassesAbilityIntent.DUAL;
	protected EnumBattleClassesAbilityCastingType castingType = EnumBattleClassesAbilityCastingType.CastType_UNKNOWN;

	//Basic ability attributes
	public float castTime = 0;
	public float range = 40;
	
	//Supporting modifiers
	public float criticalChance = 0;
	public float haste = 0;
	
	protected int channelTickCount = 1;
	protected boolean channeled = false;
	public boolean ignoresGlobalCooldown = false;
	protected boolean ignoresSilence = false;
	protected boolean requiresMeleeSwing = false;
	protected int requiredItemLevel = 0;
	protected String name = "Unknown Ability";
	
	/**
	 * Called when player presses Mouse-Right button
	 */
	public void onCastStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(!this.isAvailable(entityPlayer, itemStack)) {
			this.cancelCasting(entityPlayer);
			return;
		}
		
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.SERVER) {
			BattleClassesUtils.getPlayerSpellBook(entityPlayer).setGlobalCooldown();
		}
		
		if(this.isInstant()) {
			this.requestProcession(entityPlayer, itemStack, 0);
		}
		else {
			this.startCasting(entityPlayer, itemStack);
		}
	}
	
	public void onCastTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		if(!this.channeled) {
			return;
		}
		int currentCastTick = tickCount - 72000;
		if(currentCastTick >= 0) {
			int ticksPerProceed = this.getCastTimeInTicks() / this.channelTickCount;
			int currentCastTickInverted = this.getCastTimeInTicks() - currentCastTick;
			if(currentCastTickInverted > 0 && (currentCastTickInverted % ticksPerProceed) == 0) {
				//Set To Cooldown on first channel tick
				if(currentCastTickInverted == ticksPerProceed) {
					BattleClassesUtils.Log("First Channel tick! Set CD here! CD should set: " + this.getCooldownClock().getDefaultDuration(), LogType.ABILITY);
					this.getCooldownClock().setCooldownDefault();
				}
				BattleClassesUtils.Log("Channeling... Current tick: " + currentCastTickInverted + " Cast time in tick " + this.getCastTimeInTicks(), LogType.ABILITY);
				this.requestProcession(entityPlayer, itemStack, tickCount);
			}
		}
	}
	
	public void onCastRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		int remainingCastTick = tickCount - 72000;
		if(remainingCastTick <= 0) {
			if(this.channeled) {
				return;
			}
			this.requestProcession(entityPlayer, itemStack, tickCount);
		}
		else {
			this.cancelCasting(entityPlayer);
		}	
	}
		
	public float getAbilityDamageBase() {
		//TODO
		return 0;
	}
	
	public float getAbilityDamageAmplified() {
		//TODO
		return 0;
	}
	
	protected void startCasting(EntityPlayer entityPlayer, ItemStack itemStack) {
		BattleClassesUtils.Log("Casting started!", LogType.ABILITY);
		BattleClassesUtils.setEntityPlayerItemInUseInSeconds(entityPlayer, itemStack, this.castTime);
	}
	
	public EnumBattleClassesAbilityDirectTargetRequirement getTargetingType() {
		return targetType;
	}
	
	public EnumBattleClassesAbilitySchool getSchool() {
		return this.school;
	}
	
	public int getChannelTicks() {
		return this.channelTickCount;
	}
	
	public boolean isChanneled() {
		return this.channeled;
	}
	
	/**
	 * Called on CastStart to check if the ability is available and ready to use
	 * @param entityPlayer - the caster player
	 * @param itemStack - the itemStack containing the held item
	 * @return - availability of the ability
	 */
	public boolean isAvailable(EntityPlayer entityPlayer, ItemStack itemStack) {
		boolean hasRequiredItem = false;
		if(itemStack.getItem() instanceof BattleClassesItemWeapon) {
			BattleClassesItemWeapon weapon = (BattleClassesItemWeapon) itemStack.getItem();
			if(weapon.getClassAccessSet().contains(BattleClassesUtils.getPlayerClass(entityPlayer)) ) {
				if(weapon.getItemAbilityLevel() >= this.requiredItemLevel) {
					hasRequiredItem = true;
				}
				else {
					BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_WEAPON_LOW_LEVEL);
					return false;
				}
			}
			else {
				BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_WEAPON_WRONG_CLASS);
				return false;
			}
		}
		
		boolean cooldownFree = !this.getCooldownClock().isOnCooldown();
		if(!cooldownFree) {
			BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_ON_COOLDOWN);
			return false;
		}
		
		boolean hasRequiredAmmo = true;
		if(!hasRequiredAmmo) {
			BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_WEAPON_NO_AMMO);
		}
		
		return hasRequiredItem && cooldownFree && hasRequiredAmmo;
	}
		
	public boolean requiresRayTracingForTarget() {
		switch (this.targetType) {
		case NEEDLESS:
			return false;
		case OPTIONAL:
			return true;
		case REQUIRED:
			return true;
		default:
			return false;
		}
	}
	
	public EntityLivingBase getFinalTargetFromRaytracedEntity(EntityLivingBase entity) {
		if(this.requiresRayTracingForTarget()) {
			boolean targetIsFriendly = BattleClassesUtils.isTargetFriendly(this.playerHooks.getOwnerPlayer(), entity);
			switch(this.intent) {
			case OFFENSIVE: {
				if(!targetIsFriendly) {
					return entity;
				}
			}
				break;
			case SUPPORTIVE: {
				switch(this.targetType) {
					case OPTIONAL: {
						if(targetIsFriendly) {
							return entity;
						}
						else {
							return this.playerHooks.getOwnerPlayer();
						}
					}
						//break;
					case REQUIRED: {
						if(targetIsFriendly) {
							return entity;
						}
					}
						break;
					default:
						break;
				}
			}
				break;
			case DUAL: {
				switch(this.targetType) {
					case OPTIONAL: {
						if(entity == null) {
							return this.playerHooks.getOwnerPlayer();
						}
						else {
							return entity;
						}
					}
						//break;
					case REQUIRED: {
						return entity;
					}
						//break;
					default:
						break;
				}
			}
				//break;
			default:
				break;
			}
		}
		return null;
	}
	
	public boolean isInstant() {
		return this.castTime == 0;
	}
	
	public int getCastTimeInTicks() {
		return (int) (this.castTime * 20);
	}
	
	public void cancelCasting(EntityPlayer entityPlayer) {
		this.playerHooks.playerClass.spellBook.cancelGlobalCooldown();
		entityPlayer.clearItemInUse();
		BattleClassesUtils.Log("Cancelling Casting and GlobalCD", LogType.ABILITY);
	}
	
	public void requestProcession(EntityPlayer entityPlayer, ItemStack itemStack, int tickCount) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		//Checking CLIENT SIDE
		if(side == Side.CLIENT) {
			if(!this.requiresRayTracingForTarget()) {
				return;
			}
			EntityLivingBase target = BattleClassesClientTargeting.getFinalTargetOfAbility(this);
			
			//CHECK TARGET REQUIREMENTS
			
			//CHECK RANGE REQUIREMENTS
			
			//IF REQUIREMENTS ARE SATISFIED, SEND PERFORMREQUEST PACKET
			
		}
		//Checking SERVER SIDE
		else {
			if(this.requiresRayTracingForTarget()) {
				return;
			}
			EntityLiving targetEntity = null;
			this.proceedAbility(targetEntity, tickCount);
		}
	}
	
	public boolean proceedAbility(EntityLiving targetEntity, int tickCount) {
		boolean performSucceeded = this.performEffect(targetEntity, tickCount);
		if(performSucceeded && !this.channeled) {
			this.onCastFinished(targetEntity, tickCount);
		}
		
		return performSucceeded;
	}
	
	public abstract boolean performEffect(EntityLiving targetEntity, int tickCount);
		
	public final void onCastFinished(EntityLiving targetEntity, int tickCount) {
		BattleClassesUtils.Log("Casting finished!", LogType.ABILITY);
		this.getCooldownClock().setCooldownDefault();
	}
	
	//Helper method
	public void setCastingType(EnumBattleClassesAbilityCastingType parCastType) {
		this.castingType = parCastType;
		switch(this.castingType) {
			case CastType_CASTED: {
				this.channeled = false;
			}
				break;
			case CastType_CHANNELED: {
				this.channeled = true;
			}
				break;
			case CastType_INSTANT: {
				this.channeled = false;
				this.castTime = 0;
			}
				break;
			default:
				break;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public String getAbilityIconName() {
		return "ability_" + this.abilityID + ".png";
	}
	
    @SideOnly(Side.CLIENT)
    public String getAbilityIconPath() {
    	return "textures/spells/icons/";
    }
    
    @SideOnly(Side.CLIENT)    
    public ResourceLocation getIconResourceLocation() {
    	return abilityIconResourceLocation;
    }
    
    @SideOnly(Side.CLIENT)
    public String getName() {
    	return name;
    }
    
    @SideOnly(Side.CLIENT)
    public float getCastPercentage(EntityPlayer entityPlayer) {
    	float f = 0;
    	float total = (float) this.getCastTimeInTicks();
    	float current = total - (float) (entityPlayer.getItemInUseCount() - 72000);
    	f = current/total;
    	if(this.channeled) {
    		f = 1.0F - f;
    	}
    	if(f > 1) {
    		f = 1;
    	}
    	if(f < 0) {
    		f = 0;
    	}
    	//System.out.println(current + "/" + total + " | F = " + f);
    	return f;
    }
	
}
