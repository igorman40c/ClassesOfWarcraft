package mods.battleclasses.core;

import mods.battleclasses.enumhelper.EnumBattleClassesAmplifierApplyType;

public interface IAmplifyProvider {
	public EnumBattleClassesAmplifierApplyType getApplyType();
	/**
	 * Used with applyTypes: BASE_ATTRIBUTE_BONUS, BASE_ATTRIBUTE_AMPLIFIER
	 * @param targetAbilityID - ID of the amplified ability
	 * @return
	 */
	public BattleClassesAttributes getAttributes(int targetAbilityID);
	/**
	 * Used with applyType: TOTAL_BASED_ATTRIBUTE_BONUS
	 * @param targetAbilityID - ID of the amplified ability
	 * @param totalAttributes - Total attributes to be based on the bonus calculation
	 * @return
	 */
	public BattleClassesAttributes getAttributes(int targetAbilityID, BattleClassesAttributes totalAttributes);
}
