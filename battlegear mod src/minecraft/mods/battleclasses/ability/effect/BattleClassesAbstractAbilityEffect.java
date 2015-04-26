package mods.battleclasses.ability.effect;

import java.util.List;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BattleClassesAbstractAbilityEffect {
	
	protected BattleClassesAbstractAbilityActive parentAbility;
	
	/**
	 * School of the effect value. Used to select corresponding power and critical data, determine dispellability.
	 */
	protected EnumBattleClassesAbilitySchool school;
	
	BattleClassesAbstractAbilityEffect(EnumBattleClassesAbilitySchool school) {
		this.school = school;
	}
	
	public void setParentAbility(BattleClassesAbstractAbilityActive ability) {
		this.parentAbility = ability;
	}
	
	public EnumBattleClassesAbilitySchool getAbilitySchool() {
		return this.school;
	}
	
	public abstract void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target);
	
	public abstract EnumBattleClassesAbilityIntent getIntent();
	
	/**
	 * Utility method to perform a list of effects on target with some additional parameters
	 * @param effects - the list of effects
	 * @param attributesForParentAbility - attributes to be used to power value effects
	 * @param critChance - critical chance to power value effects
	 * @param partialMultiplier  - multiplier to dampen value effects
	 * @param owner - owner of the effect
	 * @param target - target of the effect
	 */
	public static void performListOfEffects(List<BattleClassesAbstractAbilityEffect> effects, 
			BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier, 
			EntityLivingBase owner, EntityLivingBase target) {
		
		for(BattleClassesAbstractAbilityEffect effect : effects) {
			if(effect instanceof IValueEffect) {
				((IValueEffect) effect).performValueEffect(attributesForParentAbility, critChance, partialMultiplier, owner, target);
			}
			else {
				effect.performByOwnerOnTarget(owner, target);
			}
		}
	}
	
}
