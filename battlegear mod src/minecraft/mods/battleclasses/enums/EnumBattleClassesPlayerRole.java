package mods.battleclasses.enums;

import net.minecraft.util.StatCollector;
import mods.battleclasses.client.INameProvider;

public enum EnumBattleClassesPlayerRole implements INameProvider {
	HEALER,
	DAMAGE_DEALER,
	RANGED_DAMAGE_DEALER,
	TANK,
	UNKNOWN;

	@Override
	public String getTranslatedName() {
		return StatCollector.translateToLocal("bcrole." + this.toString().toLowerCase());
	}
}
