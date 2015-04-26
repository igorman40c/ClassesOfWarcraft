package mods.battleclasses.ability.effect;

import java.util.Random;

import mods.battleclasses.ability.active.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesAbilitySchool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BattleClassesAbstractAbilityEffectInstantValue extends BattleClassesAbstractAbilityEffect implements IValueEffect {
	
	BattleClassesAbstractAbilityEffectInstantValue(EnumBattleClassesAbilitySchool school) {
		super(school);
	}
	
	BattleClassesAbstractAbilityEffectInstantValue(EnumBattleClassesAbilitySchool school, float valueBase, float valueBonusCoefficient, float valueTotalRandomness) {
		this(school);
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
	 * Creates a outputValue from the recieved attributes
	 * @param attributesForParentAbility
	 * @return
	 */
	public float getValueByAttributeBasedPower(BattleClassesAttributes attributesForParentAbility) {
		float power = attributesForParentAbility.getValueForAbilitySchool(this.school);
		return this.valueBase + this.valueBonusCoefficient * power * (1-valueTotalRandomness +  this.rand.nextFloat()*valueTotalRandomness*2);
	}
	
	public void performValueEffect(BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier, 
			EntityLivingBase owner, EntityLivingBase target) {
		this.prepareToPerform(attributesForParentAbility, critChance, partialMultiplier);
		this.performByOwnerOnTarget(owner, target);
		this.resetOutput();
	}
	
	/**
	 * Must be called before perform effect to prepare the output value;
	 * @param attributesForParentAbility
	 * @param critChance
	 * @param multiplier
	 */
	protected void prepareToPerform(BattleClassesAttributes attributesForParentAbility, float critChance, float partialMultiplier) {
		this.outputValue = this.getValueByAttributeBasedPower(attributesForParentAbility)*partialMultiplier;
		if(critChance >= rand.nextFloat()) {
			this.outputValue *= this.school.getCriticalStrikeBonus();
			this.outputCritical = true;
		}
	}
	
	/**
	 * Resets the output variables
	 */
	protected void resetOutput() {
		this.outputValue = 0;
		this.outputCritical = false;
	}
	
}
