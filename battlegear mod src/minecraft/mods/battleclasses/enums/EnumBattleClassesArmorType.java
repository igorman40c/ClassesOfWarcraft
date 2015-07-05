package mods.battleclasses.enums;

import net.minecraft.util.StatCollector;
import mods.battleclasses.client.INameProvider;

public enum EnumBattleClassesArmorType implements INameProvider {
	CLOTH,
	LEATHER,
	MAIL,
	PLATE;

	@Override
	public String getTranslatedName() {
		return StatCollector.translateToLocal("bcarmortype." + this.toString().toLowerCase());
	}
	
}
