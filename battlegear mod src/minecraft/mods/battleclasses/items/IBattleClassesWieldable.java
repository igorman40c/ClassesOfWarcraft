package mods.battleclasses.items;

import mods.battleclasses.enumhelper.EnumBattleClassesItemHandRequirement;
import mods.battleclasses.enumhelper.EnumBattleClassesItemRarity;
import mods.battlegear2.api.IAllowItem;
import mods.battlegear2.api.IOffhandDual;
import mods.battlegear2.api.ISheathed;
import mods.battlegear2.api.weapons.IBattlegearWeapon;

public interface IBattleClassesWieldable extends ISheathed,IOffhandDual,IAllowItem {
	public EnumBattleClassesItemHandRequirement getHandRequirement();
	public EnumBattleClassesItemRarity getRarity();
}
