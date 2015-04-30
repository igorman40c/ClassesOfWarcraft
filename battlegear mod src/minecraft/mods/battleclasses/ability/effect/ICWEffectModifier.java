package mods.battleclasses.ability.effect;


/**
 * Classes of Warcraft Ability effect modifier interface.
 * @author Zsolt
 */
public interface ICWEffectModifier {
	/**
	 * Determines the use of the modifier.
	 * @return if value is true - INPUT used modifier
	 * @return if value is false - OUTPUT used modifier
	 */
	public boolean isInputModifier();
	/**
	 * A multiplier to apply on the value of the effect.
	 * @return
	 */
	public float getValueMultiplier();
	/**
	 * The ammount of bonus to add to the critical chance of the effect.
	 * @return
	 */
	public float getCriticalChanceBonus();
	/**
	 * Checks if the effect can be modified depending on its parent ability.
	 * @param effect
	 * @return
	 */
	public boolean canBeAppliedOnEffect(BattleClassesAbstractAbilityEffect effect);
}
