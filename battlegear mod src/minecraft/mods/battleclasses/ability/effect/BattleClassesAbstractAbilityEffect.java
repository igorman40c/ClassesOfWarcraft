package mods.battleclasses.ability.effect;

import java.util.ArrayList;
import java.util.List;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BattleClassesAbstractAbilityEffect {
	
	/**
	 * School of the effect value. Used to select corresponding power and critical data, determine dispellability.
	 */
	public EnumBattleClassesAbilitySchool getAbilitySchool() {
		return this.parentAbility.getSchool();
	}
	
	protected BattleClassesAbstractAbilityActive parentAbility;
	public void setParentAbility(BattleClassesAbstractAbilityActive ability) {
		this.parentAbility = ability;
	}
	public BattleClassesAbstractAbilityActive getParentAbility() {
		return this.parentAbility;
	}

	
	/**
	 * Main perform method to prepare and perform the effect.
	 * @param attributesForParentAbility
	 * @param critChance
	 * @param partialMultiplier
	 * @param owner
	 * @param target
	 */
	public void performValueEffect(BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier, 
			EntityLivingBase owner, EntityLivingBase target) {
		
	}
	
	/**
	 * Core of the effect perform process.
	 * @param owner
	 * @param target
	 */
	protected abstract void performByOwnerOnTarget(EntityLivingBase owner, EntityLivingBase target);
	
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
			effect.performValueEffect(attributesForParentAbility, critChance, partialMultiplier, owner, target);
		}
	}
	
	/**
	 * Utility method to collect all the input modifiers from an entity those are able to modify this effect.
	 * @param entity
	 * @return
	 */
	public List<ICWEffectModifier> getInputEffectModifiersFromEntity(EntityLivingBase entity) {
		List<ICWEffectModifier> effectModifiers = new ArrayList<ICWEffectModifier>();
		//TODO
		return effectModifiers;
	}
	
	/**
	 * Utility method to collect all the output modifiers from an entity those are able to modify this effect.
	 * @param entity
	 * @return
	 */
	public List<ICWEffectModifier> getOutputEffectModifiersFromEntity(EntityLivingBase entity) {
		List<ICWEffectModifier> effectModifiers = new ArrayList<ICWEffectModifier>();
		//TODO
		return effectModifiers;
	}
}
