package mods.battleclasses.attributes;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

/**
 * Classes of Warcraft attribute modifier interface.
 * @author Zsolt
 */
public interface ICWAttributeModifier {
	/**
	 * Determines the applying order while accumulating the modifiers. 
	 * Implementating classes with a certain type should act as the name of the enum describes.
	 * @return
	 */
	public EnumBattleClassesAmplifierApplyType getApplyType();
	/**
	 * Applies modification on the recent attributes. Used during the accumulation of the modifiers.
	 * @param targetAbility - the ability that is going to use
	 * @param attributes - already accumalted attributes those to be modified
	 * @return
	 */
	public BattleClassesAttributes applyAttributeModifier(BattleClassesAbstractAbilityActive targetAbility, BattleClassesAttributes attributes);
}
