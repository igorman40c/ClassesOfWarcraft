package mods.battleclasses.items.misc;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public interface AmmoItem {
	public EnumSet<EnumBattleClassesPlayerClass> getClassesUsing();
}
