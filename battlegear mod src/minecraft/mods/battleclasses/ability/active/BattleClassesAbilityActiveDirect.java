package mods.battleclasses.ability.active;

import mods.battleclasses.enums.EnumBattleClassesAbilityDirectTargetRequirement;
import net.minecraft.entity.EntityLivingBase;

public class BattleClassesAbilityActiveDirect extends BattleClassesAbstractAbilityActive {

	public BattleClassesAbilityActiveDirect(int parAbilityID) {
		super(parAbilityID);
		this.targetRequirementType = EnumBattleClassesAbilityDirectTargetRequirement.REQUIRED;
	}

	@Override
	protected boolean releaseEffects(EntityLivingBase targetEntity, int tickCount) {
		if(targetEntity != null) {
			this.performEffects(targetEntity);
			return true;
		}
		return false;
	}

}
