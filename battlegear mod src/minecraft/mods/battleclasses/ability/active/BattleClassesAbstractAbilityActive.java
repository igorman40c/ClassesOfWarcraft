package mods.battleclasses.ability.active;

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
	}
	
	protected IIcon abilityIcon;
	public ResourceLocation abilityIconResourceLocation;
	
	//Basic ability parameters
	protected EnumBattleClassesAbilitySchool school = EnumBattleClassesAbilitySchool.UNKNOWN;
	protected EnumBattleClassesAbilityDirectTargetRequirement targetRequirementType = EnumBattleClassesAbilityDirectTargetRequirement.NEEDLESS;
	protected EnumBattleClassesAbilityIntent intent = EnumBattleClassesAbilityIntent.DUAL;
	
	//Basic ability attributes
	public float castTime = 0;
	public float range = 40;
	
	//Supporting modifiers
	public float criticalChance = 0;
	public float haste = 0;
	
	public boolean ignoresGlobalCooldown = false;
	protected boolean ignoresSilence = false;
	protected boolean requiresMeleeSwing = false;
	protected int requiredItemLevel = 0;
	protected String name = "Unknown Ability";
	
	public EnumAction getEnumActionForCasting() {
		return EnumAction.bow;
	}
	
	public abstract EnumBattleClassesAbilityCastingType getCastingType();
	
	/**
	 * Helper method to get the owner of this ability
	 * @return
	 */
	protected EntityPlayer getOwnerPlayer() {
		return this.playerHooks.getOwnerPlayer();
	}
	
	/**
	 * Called when player presses Mouse-Right button
	 */
	public void onCastStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(!this.isAvailable(entityPlayer, itemStack)) {
			this.cancelCasting(entityPlayer);
			return;
		}
	}
	
	/**
	 * Called while player keeps Mouse-Right button pressed down. Hook method to be overriden.
	 */
	public void onCastTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		
	}

	/**
	 * Called when player releases Mouse-Right button
	 */
	public void onCastRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		sendCastingSoundPacket(false);
	}
		
	public float getAbilityDamageBase() {
		//TODO
		return 0;
	}
	
	public float getAbilityDamageAmplified() {
		//TODO
		return 0;
	}
	
	public boolean isIgnoringSilence() {
		return this.ignoresSilence;
	}
	
	protected void startCastingProcess(EntityPlayer entityPlayer, ItemStack itemStack) {
		BattleClassesUtils.Log("Casting started!", LogType.ABILITY);
		BattleClassesUtils.setEntityPlayerItemInUseInSeconds(entityPlayer, itemStack, this.getCastTime());
		sendCastingSoundPacket(true);
	}
	
	public EnumBattleClassesAbilityDirectTargetRequirement getTargetingType() {
		return this.targetRequirementType;
	}
	
	public EnumBattleClassesAbilitySchool getSchool() {
		return this.school;
	}
	
		/**
	 * Called on CastStart to check if the ability is available and ready to use
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
		

	
	public boolean isInstant() {
		return this.getCastTime() == 0;
	}
	
	public int getCastTimeInTicks() {
		return (int) (this.getCastTime() * 20);
	}
	
	public void cancelCasting(EntityPlayer entityPlayer) {
		if(!this.ignoresGlobalCooldown) {
			this.playerHooks.playerClass.spellBook.cancelGlobalCooldown();
		}
		entityPlayer.clearItemInUse();
		sendCastingSoundPacket(false);
		BattleClassesUtils.Log("Cancelling Casting and GlobalCD", LogType.ABILITY);
	}
	
	public void requestCastingProcessFinish(EntityPlayer entityPlayer, ItemStack itemStack, int tickCount) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		//Checking CLIENT SIDE
		if(side == Side.CLIENT && this.requiresRayTracingForTarget()) {
			
			//CHECK TARGET REQUIREMENTS
			//CHECK RANGE REQUIREMENTS
			EntityLivingBase target = this.getFinalTarget();
			
			//TODO
			if (target != null) {
				int targetEntityID = target.getEntityId();
				FMLProxyPacket p = new BattleClassesPacketProcessAbilityWithTarget(entityPlayer, this.abilityID, targetEntityID, tickCount).generatePacket();
				BattleClassesMod.packetHandler.sendPacketToServer(p);
			}
		}
		//Checking SERVER SIDE
		else if (side == Side.SERVER && !this.requiresRayTracingForTarget()) {
			EntityLivingBase targetEntity = null;
			this.finishCastingWithTarget(targetEntity, tickCount);
		} 
		else {
			System.out.println("ServerSide requestProcession failed. TargetRT: " + this.targetRequirementType);
		}
	}
	
	public boolean finishCastingWithTarget(EntityLivingBase targetEntity, int tickCount) {
		boolean performSucceeded = this.performEffects(targetEntity, tickCount);
		if(performSucceeded) {
			this.onCastFinished(targetEntity, tickCount);
		}
		
		return performSucceeded;
	}
	
	public abstract boolean performEffects(EntityLivingBase targetEntity, int tickCount);
		
	public void onCastFinished(EntityLivingBase targetEntity, int tickCount) {
		BattleClassesUtils.Log("Casting finished!", LogType.ABILITY);
	}
	
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
    
    @Override
    public BattleClassesAbstractAbilityActive setName(String parName) {
		super.setName(parName);
		abilityIconResourceLocation = BattleClassesGuiHelper.getResourceLocationOfTexture("textures/spells/icons/", this.getUnlocalizedIconName() + ".png");
		return this;
	}
    
    @SideOnly(Side.CLIENT)
    public String getName() {
    	return name;
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
    
    public float getCastTime() {
    	return this.castTime;
    }
	
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
    
    public void playReleaseSound() {
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.SERVER) {
			String soundEvent = BattleClassesMod.MODID + ":" + this.getUnlocalizedID() + ".release";
			this.getOwnerPlayer().worldObj.playSoundAtEntity(getOwnerPlayer(), soundEvent, 1F, 1F);
		}
    }
    
    public void playImpactSoundAtEntity(Entity targetEntity) {
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.SERVER) {
			String soundEvent = BattleClassesMod.MODID + ":" + this.getUnlocalizedID() + ".impact";
			this.getOwnerPlayer().worldObj.playSoundAtEntity(targetEntity, soundEvent, 1F, 1F);
		}
    }
}
