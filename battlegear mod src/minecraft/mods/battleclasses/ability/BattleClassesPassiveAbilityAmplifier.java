package mods.battleclasses.ability;

import mods.battleclasses.core.BattleClassesAttributes;
import mods.battleclasses.core.IAmplifyProvider;
import mods.battleclasses.enums.EnumBattleClassesAmplifierApplyType;

public class BattleClassesPassiveAbilityAmplifier extends BattleClassesAbstractAbilityPassive implements IAmplifyProvider {
	
	public BattleClassesPassiveAbilityAmplifier(int parAbilityID) {
		super(parAbilityID);
	}
	
	EnumBattleClassesAmplifierApplyType amplifierApplyType = EnumBattleClassesAmplifierApplyType.BASE_ATTRIBUTE_BONUS;
	BattleClassesAttributes attributes = null;
	
	@Override
	public EnumBattleClassesAmplifierApplyType getApplyType() {
		return amplifierApplyType;
	}

	@Override
	public BattleClassesAttributes getAttributes(int targetAbilityID) {
		return attributes;
	}

	@Override
	public BattleClassesAttributes getAttributes(int targetAbilityID,
			BattleClassesAttributes totalAttributes) {
		return this.getAttributes(targetAbilityID);
	}
}
