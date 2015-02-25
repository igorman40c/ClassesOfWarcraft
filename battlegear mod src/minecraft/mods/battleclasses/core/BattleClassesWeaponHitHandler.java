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
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesMod;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.BattleClassesWeaponSkill;
import mods.battleclasses.enums.EnumBattleClassesCooldownType;
import mods.battleclasses.enums.EnumBattleClassesWieldAccess;
import mods.battleclasses.items.IControlledSpeedWeapon;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battleclasses.packet.BattleClassesPacketProcessOffhandAttack;
import mods.battlegear2.Battlegear;
import mods.battlegear2.BattlemodeHookContainerClass;
import mods.battlegear2.api.IUsableItem;
import mods.battlegear2.api.PlayerEventChild;
import mods.battlegear2.api.core.BattlegearUtils;
import mods.battlegear2.api.core.IBattlePlayer;
import mods.battlegear2.api.core.InventoryPlayerBattle;
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
	protected ItemStack lastUsedMainHandItemStack;
	
	public BattleClassesWeaponHitHandler(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		mainHandClock = new CooldownClock(WEAPONSKILL_COOLDOWN_HASHCODE_MAINHAND, parPlayerHooks);
		offHandClock = new CooldownClock(WEAPONSKILL_COOLDOWN_HASHCODE_OFFHAND, parPlayerHooks);
	}

	 
	
	public void processOffhandAttack(EntityPlayer entityPlayer, Entity entityTarget) {
		if(entityPlayer.worldObj.isRemote) {
			//True Client side
			FMLProxyPacket p = new BattleClassesPacketProcessOffhandAttack(entityPlayer, entityTarget.getEntityId()).generatePacket();
			BattleClassesMod.packetHandler.sendPacketToServer(p);
		}
		else {
			//True Server side
		}
		EntityInteractEvent interactEvent = new EntityInteractEvent(entityPlayer, entityTarget);
		
		if (!this.offHandClock.isOnCooldown() && ((IBattlePlayer) entityPlayer).isBattlemode()) {
            ItemStack offhandItem = BattleClassesUtils.getOffhandItemStack(entityPlayer); 
            ItemStack mainHandItem = entityPlayer.getCurrentEquippedItem();
            PlayerEventChild.OffhandAttackEvent offAttackEvent = new PlayerEventChild.OffhandAttackEvent(interactEvent, mainHandItem, offhandItem);
            if(!MinecraftForge.EVENT_BUS.post(offAttackEvent)){
                if (offAttackEvent.shouldAttack) {
                	this.mainHandClock.setEnabled(false);
                    this.offHandClock.setEnabled(false);
                	BattlemodeHookContainerClass.sendOffSwingEvent(interactEvent, mainHandItem, offhandItem);
                    ((IBattlePlayer) entityPlayer).attackTargetEntityWithCurrentOffItem(entityTarget);
                    this.mainHandClock.setEnabled(true);
                    this.offHandClock.setEnabled(true);
                }
            }
        }
	}
		
	public void initAccessSet() {
		accessSet.clear();
	}
		
	public void setMainhandToCooldown(EntityPlayer entityPlayer) {
		System.out.println("Settings mainhandCD");
		ItemStack mainHandItemStack = BattleClassesUtils.getMainhandItemStack(entityPlayer);
		ItemStack offHandItemStack = BattleClassesUtils.getOffhandItemStack(entityPlayer);
		if(mainHandItemStack != null) {
			float cooldownDuration = DEFAULT_WEAPON_COOLDOWN_DURATION;
			if(mainHandItemStack.getItem() instanceof IControlledSpeedWeapon) {
				cooldownDuration = ((IControlledSpeedWeapon)mainHandItemStack.getItem()).getSpeedInSeconds();
				this.lastUsedMainHandItemStack = mainHandItemStack;
			}
			mainHandClock.setCooldown(cooldownDuration, true, EnumBattleClassesCooldownType.CooldownType_ABILITY);
			
			
			///
			if(offHandItemStack != null) {
				float counterCooldownDuration = DEFAULT_WEAPON_COOLDOWN_DURATION;
				if(offHandItemStack.getItem() instanceof IControlledSpeedWeapon) {
					counterCooldownDuration = ((IControlledSpeedWeapon)offHandItemStack.getItem()).getSpeedInSeconds();
				}
				offHandClock.setCooldown(counterCooldownDuration*0.45F, true, EnumBattleClassesCooldownType.CooldownType_ABILITY);
			}
			///
		}
	}
	
	public void setOffhandToCooldown(EntityPlayer entityPlayer) {
		System.out.println("Settings OffhandCD");
		ItemStack mainHandItemStack = BattleClassesUtils.getMainhandItemStack(entityPlayer);
		ItemStack offHandItemStack = BattleClassesUtils.getOffhandItemStack(entityPlayer);
		if(offHandItemStack != null) {
			float cooldownDuration = DEFAULT_WEAPON_COOLDOWN_DURATION;
			if(offHandItemStack.getItem() instanceof IControlledSpeedWeapon) {
				cooldownDuration = ((IControlledSpeedWeapon)offHandItemStack.getItem()).getSpeedInSeconds();
			}
			offHandClock.setCooldown(cooldownDuration, true, EnumBattleClassesCooldownType.CooldownType_ABILITY);
			
			
			///
			if(mainHandItemStack != null) {
				float counterCooldownDuration = DEFAULT_WEAPON_COOLDOWN_DURATION;
				if(mainHandItemStack.getItem() instanceof IControlledSpeedWeapon) {
					counterCooldownDuration = ((IControlledSpeedWeapon)mainHandItemStack.getItem()).getSpeedInSeconds();
				}
				mainHandClock.setCooldown(counterCooldownDuration*0.45F, true, EnumBattleClassesCooldownType.CooldownType_ABILITY);
			}
			///
		}
	}
	
}
