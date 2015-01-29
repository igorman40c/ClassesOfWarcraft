package mods.battleclasses.enums;

public enum EnumBattleClassesWeaponHeldType {
	ONE_HANDED,
	MAIN_HANDED,
	TWO_HANDED,
	OFF_HANDED
	;
	public float getAttributeMultiplier() {
		switch(this) {
			case MAIN_HANDED: {
				return 0.75F;
			}
			case OFF_HANDED: {
				return 0.25F;
			}
			case ONE_HANDED: {
				return 0.5F;
			}
			case TWO_HANDED: {
				return 1F;
			}
			default: {
				return 1F;
			}
		}
	}
}
