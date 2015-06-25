package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.ICWClassAccessAttributeModifier;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public interface IAttributeProviderItem extends ICWClassAccessAttributeModifier {
	public BattleClassesAttributes getAttributes();
}
