package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.ICWAttributeModifierOwnerClassFocused;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public interface IAttributeProviderItem extends ICWAttributeModifierOwnerClassFocused {
	public BattleClassesAttributes getAttributes();
}
