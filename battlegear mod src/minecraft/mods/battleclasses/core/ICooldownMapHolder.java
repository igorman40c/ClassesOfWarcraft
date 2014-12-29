package mods.battleclasses.core;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;

public interface ICooldownMapHolder {
	
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
}
