package mods.battleclasses.client;

import java.util.Set;

import mods.battleclasses.enums.EnumBattleClassesAttributeType;

public interface IItemTooltipProvider extends ITooltipProvider {
	/**
	 * Determines the set of active attribute types those shouldn't be added to the item tooltip attribute bonus list.
	 * Possible reasons: already added, want to hide, etc...
	 * @return
	 */
	public Set<EnumBattleClassesAttributeType> getActiveTypeExceptions();
}
