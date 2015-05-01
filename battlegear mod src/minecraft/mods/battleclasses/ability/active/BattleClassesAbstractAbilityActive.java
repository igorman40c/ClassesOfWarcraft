package mods.battleclasses.ability.active;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.effect.BattleClassesAbstractAbilityEffect;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.client.BattleClassesClientTargeting;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownOwner;
import mods.battleclasses.enums.EnumBattleClassesAbilityCastingType;
import mods.battleclasses.enums.EnumBattleClassesAbilityDirectTargetRequirement;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.gui.BattleClassesGuiHUDOverlay;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battleclasses.items.BattleClassesItemWeapon;
import mods.battleclasses.packet.BattleClassesPacketCastingSound;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battleclasses.packet.BattleClassesPacketProcessAbilityWithTarget;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.core.InventoryPlayerBattle;

public abstract class BattleClassesAbstractAbilityActive extends BattleClassesAbstractAbilityCooldownHolder {

	public BattleClassesAbstractAbilityActive(int parAbilityID) {
		super(parAbilityID);
		this.setCastingType(EnumBattleClassesAbilityCastingType.CastType_INSTANT);
	}
	
	protected IIcon abilityIcon;
	public ResourceLocation abilityIconResourceLocation;
	
	//Basic ability parameters
	protected EnumBattleClassesAbilitySchool school = EnumBattleClassesAbilitySchool.UNKNOWN;
	protected EnumBattleClassesAbilityDirectTargetRequirement targetRequirementType = EnumBattleClassesAbilityDirectTargetRequirement.NEEDLESS;
	protected EnumBattleClassesAbilityIntent intent = EnumBattleClassesAbilityIntent.DUAL;
	
	//Use process parameters
	private AbilityUseProcessor useProcessor;
	private EnumBattleClassesAbilityCastingType castingType;
	protected int channelTickCount = 1;
	public float castTime = 0;
	
	//Supporting modifiers
	public float range = 40;
	public float criticalChance = 0;
	public float haste = 0;
	
	public boolean ignoresGlobalCooldown = false;
	protected boolean ignoresSilence = false;
	protected boolean requiresMeleeSwing = false;
	protected int requiredItemLevel = 0;
	
	public EnumAction getEnumActionForCasting() {
		return EnumAction.bow;
	}
	
	public EnumBattleClassesAbilityCastingType getCastingType() {
		return this.castingType;
	}
	
	protected void setCastingType(EnumBattleClassesAbilityCastingType castingType) {
		this.castingType = castingType;
		this.useProcessor = AbilityUseProcessor.INSTANCE.createUseProcessor(castingType, this);
	}
	
	//----------------------------------------------------------------------------------
	//							SECTION - Usage Process
	//----------------------------------------------------------------------------------
	//----------------------------------------
	//SUB-SECTION - Start Use
	//----------------------------------------
	/**
	 * Called on startUse to check if the ability is available and ready to use
	 * @param entityPlayer - the caster player
	 * @param itemStack - the itemStack containing the held item
	 * @return - availability of the ability
	 */
	public boolean isAvailable(EntityPlayer entityPlayer, ItemStack itemStack) {
		//boolean hasRequiredItem = false;
		if(itemStack.getItem() instanceof BattleClassesItemWeapon) {
			BattleClassesItemWeapon weapon = (BattleClassesItemWeapon) itemStack.getItem();
			if(weapon.getClassAccessSet().contains(BattleClassesUtils.getPlayerClassEnum(entityPlayer)) ) {
				/*
				if(weapon.getItemAbilityLevel() >= this.requiredItemLevel) {
					hasRequiredItem = true;
				}
				else {
					BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_WEAPON_LOW_LEVEL);
					return false;
				}
				*/
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
			BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_WEAPON_AMMO_REQUIRED);
		}
		
		return /*hasRequiredItem &&*/ cooldownFree && hasRequiredAmmo;
	}
	
	/**
	 * Called when player presses Mouse-Right button
	 */
	public final void startUse(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(!this.isAvailable(entityPlayer, itemStack)) {
			this.cancelCasting(entityPlayer);
			return;
		}
		this.useProcessor.onUseStart(itemStack, world, entityPlayer);
	}
		
	//----------------------------------------
	//SUB-SECTION - Tick Use
	//----------------------------------------
	/**
	 * Called while player keeps Mouse-Right button pressed down. Hook method to be overriden.
	 */
	public final void tickUse(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		this.useProcessor.onUseTick(itemStack, entityPlayer, tickCount);
	}
	
	//----------------------------------------
	//SUB-SECTION - Release Use
	//----------------------------------------
	/**
	 * Called when player releases Mouse-Right button
	 */
	public final void releaseUse(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		sendCastingSoundPacket(false);
		this.useProcessor.onUseRelease(itemStack, entityPlayer, tickCount);
	}
		
	//----------------------------------------
	//SUB-SECTION - Finish Use
	//----------------------------------------
	
