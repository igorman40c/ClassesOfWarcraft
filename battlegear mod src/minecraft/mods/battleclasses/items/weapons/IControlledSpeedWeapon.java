package mods.battleclasses.items.weapons;

public interface IControlledSpeedWeapon {
	/**
	 * Returns the time between two weaponswings. Higher value means lower speed generally
	 * @return
	 */
	public float getSpeedInSeconds();
}
