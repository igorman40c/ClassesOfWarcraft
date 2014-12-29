package mods.battleclasses.core;

import mods.battleclasses.enumhelper.EnumBattleClassesCooldownType;

public interface ICooldownOwner {
	
	/**
	 * Returns the owned CooldownClock object
	 * @return - owned CooldownClock object
	 */
	public CooldownClock getCooldownClock();
	
}
