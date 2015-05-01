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
	 * If the effect can be modified depending on its parent ability, applies the modifications.
	 * @param effect
	 * @return
	 */
	public void applyOnEffect(BattleClassesAbstractAbilityEffect effect);
}
