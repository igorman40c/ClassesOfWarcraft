package mods.battleclasses.enums;

import net.minecraft.util.StatCollector;

public enum EnumBattleClassesAbilityCastingType {
	CASTED,
	CHANNELED,
	INSTANT,
	
	UNKNOWN;
	
	public String getTranslatedAbilityInfo() {
		return StatCollector.translateToLocal("bcability.info.casttype." + this.toString().toLowerCase());
	}
}
