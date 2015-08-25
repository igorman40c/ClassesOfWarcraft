package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.attributes.BattleClassesAttributes;
import mods.battleclasses.attributes.ICWAttributeModifier;
import mods.battleclasses.attributes.ICWClassAccessAttributeModifier;
import mods.battleclasses.client.IItemTooltipProvider;
import mods.battleclasses.client.ITooltipProvider;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;

public interface IAttributeProviderItem extends ICWClassAccessAttributeModifier, IItemTooltipProvider {
	public BattleClassesAttributes getAttributes();
	public int getItemLevel();
}
