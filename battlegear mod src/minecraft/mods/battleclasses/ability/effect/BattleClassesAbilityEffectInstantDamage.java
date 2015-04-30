package mods.battleclasses.ability.effect;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class BattleClassesAbilityEffectInstantDamage extends BattleClassesAbstractAbilityEffectInstantValue {

	BattleClassesAbilityEffectInstantDamage() {
		super();
	}
	
	BattleClassesAbilityEffectInstantDamage(float valueBase, float valueBonusCoefficient, float valueTotalRandomness) {
		super(valueBase, valueBonusCoefficient, valueTotalRandomness);
	}
		
	@Override
	protected void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target) {
		//Reset hurtCD
		int tempHurtCD = target.hurtResistantTime;
		target.hurtResistantTime = 0;
		//Check if target is unfriendly
		if(!(owner instanceof EntityPlayer) || !BattleClassesUtils.isTargetFriendly((EntityPlayer)owner, target)) {
			//Get DamageSource from school
			DamageSource damageSource = BattleClassesDamageSources.createEntityDamageSourceForAbilitySchool(owner, this.getAbilitySchool());
			//Attack target by outputValue
			target.attackEntityFrom(damageSource, getOutputValue());
		}
		//Reset hurtCD
		target.hurtResistantTime = tempHurtCD;
	}

	@Override
	public EnumBattleClassesAbilityIntent getIntent() {
		return EnumBattleClassesAbilityIntent.OFFENSIVE;
	}

}
