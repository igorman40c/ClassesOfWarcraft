package mods.battleclasses.ability.effect;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;

public class BattleClassesAbilityEffectInstantHeal extends BattleClassesAbstractAbilityEffectInstantValue {

	BattleClassesAbilityEffectInstantHeal() {
		super();
	}
	
	BattleClassesAbilityEffectInstantHeal(float valueBase, float valueBonusCoefficient, float valueTotalRandomness) {
		super(valueBase, valueBonusCoefficient, valueTotalRandomness);
	}

	@Override
	protected void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target) {
		//Check if target is friendly
		if(!(owner instanceof EntityPlayer) || BattleClassesUtils.isTargetFriendly((EntityPlayer)owner, target)) {
			//Heal target by outputValue
			target.heal(this.getOutputValue());
		}
	}
	
	@Override
	public EnumBattleClassesAbilityIntent getIntent() {
		return EnumBattleClassesAbilityIntent.SUPPORTIVE;
	}

	@Override
	public String getTranslatedDescription() {
		String description = StatCollector.translateToLocal("bceffect.heal.description");
		float estimatedValue = this.getEstimatedOutput();
		String valueString;
		if(this.valueTotalRandomness==0) {
			valueString = String.format("%.1f", estimatedValue);
		}
		else {
			float estimatedMin = estimatedValue * (1F - this.valueTotalRandomness);
			float estimatedMax = estimatedValue * (1F + this.valueTotalRandomness);
			valueString = new String(String.format("%.1f", estimatedMin) + " - " + String.format("%.1f", estimatedMax));
		}
		
		String effectOutputString = valueString;
		description.replace("%1$s", effectOutputString);
		return description;
	}

}