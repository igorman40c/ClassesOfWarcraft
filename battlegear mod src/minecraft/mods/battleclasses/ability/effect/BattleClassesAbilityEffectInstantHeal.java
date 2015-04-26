package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class BattleClassesAbilityEffectInstantHeal extends BattleClassesAbstractAbilityEffectInstantValue {

	BattleClassesAbilityEffectInstantHeal(EnumBattleClassesAbilitySchool abilitySchool) {
		super(abilitySchool);
	}
	
	BattleClassesAbilityEffectInstantHeal(EnumBattleClassesAbilitySchool school,
			float valueBase, float valueBonusCoefficient, float valueTotalRandomness) {
		super(school, valueBase, valueBonusCoefficient, valueTotalRandomness);
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