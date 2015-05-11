package mods.battleclasses.client;

import java.util.List;

/**
 * Tooltip provider interface for displaying hover-over info on GUI.
 * @author Zsolt
 */
public interface ITooltipProvider {
	/**
	 * Returns a list of strings containing the translated and/or colored strings to be displayed. 
	 * The strings in the list are width-formated. (Format width in UI element!)
	 * @return
	 */
	public List<String> getTooltipText();
}
