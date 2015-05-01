package mods.battleclasses.ability.effect;

import java.util.List;
import java.util.Random;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BattleClassesAbstractAbilityEffectInstantValue extends BattleClassesAbstractAbilityEffect {
	
	BattleClassesAbstractAbilityEffectInstantValue() {
		super();
	}
	
	BattleClassesAbstractAbilityEffectInstantValue(float valueBase, float valueBonusCoefficient, float valueTotalRandomness) {
		this();
		this.valueBase = valueBase;
		this.valueBonusCoefficient = valueBonusCoefficient;
		this.valueTotalRandomness = valueTotalRandomness;
	}

	/**
	 * Permanentaly stores the value that will be used for damaging or healing. 
	 * Value set on @prepareToPerform.
	 * Value deleted on @performByOwnerOnTarget.
	 */
	private float outputValue = 0;
	/**
	 * Permanentaly stores if the outputValue is critical or not
	 * Value set on @prepareToPerform.
	 * Value deleted on @performByOwnerOnTarget.
	 */
	private boolean outputCritical = false;
	
	private Random rand = new Random();
	
	/**
	 * The minimum value of the effect.
	 */
	protected float valueBase = 0;
	/**
	 * The multiplier to be used on the (attribute based) power value that will be that will be added to the @valueBase.
	 */
	protected float valueBonusCoefficient = 0;
	/**
	 * A multiplier, used to randomize the the output value with in its range.
	 * Example: randomness = 0.1 turns 10 damage into 9~11
	 */
	protected float valueTotalRandomness = 0;
	
	/**
	 * A multiplier for the output, used to amplify or dampen the effect by the school of the ability
	 */
	protected float valueBalancer = 1F;
	
	/**
	 * Creates a outputValue from the recieved attributes
	 * @param attributesForParentAbility
	 * @return
	 */
	public float getValueByAttributeBasedPower(BattleClassesAttributes attributesForParentAbility) {
		float power = attributesForParentAbility.getValueForAbilitySchool(this.getAbilitySchool());
		return (this.valueBase + this.valueBonusCoefficient * power * (1-valueTotalRandomness +  this.rand.nextFloat()*valueTotalRandomness*2)) * valueBalancer;
	}
	
	public void performValueEffect(BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier, 
			EntityLivingBase owner, EntityLivingBase target) {
		this.prepareToPerform(attributesForParentAbility, critChance, partialMultiplier, owner, target);
		this.performByOwnerOnTarget(owner, target);
		this.resetOutput();
		this.resetModifiers();
	}
	
	/**
	 * Must be called before perform effect to prepare the output value.
	 * @param attributesForParentAbility
	 * @param critChance
	 * @param partialMultiplier
	 * @param owner
	 * @param target
	 */
	protected void prepareToPerform(BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier,
			EntityLivingBase owner, EntityLivingBase target) {
		//Inital output data
		this.outputValue = this.getValueByAttributeBasedPower(attributesForParentAbility)*partialMultiplier;
		float criticalChance = critChance;
		//Collecting modifications
		this.applyOutputEffectModifiersFromEntity(owner);
		this.applyInputEffectModifiersFromEntity(target);
		//Applying modifications on the output
		this.outputValue *= this.modifierMultiplier;
		criticalChance += this.modifierCriticalBonus;
		
		//Setting critical
		if(criticalChance >= rand.nextFloat()) {
			this.outputCritical = true;
			this.outputValue *= this.getAbilitySchool().getCriticalStrikeBonus();
		}
	}
	
	/**
	 * Resets the output variables
	 */
	protected void resetOutput() {
		this.outputValue = 0;
		this.outputCritical = false;
	}
	
	protected float getOutputValue() {
		return this.outputValue;
	}
	
	void setValueBalancer(float valueBalancer) {
		this.valueBalancer = valueBalancer;
	}
}
