package mods.battleclasses.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BattleClassesCombatHooks {
	
	@SubscribeEvent
    public void attackEntity(AttackEntityEvent event) {
		//TODO : Apply melee speed handling
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
