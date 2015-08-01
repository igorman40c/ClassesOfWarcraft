package mods.battleclasses.core;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
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
import mods.battleclasses.ability.active.BattleClassesAbilityAttackMainHand;
import mods.battleclasses.ability.active.BattleClassesAbilityAttackOffHand;
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
	
	protected ItemStack lastUsedMainHandItemStack;
	public boolean offhandAttackInProgress = false;
	
	public static final BattleClassesAbilityAttackMainHand mainHandAttackAbility = new BattleClassesAbilityAttackMainHand();
	public static final BattleClassesAbilityAttackOffHand offHandAttackAbility = new BattleClassesAbilityAttackOffHand();
	
	public BattleClassesWeaponHitHandler(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		mainHandAttackAbility.setContextReferences(parPlayerHooks, parPlayerHooks.playerAttributes);
		mainHandAttackAbility.onLearn();
		offHandAttackAbility.setContextReferences(parPlayerHooks, parPlayerHooks.playerAttributes);
		offHandAttackAbility.onLearn();
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
		System.out.println("Try processOffhandAttack");
		if (!this.offHandAttackAbility.isOnCooldown() && ((IBattlePlayer) entityPlayer).isBattlemode()) {
            ItemStack offhandItem = BattleClassesUtils.getOffhandItemStack(entityPlayer); 
            ItemStack mainHandItem = entityPlayer.getCurrentEquippedItem();
            
            System.out.println("processOffhandAttack @begin");
            
            if(entityPlayer.worldObj.isRemote) {
    			//True Client side
        		//BattlemodeHookContainerClass.sendOffSwingEvent(interactEvent, mainHandItem, offhandItem);
        		Minecraft mc = Minecraft.getMinecraft();
        		((IBattlePlayer) mc.thePlayer).swingOffItem();
	            Battlegear.proxy.sendAnimationPacket(EnumBGAnimations.OffHandSwing, mc.thePlayer);
        		return;
    		}
    		else {
    			//True Server side
    		}
            
            PlayerEventChild.OffhandAttackEvent offAttackEvent = new PlayerEventChild.OffhandAttackEvent(interactEvent, mainHandItem, offhandItem);
            offhandAttackInProgress = true;
            if(!MinecraftForge.EVENT_BUS.post(offAttackEvent)){
                if (offAttackEvent.shouldAttack) {

                	//this.attackWithOffHand((EntityLivingBase) entityTarget);
        			((IBattlePlayer) entityPlayer).attackTargetEntityWithCurrentOffItem(entityTarget);
                }
            }
            offhandAttackInProgress = false;
            System.out.println("processOffhandAttack @end");
        }
	}
	
	public boolean isOffhandAttackInProgress() {
		return this.offhandAttackInProgress;
	}
		
	public void initAccessSet() {
		accessSet.clear();
	}
	
	public void attackWithMainHand(EntityLivingBase entityTarget) {
		this.mainHandAttackAbility.performEffects(entityTarget);
		this.setMainhandToCooldown();
		
		
		System.out.println("Mainhand CD remaining: " + mainHandAttackAbility.getCooldownClock().getRemainingDuration());
		System.out.println("Offhand CD remaining: " + offHandAttackAbility.getCooldownClock().getRemainingDuration());
	}
	
	public void attackWithOffHand(EntityLivingBase entityTarget) {
		this.offHandAttackAbility.performEffects(entityTarget);
		this.setOffhandToCooldown();
		
		System.out.println("Mainhand CD remaining: " + mainHandAttackAbility.getCooldownClock().getRemainingDuration());
		System.out.println("Offhand CD remaining: " + offHandAttackAbility.getCooldownClock().getRemainingDuration());
	}
		
	public void setMainhandToCooldown() {
		System.out.println("Settings mainhandCD");
		EntityPlayer ownerPlayer = this.playerHooks.getOwnerPlayer();
		ItemStack mainHandItemStack = BattleClassesUtils.getMainhandItemHeld(ownerPlayer);
		ItemStack offHandItemStack = BattleClassesUtils.getOffhandItemHeld(ownerPlayer);
		/*
		if(mainHandItemStack != null) {
			this.mainHandAttackAbility.startCooldown();
			///
			if(offHandItemStack != null) {
				this.offHandAttackAbility.startCooldown(0.45F);
			}
			///
		}
		*/
		this.mainHandAttackAbility.startCooldown();
		this.offHandAttackAbility.startCooldown(0.45F);
	}
	
	public void setOffhandToCooldown() {
		System.out.println("Settings OffhandCD");
		EntityPlayer ownerPlayer = this.playerHooks.getOwnerPlayer();
		ItemStack mainHandItemStack = BattleClassesUtils.getMainhandItemHeld(ownerPlayer);
		ItemStack offHandItemStack = BattleClassesUtils.getOffhandItemHeld(ownerPlayer);
		/*
		if(offHandItemStack != null) {
			this.offHandAttackAbility.startCooldown();
			///
			if(mainHandItemStack != null) {
				this.mainHandAttackAbility.startCooldown(0.45F);
			}
			///
		}
		*/
		this.offHandAttackAbility.startCooldown();
		this.mainHandAttackAbility.startCooldown(0.45F);
	}
	
}
