package mods.battleclasses.ability.effect;

import java.util.ArrayList;
import java.util.List;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.effect.modifier.ICWEffectModifier;
import mods.battleclasses.ability.effect.modifier.ICWEffectModifierOwner;
import mods.battleclasses.ability.passive.BattleClassesAbstractAbilityPassive;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.client.IDescriptionProvider;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battleclasses.core.IStackableModifier;
import mods.battleclasses.enums.EnumBattleClassesAbilityIntent;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public abstract class BattleClassesAbstractAbilityEffect implements IDescriptionProvider {
	
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
	
	//----------------------------------------------------------------------------------
	//							SECTION - Modifyable properties
	//----------------------------------------------------------------------------------
	
	public float modifierMultiplier = 1;
	public float modifierCriticalBonus = 0;
	
	public void resetModifiers() {
		this.modifierMultiplier = 1;
		this.modifierCriticalBonus = 0;
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
	 * Utility method to collect all the input modifiers from an entity, and modifiy this effect using them.
	 * @param entity
	 * @return
	 */
	public void applyInputEffectModifiersFromEntity(EntityLivingBase entity) {
		List<ICWEffectModifier> inputEffectModifiers = getEffectModifiersFromEntity(entity, true);
		for(ICWEffectModifier effectModifier : inputEffectModifiers) {
			effectModifier.applyOnEffect(this);
		}
	}
	
	/**
	 * Utility method to collect all the output modifiers from an entity, and modifiy this effect using them.
	 * @param entity
	 * @return
	 */
	public void applyOutputEffectModifiersFromEntity(EntityLivingBase entity) {
		List<ICWEffectModifier> outputEffectModifiers = getEffectModifiersFromEntity(entity, false);
		for(ICWEffectModifier effectModifier : outputEffectModifiers) {
			effectModifier.applyOnEffect(this);
		}
	}
	
	/**
	 * Utility method to collect all the modifiers from an entity, also prepares stackable modifiers.
	 * Collects from potion effects and passive abilities
	 * @param enity
	 * @param isInputModifier
	 * @return
	 */
	public static List<ICWEffectModifier> getEffectModifiersFromEntity(EntityLivingBase entity, boolean isInputModifier) {
		List<ICWEffectModifier> effectModifiers = new ArrayList<ICWEffectModifier>();
		for(PotionEffect potionEffect : BattleClassesUtils.getActivePotionEffectsFromEntity(entity)) {
			if(BattleClassesUtils.getPotionByID(potionEffect.getPotionID()) instanceof ICWEffectModifierOwner) {
				ICWEffectModifierOwner effectModifierOwner = (ICWEffectModifierOwner) BattleClassesUtils.getPotionByID(potionEffect.getPotionID());
				for(ICWEffectModifier effectModifier : effectModifierOwner.getEffectModifiers()) {
					if(effectModifier.isInputModifier() == isInputModifier) {
						if(effectModifier instanceof IStackableModifier) {
							((IStackableModifier)effectModifier).updateStackCount(potionEffect.getAmplifier());
						}
						effectModifiers.add(effectModifier);
					}
				}
			}
		}
		if(entity instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer) entity;
			BattleClassesSpellBook spellBook = BattleClassesUtils.getPlayerSpellBook(entityPlayer);
			if(spellBook != null) {
				for(BattleClassesAbstractAbilityPassive passiveAbility : spellBook.getPassiveAbilitiesInArray()) {
					if(passiveAbility instanceof ICWEffectModifierOwner) {
						ICWEffectModifierOwner effectModifierOwner = (ICWEffectModifierOwner) passiveAbility;
						for(ICWEffectModifier effectModifier : effectModifierOwner.getEffectModifiers()) {
							if(effectModifier.isInputModifier() == isInputModifier) {
								/*
								if(effectModifier instanceof IStackableModifier) {
									((IStackableModifier)effectModifier).setStackCount(potionEffect.getAmplifier());
								}
								*/
								effectModifiers.add(effectModifier);
							}
						}
					}
				}
			}
		}
		return effectModifiers;
	}
}
