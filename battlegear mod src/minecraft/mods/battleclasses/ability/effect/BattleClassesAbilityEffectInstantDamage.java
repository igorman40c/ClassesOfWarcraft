package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class BattleClassesAbilityEffectInstantDamage extends BattleClassesAbstractAbilityEffectInstantValue {

	BattleClassesAbilityEffectInstantDamage(EnumBattleClassesAbilitySchool abilitySchool) {
		super(abilitySchool);
	}
	
	BattleClassesAbilityEffectInstantDamage(EnumBattleClassesAbilitySchool school,
			float valueBase, float valueBonusCoefficient, float valueTotalRandomness) {
		super(school, valueBase, valueBonusCoefficient, valueTotalRandomness);
	}
		
	@Override
	public void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target) {
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
