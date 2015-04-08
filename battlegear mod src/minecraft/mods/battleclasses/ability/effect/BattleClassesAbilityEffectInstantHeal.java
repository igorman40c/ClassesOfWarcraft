package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesAbilityEffectInstantHeal extends BattleClassesAbstractAbilityEffectInstantValue {

	BattleClassesAbilityEffectInstantHeal(BattleClassesAbstractAbilityActive ability) {
		super(ability);
	}

	@Override
	public void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target) {
		//TODO:
		//Check if target is friendly
		//Heal target by outputValue
	}
	
	@Override
	public EnumBattleClassesAbilityIntent getIntent() {
		return EnumBattleClassesAbilityIntent.SUPPORTIVE;
	}

}