package mods.battleclasses.attributes;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public interface ICWClassAccessAttributeModifier extends ICWAttributeModifierOwner {
	public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet();
}
