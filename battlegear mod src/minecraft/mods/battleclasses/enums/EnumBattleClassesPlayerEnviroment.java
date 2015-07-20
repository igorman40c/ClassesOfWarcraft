package mods.battleclasses.enums;

import net.minecraft.util.StatCollector;
import mods.battleclasses.client.INameProvider;

public enum EnumBattleClassesPlayerEnviroment implements INameProvider {
	PVE,
	PVP,
	UNIVERSAL;
	
	public String getUnlocalizedPrefix() {
		return "bcenviroment";
	}
	
	public String getUnlocalizedName() {
		return this.getUnlocalizedPrefix() + "." + this.toString().toLowerCase();
	}

	@Override
	public String getTranslatedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName());
	}
}
