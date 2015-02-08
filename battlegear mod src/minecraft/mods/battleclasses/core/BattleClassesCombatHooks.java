package mods.battleclasses.core;

import java.util.List;

import mods.battleclasses.BattleClassesUtils;
import mods.battlegear2.api.IHandListener;
import mods.battlegear2.api.core.BattlegearUtils;
import mods.battlegear2.api.core.IBattlePlayer;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import mods.battlegear2.api.heraldry.IFlagHolder;
import mods.battlegear2.api.heraldry.IHeraldryItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BattleClassesCombatHooks {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerInteract(PlayerInteractEvent event) {
		System.out.println("asd");
        if(event.entityPlayer instanceof FakePlayer)
            return;
        /*
        if(((IBattlePlayer) event.entityPlayer).getSpecialActionTimer() > 0){
            event.setCanceled(true);
            event.entityPlayer.isSwingInProgress = false;
        }else
        */
        if(((IBattlePlayer) event.entityPlayer).isBattlemode()) {
        	System.out.println("asd");
        	//Right click block
            if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {//Right click
            	System.out.println("asd");
            	/*
                ItemStack mainHandItem = event.entityPlayer.getCurrentEquippedItem();
                if(mainHandItem == null || !BattlegearUtils.usagePriorAttack(mainHandItem)) {
                    ItemStack offhandItem = ((InventoryPlayerBattle) event.entityPlayer.inventory).getCurrentOffhandWeapon();
                    if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
                        event.setCanceled(true);
                    sendOffSwingEvent(event, mainHandItem, offhandItem);
                }
                */
            }
            //Left click block
            else {
                ItemStack mainHandItem = event.entityPlayer.getCurrentEquippedItem();
                System.out.println("Alter hand!");
            }
        }
    }
	
	/**
	 * Helper method to certain events, should be called on attack, on hit recieved.
	 * @param player
	 */
	public static void onPlayerCombatEntered(EntityPlayer player) {
		
	}
		
	/**
	 * Checks the player for temporal effects which prevent him/her from using any kind of <<attack AND ability>>
	 * @param player
	 * @return
	 */
	public static boolean isPlayerCombatDisabled(EntityPlayer player) {
		return false;
	}
	
	/**
	 * Checks the player for temporal effects which prevent him/her from using <<abilitis>> which don't ignore silence
	 * @param player
	 * @return
	 */
	public static boolean isPlayerSilenced(EntityPlayer player) {
		return false;
	}
	
}