	protected void requestUseFinishAndTarget(EntityPlayer entityPlayer, ItemStack itemStack, int tickCount) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		//Checking CLIENT SIDE
		if(side == Side.CLIENT && this.requiresRayTracingForTarget()) {			
			//TODO
			//CHECK TARGET REQUIREMENTS
			//CHECK RANGE REQUIREMENTS
			EntityLivingBase target = this.getFinalTarget();
			
			if (target != null) {
				int targetEntityID = target.getEntityId();
				FMLProxyPacket p = new BattleClassesPacketProcessAbilityWithTarget(entityPlayer, this.abilityID, targetEntityID, tickCount).generatePacket();
				BattleClassesMod.packetHandler.sendPacketToServer(p);
			}
			else {
				EntityLivingBase farTarget = getTargetFromExtendedRange();
				if(farTarget != null) {
					BattleClassesGuiHUDOverlay.displayWarning(BattleClassesGuiHUDOverlay.HUD_W_TARGET_OUT_OF_RANGE);
				}
			}
		}
		//Checking SERVER SIDE
		else if (side == Side.SERVER && !this.requiresRayTracingForTarget()) {
			EntityLivingBase targetEntity = null;
			this.finishUseWithTarget(targetEntity, tickCount);
		} 
		else {
			System.out.println("ServerSide requestProcession failed. TargetRT: " + this.targetRequirementType);
		}
	}
	
	public boolean finishUseWithTarget(EntityLivingBase targetEntity, int tickCount) {
		boolean releaseSucceeded = this.releaseEffects(targetEntity, tickCount);
		if(releaseSucceeded) {
			this.useProcessor.onUseFinished(targetEntity, tickCount);
		}
		
		return releaseSucceeded;
	}
		
	//----------------------------------------------------------------------------------
	//							SECTION - Casting
	//----------------------------------------------------------------------------------
	
	protected void startCasting(EntityPlayer entityPlayer, ItemStack itemStack) {
		BattleClassesUtils.Log("Casting started!", LogType.ABILITY);
		BattleClassesUtils.setEntityPlayerItemInUseInSeconds(entityPlayer, itemStack, this.getCastTime());
		sendCastingSoundPacket(true);
	}
	
	public void cancelCasting(EntityPlayer entityPlayer) {
		if(!this.ignoresGlobalCooldown) {
			this.playerHooks.playerClass.spellBook.cancelGlobalCooldown();
		}
		entityPlayer.clearItemInUse();
		sendCastingSoundPacket(false);
		BattleClassesUtils.Log("Cancelling Casting and GlobalCD", LogType.ABILITY);
	}
	
	public int getCastTimeInTicks() {
		return (int) (this.getCastTime() * 20);
	}
	
	//----------------------------------------------------------------------------------
	//							SECTION - Targeting
	//----------------------------------------------------------------------------------
	
	@SideOnly(Side.CLIENT)
	public EntityLivingBase getTargetFromExtendedRange() {
		return BattleClassesClientTargeting.getClientMouseOverTarget(this.range*1.5f);
	}
	
	/**
	 * Returns the FinalTargetFromRaytracedEntity using a newly traced mouse-over target
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public EntityLivingBase getFinalTarget() {
		EntityLivingBase target = BattleClassesClientTargeting.getClientMouseOverTarget(this.range);
		EntityLivingBase finalTarget = this.getFinalTargetFromRaytracedEntity(target);
		return finalTarget;
	}
	
	/**
	 * Decides if the ability requires a direct raytraced based on the target requirement type 
	 * @return
	 */
	public boolean requiresRayTracingForTarget() {
		switch (this.targetRequirementType) {
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
	
	/**
	 * Decides between the raytraced entity and the caster to be the final target depending on the intent of the ability.
	 * @param entity - raytraced
	 * @return final target
	 */
	public EntityLivingBase getFinalTargetFromRaytracedEntity(EntityLivingBase entity) {
		if(this.requiresRayTracingForTarget()) {
			boolean targetIsFriendly = BattleClassesUtils.isTargetFriendly(this.getOwnerPlayer(), entity);
			switch(this.intent) {
			case OFFENSIVE: {
				if(!targetIsFriendly) {
					return entity;
				}
			}
				break;
			case SUPPORTIVE: {
				switch(this.targetRequirementType) {
					case OPTIONAL: {
						if(targetIsFriendly) {
							return entity;
						}
						else {
							return this.getOwnerPlayer();
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
				switch(this.targetRequirementType) {
					case OPTIONAL: {
						if(entity == null) {
							return this.getOwnerPlayer();
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
	
	//----------------------------------------------------------------------------------
	//								SECTION - Effects 
	//----------------------------------------------------------------------------------
	
	protected ArrayList<BattleClassesAbstractAbilityEffect> effects = new ArrayList<BattleClassesAbstractAbilityEffect>();

	public void addEffect(BattleClassesAbstractAbilityEffect effect) {
		effect.setParentAbility(this);
		this.effects.add(effect);
	}
	
	public void removeEffect(BattleClassesAbstractAbilityEffect effect) {
		effect.setParentAbility(null);
		this.effects.remove(effect);
	}
	
	public void performEffects(EntityLivingBase targetEntity) {
		performEffects(targetEntity, 1);
	}
	
	public void performEffects(EntityLivingBase targetEntity, float partialMultiplier) {
		//TODO
		BattleClassesAttributes attributesForParentAbility = this.getPlayerAttributes().getTotalAttributesForAbility(this);
		float critChance = attributesForParentAbility.crit;
		BattleClassesAbstractAbilityEffect.performListOfEffects(this.effects, attributesForParentAbility, critChance, partialMultiplier, this.getOwnerPlayer(), targetEntity);
	}
	
	/**
	 * Called on finished ability use to release the effects
	 * @param targetEntity - onMouse target of the player (available on server side too)
	 * @param tickCount - current state of the casting in ticks
	 * @return boolean value if the release was succesfuly performed or not
	 */
	protected abstract boolean releaseEffects(EntityLivingBase targetEntity, int tickCount);
	
	//----------------------------------------------------------------------------------
	//							SECTION - Getters & helpers
	//----------------------------------------------------------------------------------
		
	public int getChannelTicks() {
		return this.channelTickCount;
	}
	
	public boolean isIgnoringSilence() {
		return this.ignoresSilence;
	}
		
	public EnumBattleClassesAbilityDirectTargetRequirement getTargetingType() {
		return this.targetRequirementType;
	}
	
	public EnumBattleClassesAbilitySchool getSchool() {
		return this.school;
	}
	
	public boolean isInstant() {
		return this.getCastTime() == 0;
	}
	
	public boolean isChanneled() {
		return this.castingType == EnumBattleClassesAbilityCastingType.CastType_CHANNELED;
	}
	
	public float getCastTime() {
    	return this.castTime;
    }
	
	@Override
    public BattleClassesAbstractAbilityActive setUnlocalizedName(String parName) {
		super.setUnlocalizedName(parName);
		abilityIconResourceLocation = BattleClassesGuiHelper.getResourceLocationOfTexture("textures/spells/icons/", this.getUnlocalizedIconName() + ".png");
		return this;
	}
	
	//----------------------------------------------------------------------------------
	//							SECTION - Client side stuff
	//----------------------------------------------------------------------------------
			
    @SideOnly(Side.CLIENT)
    public boolean hasItemIcon() {
    	return false;
    }
    
    @SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		return null;
	}
    
    @SideOnly(Side.CLIENT)    
    public ResourceLocation getIconResourceLocation() {
    	return abilityIconResourceLocation;
    }
    
    @SideOnly(Side.CLIENT)
    public String getName() {
    	return unlocalizedName;
    }
    
    @SideOnly(Side.CLIENT)
    public float getCastPercentage(EntityPlayer entityPlayer) {
    	float f = 0;
    	if(entityPlayer.getItemInUse() == null) {
    		return f;
    	}
    	float total = (float) this.getCastTimeInTicks();
    	float current = total - (float) (entityPlayer.getItemInUseCount() - 72000);
    	f = current/total;

    	if(f > 1) {
    		f = 1;
    	}
    	if(f < 0) {
    		f = 0;
    	}

    	return f;
    }
    
    //----------------------------------------------------------------------------------
    //							SECTION - Sound
    //----------------------------------------------------------------------------------
	
    public void sendCastingSoundPacket(boolean start) {
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.SERVER && this.school.hasCastingSound() && this.castTime > 0) {
			float range = (start) ? 60 : 100;
			FMLProxyPacket packet;
			String soundEvent = "abilityschool." + this.school.toString().toLowerCase() + ".casting";
			if(start) {
				packet = (new BattleClassesPacketCastingSound(this.getOwnerPlayer(), soundEvent,true)).generatePacket();
			}
			else {
				packet = (new BattleClassesPacketCastingSound(this.getOwnerPlayer())).generatePacket();
			}
			BattleClassesMod.packetHandler.sendPacketAround(this.getOwnerPlayer(), range, packet);
		}
    }
    
    private void playAbilitySound(Entity entityPlayAt, String soundEventName) {
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.SERVER) {
			String soundEvent = BattleClassesMod.MODID + ":" + this.getUnlocalizedID() + "." + soundEventName;
			entityPlayAt.worldObj.playSoundAtEntity(entityPlayAt, soundEvent, 1F, 1F);
		}
    }
    
    public void playReleaseSound() {
    	this.playAbilitySound(getOwnerPlayer(), "release");
    }
    
    public void playTickSoundAtEntity(Entity targetEntity) {
    	this.playAbilitySound(targetEntity, "tick");
    }
    
    public void playImpactSoundAtEntity(Entity targetEntity) {
    	this.playAbilitySound(targetEntity, "impact");
    }
    
    //----------------------------------------------------------------------------------
    //							Section - Effect visual
    //----------------------------------------------------------------------------------
    
    public void animateWithParticlesRelease(EntityPlayer owner) {
    	
    }
    
    public void animateWithParticlesTick(EntityPlayer owner, EntityLivingBase target, boolean critical) {
    	
    }
    
    public void animateWithParticlesImpact(EntityPlayer owner, EntityLivingBase target, boolean critical) {
    	
    }
    
    public void animateWithParticlesCarrierTravel(Entity abilityCarrierEntity, int travelTick) {
    	
    }
}
