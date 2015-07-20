package mods.battleclasses.enums;

import net.minecraft.util.StatCollector;
import mods.battleclasses.client.INameProvider;

public enum EnumBattleClassesArmorType implements INameProvider {
	CLOTH,
	LEATHER,
	MAIL,
	PLATE;
	
	protected static final String unlocalizedPrefix = "bcarmortype."; 
	
	public String getUnlocalizedName() {
		return unlocalizedPrefix + this.toString().toLowerCase();
	}

	public String getTranslatedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName());
	}
	
}
