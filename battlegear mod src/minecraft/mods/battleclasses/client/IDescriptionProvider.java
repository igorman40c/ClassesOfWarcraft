package mods.battleclasses.client;

/**
 * Description provider interface. Classes implementing this will generate dynamic description about what they do.
 * Returned strings will be used displayed in ability tooltips. 
 * @author Zsolt
 */
public interface IDescriptionProvider {
	/**
	 * Dynamicly generated, translated description, about what the object does.
	 * @return
	 */
	public String getTranslatedDescription();
}
