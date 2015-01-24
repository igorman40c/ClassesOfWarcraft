package mods.battleclasses.enumhelper;

import net.minecraft.util.ResourceLocation;

public enum EnumBattleClassesPlayerClass {
	NONE,
	
	MAGE,
	PRIEST,
	WARLOCK,
	ROGUE,
	HUNTER,
	PALADIN,
	WARRIOR
	;
	
	public ResourceLocation getIconResourceLocation() {
		return new ResourceLocation("battleclasses", "textures/sharedicons/classes/" + this.toString().toLowerCase() + ".png");
	}
}
