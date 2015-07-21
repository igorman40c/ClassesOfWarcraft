package mods.battleclasses.ability.effect;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;

public class BattleClassesAbilityEffectInstantDamage extends BattleClassesAbstractAbilityEffectInstantValue {

	BattleClassesAbilityEffectInstantDamage() {
		super();
	}
	
	BattleClassesAbilityEffectInstantDamage(float valueBase, float valueBonusCoefficient, float valueTotalRandomness) {
		super(valueBase, valueBonusCoefficient, valueTotalRandomness);
	}
	
	public static BattleClassesAbilityEffectInstantDamage createWeaponAttackEffect() {
		BattleClassesAbilityEffectInstantDamage damageEffect = new BattleClassesAbilityEffectInstantDamage(0,1,0);
		return damageEffect;
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

	@Override
	public String getTranslatedDescription() {
		String description = StatCollector.translateToLocal("bceffect.damage.description");
		float estimatedValue = this.getEstimatedOutput();
		String valueString;
		if(this.valueTotalRandomness==0) {
			valueString = String.format("%.1f", estimatedValue);
		}
		else {
			float estimatedMin = estimatedValue * (1F - this.valueTotalRandomness);
			float estimatedMax = estimatedValue * (1F + this.valueTotalRandomness);
			valueString = new String(String.format("%.1f", estimatedMin) + "-" + String.format("%.1f", estimatedMax));
		}
		String abilitySchoolString = this.getAbilitySchool().getTranslatedDisplayedName();
		String effectOutputString = new String(valueString + " " + abilitySchoolString);
		description = description.replace("%1$s", effectOutputString);
		return description;
	}

}
