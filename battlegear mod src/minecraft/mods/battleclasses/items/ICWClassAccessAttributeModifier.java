package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public interface ICWClassAccessAttributeModifier extends ICWAttributeModifier {
	public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet();
}
