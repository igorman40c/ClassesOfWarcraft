package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public interface IAttributeProvider {
	public BattleClassesAttributes getAttributes();
	public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet();
}
