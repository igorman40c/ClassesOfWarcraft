package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesAbilityEffectInstantDual extends BattleClassesAbstractAbilityEffectInstantValue {

	BattleClassesAbilityEffectInstantDual(BattleClassesAbstractAbilityActive ability) {
		super(ability);
	}
	
	@Override
	public EnumBattleClassesAbilityIntent getIntent() {
		return EnumBattleClassesAbilityIntent.DUAL;
	}
	
	protected BattleClassesAbilityEffectInstantDamage damageEffect;
	protected BattleClassesAbilityEffectInstantHeal healEffect;
	
	public void setDamageAndHealEffects(BattleClassesAbilityEffectInstantDamage damageEffect, 
			BattleClassesAbilityEffectInstantHeal healEffect) {
		this.damageEffect = damageEffect;
		this.healEffect = healEffect;
	}
	
	@Override
	public void performValueEffect(BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier, 
			EntityLivingBase owner, EntityLivingBase target) {
		this.prepareToPerform(attributesForParentAbility, critChance, partialMultiplier);
		this.performByOwnerOnTarget(owner, target);
		this.resetOutput();
	}
	
	@Override
	public void prepareToPerform(BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier) {
		this.damageEffect.prepareToPerform(attributesForParentAbility, critChance, partialMultiplier);
		this.healEffect.prepareToPerform(attributesForParentAbility, critChance, partialMultiplier);
	}

	@Override
	public void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target) {
		this.damageEffect.performByOwnerOnTarget(owner, target);
		this.healEffect.performByOwnerOnTarget(owner, target);
	}
	
	@Override
	public void resetOutput() {
		this.damageEffect.resetOutput();
		this.healEffect.resetOutput();
	}
}