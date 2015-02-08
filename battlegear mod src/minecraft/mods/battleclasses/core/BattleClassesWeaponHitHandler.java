package mods.battleclasses.core;

import java.util.EnumSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.BattleClassesWeaponSkill;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.enums.EnumBattleClassesWieldAccess;
import mods.battleclasses.items.IControlledSpeedWeapon;
import mods.battlegear2.Battlegear;
import mods.battlegear2.BattlemodeHookContainerClass;
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
	
	private ItemStack lastUsedMainhand;
	public boolean processAttack(ItemStack itemStackHeld) {
		boolean cancelMainhandHitEvent = false;
		lastUsedMainhand = itemStackHeld;
		ItemStack mainHandItemStack = BattleClassesUtils.getMainhandItemStack(playerHooks.getOwnerPlayer());
		ItemStack offHandItemStack = BattleClassesUtils.getOffhandItemStack(playerHooks.getOwnerPlayer());
		if(mainHandClock.isOnCooldown()) {
			if(itemStackHeld == mainHandItemStack && offHandItemStack != null) {
				if(offHandClock.isOnCooldown()) {
					System.out.println("Offhand on CD!");
				}
				else {
					((IBattlePlayer) playerHooks.getOwnerPlayer()).swingOffItem();
		            Battlegear.proxy.sendAnimationPacket(EnumBGAnimations.OffHandSwing, playerHooks.getOwnerPlayer());
					setOffhandToCooldown(itemStackHeld, offHandItemStack);
				}
			}
			System.out.println("Mainhand on CD!");
			cancelMainhandHitEvent = true;
		}
		else {
			System.out.println("Using mainhand, setting CD");
			this.setMainhandToCooldown(itemStackHeld, (itemStackHeld == mainHandItemStack) ? offHandItemStack : null );
		}
		
		return cancelMainhandHitEvent;
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

	
}
