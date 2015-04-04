package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesAbilityEffectInstantDamage extends BattleClassesAbstractAbilityEffectInstantValue {

	BattleClassesAbilityEffectInstantDamage(BattleClassesAbstractAbilityActive ability) {
		super(ability);
	}

	@Override
	public void performByOwnerOnTarget(EntityPlayer owner, EntityLivingBase target) {
		//TODO:
		//Reset hurtCD
		//Check if target is unfriendly
		//Get DamageSource from school
		//Set Damage value to outputValue
		//Attack target
		//Reset hurtCD
	}

	@Override
	public EnumBattleClassesAbilityIntent getIntent() {
		return EnumBattleClassesAbilityIntent.OFFENSIVE;
	}

}
