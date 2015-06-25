package mods.battleclasses.enums;

import java.util.EnumSet;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

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
	
	public String getTranslatedName() {
		return StatCollector.translateToLocal("bc" + this.toString().toLowerCase());
	}
	
	public boolean isEligibleForClassAccessSet(EnumSet<EnumBattleClassesPlayerClass> classAccessSet) {
		return classAccessSet.contains(NONE) || classAccessSet.contains(this); 
	}
}
