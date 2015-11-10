package mods.battleclasses.items.weapons;

import mods.battleclasses.enums.EnumBattleClassesItemRarity;
import mods.battleclasses.items.IAttributeProviderItem;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;
import mods.battlegear2.api.IAllowItem;
import mods.battlegear2.api.IOffhandDual;
import mods.battlegear2.api.ISheathed;
import mods.battlegear2.api.weapons.IBattlegearWeapon;

public interface IBattleClassesHandHeld extends ISheathed,IOffhandDual,IAllowItem, IAttributeProviderItem {
	public EnumBattleClassesHandHeldType getHeldType();
	//public EnumBattleClassesItemRarity getRarity();
}
