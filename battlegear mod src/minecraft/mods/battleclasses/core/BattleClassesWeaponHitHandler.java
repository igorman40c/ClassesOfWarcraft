package mods.battleclasses.core;

import java.util.EnumSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.BattleClassesWeaponSkill;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.enums.EnumBattleClassesWieldAccess;
import mods.battleclasses.items.IControlledSpeedWeapon;
import mods.battlegear2.Battlegear;
import mods.battlegear2.BattlemodeHookContainerClass;
import mods.battlegear2.api.IUsableItem;
import mods.battlegear2.api.PlayerEventChild;
import mods.battlegear2.api.core.IBattlePlayer;
import mods.battlegear2.api.weapons.IBackStabbable;
import mods.battlegear2.api.weapons.IHitTimeModifier;
import mods.battlegear2.api.weapons.IPenetrateWeapon;
import mods.battlegear2.api.weapons.IPotionEffect;
import mods.battlegear2.api.weapons.ISpecialEffect;
import mods.battlegear2.utils.EnumBGAnimations;

public class BattleClassesWeaponHitHandler {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	EnumSet<EnumBattleClassesWieldAccess> accessSet;
	
	public static final int WEAPONSKILL_COOLDOWN_HASHCODE_MAINHAND =  1398;
	public static final int WEAPONSKILL_COOLDOWN_HASHCODE_OFFHAND = 1397;
	public static final float DEFAULT_WEAPON_COOLDOWN_DURATION = 1F;
	public CooldownClock mainHandClock;
	public CooldownClock offHandClock;
	
	public BattleClassesWeaponHitHandler(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		mainHandClock = new CooldownClock(WEAPONSKILL_COOLDOWN_HASHCODE_MAINHAND, parPlayerHooks);
		offHandClock = new CooldownClock(WEAPONSKILL_COOLDOWN_HASHCODE_OFFHAND, parPlayerHooks);
	}
	
	public void processAttack(AttackEntityEvent attackEvent) {
		ItemStack item = attackEvent.entityPlayer.getCurrentEquippedItem();
		ItemStack mainHandItemStack = BattleClassesUtils.getMainhandItemStack(attackEvent.entityPlayer);
		ItemStack offHandItemStack = BattleClassesUtils.getOffhandItemStack(attackEvent.entityPlayer);
		if(!mainHandClock.isOnCooldown()) {
			System.out.println("Using mainhand, setting CD");
			this.setMainhandToCooldown(mainHandItemStack, offHandItemStack);
		}
		else if(offHandItemStack != null) {
			System.out.println("Mainhand on CD!");
			if(!offHandClock.isOnCooldown()) {
				//Generating offhand attack event
				//Transforming attack->interact event
				System.out.println("Using offhand, setting CD");
				EntityInteractEvent interactEvent = new EntityInteractEvent(attackEvent.entityPlayer, attackEvent.target);
				PlayerEventChild.OffhandAttackEvent offAttackEvent = new PlayerEventChild.OffhandAttackEvent(interactEvent, mainHandItemStack, offHandItemStack);
                boolean useableItemInMainhand = mainHandItemStack != null && mainHandItemStack.getItem() != null && mainHandItemStack.getItem() instanceof IUsableItem;
                if(!useableItemInMainhand && !MinecraftForge.EVENT_BUS.post(offAttackEvent)){
                    if (offAttackEvent.swingOffhand){
                    	BattlemodeHookContainerClass.sendOffSwingEvent(interactEvent, mainHandItemStack, offHandItemStack);
                    }
                    if (offAttackEvent.shouldAttack) {
                        ((IBattlePlayer) interactEvent.entityPlayer).attackTargetEntityWithCurrentOffItem(interactEvent.target);
                    }
                    if (offAttackEvent.cancelParent) {
                    	interactEvent.setCanceled(true);
                    }
                }
				
				setOffhandToCooldown(mainHandItemStack, offHandItemStack);
			}
			else {
				System.out.println("Offhand on CD!");
			}
			this.cancelMainhandWeaponSwingAnimation(attackEvent.entityPlayer);
			attackEvent.setCanceled(true);
		}
	}
	
	public void initAccessSet() {
		accessSet.clear();
	}
		
	public void setMainhandToCooldown(ItemStack itemStackHeld, ItemStack offHandItemStack) {
		if(itemStackHeld != null) {
			float cooldownDuration = DEFAULT_WEAPON_COOLDOWN_DURATION;
			if(itemStackHeld.getItem() instanceof IControlledSpeedWeapon) {
				cooldownDuration = ((IControlledSpeedWeapon)itemStackHeld.getItem()).getSpeedInSeconds();
			}
			mainHandClock.setCooldown(cooldownDuration, true, EnumBattleClassesCooldownType.CooldownType_ABILITY);
			
			
			///
			if(offHandItemStack != null) {
				float counterCooldownDuration = DEFAULT_WEAPON_COOLDOWN_DURATION;
				if(offHandItemStack.getItem() instanceof IControlledSpeedWeapon) {
					counterCooldownDuration = ((IControlledSpeedWeapon)offHandItemStack.getItem()).getSpeedInSeconds();
				}
				offHandClock.setCooldown(counterCooldownDuration/2, true, EnumBattleClassesCooldownType.CooldownType_ABILITY);
			}
			///
		}
	}
	
	public void setOffhandToCooldown(ItemStack itemStackHeld, ItemStack offHandItemStack) {
		if(offHandItemStack != null) {
			float cooldownDuration = DEFAULT_WEAPON_COOLDOWN_DURATION;
			if(offHandItemStack.getItem() instanceof IControlledSpeedWeapon) {
				cooldownDuration = ((IControlledSpeedWeapon)offHandItemStack.getItem()).getSpeedInSeconds();
			}
			offHandClock.setCooldown(cooldownDuration, true, EnumBattleClassesCooldownType.CooldownType_ABILITY);
			
			
			///
			if(itemStackHeld != null) {
				float counterCooldownDuration = DEFAULT_WEAPON_COOLDOWN_DURATION;
				if(itemStackHeld.getItem() instanceof IControlledSpeedWeapon) {
					counterCooldownDuration = ((IControlledSpeedWeapon)itemStackHeld.getItem()).getSpeedInSeconds();
				}
				mainHandClock.setCooldown(counterCooldownDuration/2, true, EnumBattleClassesCooldownType.CooldownType_ABILITY);
			}
			///
		}
	}

	public void cancelMainhandWeaponSwingAnimation(EntityPlayer entityPlayer) {
		//TODO
		entityPlayer.swingProgress = 0;
	}
	
}
