package mods.battleclasses.core;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

public interface ICWAttributeModifier {
	
	/**
	 * Used with applyTypes: BASE_ATTRIBUTE_BONUS, BASE_ATTRIBUTE_AMPLIFIER
	 * @param targetAbilityID - ID of the amplified ability
	 * @return
	 */
	public BattleClassesAttributes applyAttributeModifier(int targetAbilityID);
	/**
	 * Used with applyType: TOTAL_BASED_ATTRIBUTE_BONUS
	 * @param targetAbilityID - ID of the amplified ability
	 * @param totalAttributes - Total attributes to be based on the bonus calculation
	 * @return
	 */
	public BattleClassesAttributes applyAttributeModifier(int targetAbilityID, BattleClassesAttributes totalAttributes);
	
	
	public EnumBattleClassesAmplifierApplyType getApplyType();
	public BattleClassesAttributes applyAttributeModifier(BattleClassesAbstractAbilityActive modifiedAbility, BattleClassesAttributes totalAttributes);
}
