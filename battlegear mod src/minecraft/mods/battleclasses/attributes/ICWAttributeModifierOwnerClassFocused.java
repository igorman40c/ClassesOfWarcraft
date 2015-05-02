package mods.battleclasses.attributes;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public interface ICWAttributeModifierOwnerClassFocused extends ICWAttributeModifierOwner {
	public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet();
}
