package mods.battleclasses.core;

import java.util.HashMap;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import net.minecraft.entity.player.EntityPlayer;

public interface IMainCooldownMap {
	
	/**
	 * Returns the HashMap in which CooldownClock objects are being stored
	 * @return
	 */
	public HashMap<Integer, CooldownClock> getCooldownMap();
	
	/**
	 * Returns the owner player of the CooldownMapHolder
	 * @return
	 */
	public EntityPlayer getCooldownCenterOwner();
	
	public float getCooldownMultiplierForAbility(BattleClassesAbstractAbilityActive ability);
	
}
