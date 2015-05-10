package mods.battleclasses.core;

/**
 * Stackable interface for modifiers, used by Attribute and Effect modifiers.
 * @author Zsolt
 */
public interface IStackableModifier {
		
	/**
	 * Use after or within the constructor of the modifier to set the stackable properties, to enable the stacking.
	 * @param maxStackCount
	 * @param stackBonusMultiplier
	 */
	public void setStackableProperties(int maxStackCount, float stackBonusMultiplier);
	
	
	/**
	 * Determines the maximal stack count of the potion which contains the modifier. 
	 * @return
	 */
	public int getMaxStackCount();
	
	/**
	 * Determines the value that should multiply the modification, calculated by stackBonusMultiplier * currentStackCount
	 * @return
	 */
	public float getStackBonus();
	
	
	/**
	 * Use this before applying the modifications, to update the currentStackCount of the stackable modifier.
	 * @param stackCount
	 */
	public void updateStackCount(int stackCount);
	
	/**
	 * Use this after applying the modifications, to reset the currentStackCount of the stackable modifier.
	 * @param stackCount
	 */
	public void resetStackCount();
}
