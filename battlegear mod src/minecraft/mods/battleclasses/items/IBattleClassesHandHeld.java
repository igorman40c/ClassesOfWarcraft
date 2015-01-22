package mods.battleclasses.items;

import mods.battleclasses.enumhelper.EnumBattleClassesWeaponHeldType;
import mods.battleclasses.enumhelper.EnumBattleClassesItemRarity;
import mods.battlegear2.api.IAllowItem;
import mods.battlegear2.api.IOffhandDual;
import mods.battlegear2.api.ISheathed;
import mods.battlegear2.api.weapons.IBattlegearWeapon;

public interface IBattleClassesHandHeld extends ISheathed,IOffhandDual,IAllowItem {
	public EnumBattleClassesWeaponHeldType getHeldType();
	public EnumBattleClassesItemRarity getRarity();
}
