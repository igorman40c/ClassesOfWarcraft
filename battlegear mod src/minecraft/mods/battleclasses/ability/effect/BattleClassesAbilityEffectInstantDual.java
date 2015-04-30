package mods.battleclasses.ability.effect;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesAbilityEffectInstantDual extends BattleClassesAbstractAbilityEffectInstantValue {

	BattleClassesAbilityEffectInstantDual() {
		super();
	}
	
	BattleClassesAbilityEffectInstantDual(BattleClassesAbilityEffectInstantDamage damageEffect, 
			BattleClassesAbilityEffectInstantHeal healEffect) {
		this();
		this.setDamageAndHealEffects(damageEffect, healEffect);
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
	
	public void setParentAbility(BattleClassesAbstractAbilityActive ability) {
		super.setParentAbility(ability);
		if(damageEffect != null) {
			damageEffect.setParentAbility(ability);
		}
		if(healEffect != null) {
			healEffect.setParentAbility(ability);
		}
	}
	
	@Override
	public void performValueEffect(BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier, 
			EntityLivingBase owner, EntityLivingBase target) {
		this.prepareToPerform(attributesForParentAbility, critChance, partialMultiplier, owner, target);
		this.performByOwnerOnTarget(owner, target);
		this.resetOutput();
	}
	
	@Override
	protected void prepareToPerform(BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier,
			EntityLivingBase owner, EntityLivingBase target) {
		this.damageEffect.prepareToPerform(attributesForParentAbility, critChance, partialMultiplier, owner, target);
		this.healEffect.prepareToPerform(attributesForParentAbility, critChance, partialMultiplier, owner, target);
	}

	@Override
	protected void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target) {
		this.damageEffect.performByOwnerOnTarget(owner, target);
		this.healEffect.performByOwnerOnTarget(owner, target);
	}
	
	@Override
	public void resetOutput() {
		this.damageEffect.resetOutput();
		this.healEffect.resetOutput();
	}
}