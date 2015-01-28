package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public interface IPlayerClassAccess {
	public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet();
}
