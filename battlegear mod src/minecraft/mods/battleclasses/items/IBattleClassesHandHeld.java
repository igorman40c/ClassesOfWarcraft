package mods.battleclasses.items;

import mods.battleclasses.enums.EnumBattleClassesItemRarity;
import mods.battleclasses.enums.EnumBattleClassesWeaponHeldType;
import mods.battlegear2.api.IAllowItem;
import mods.battlegear2.api.IOffhandDual;
import mods.battlegear2.api.ISheathed;
import mods.battlegear2.api.weapons.IBattlegearWeapon;

public interface IBattleClassesHandHeld extends ISheathed,IOffhandDual,IAllowItem, IPlayerClassAccess {
	public EnumBattleClassesWeaponHeldType getHeldType();
	public EnumBattleClassesItemRarity getRarity();
}
