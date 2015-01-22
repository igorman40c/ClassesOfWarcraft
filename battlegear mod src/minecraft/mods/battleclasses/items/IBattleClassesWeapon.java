package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.enumhelper.EnumBattleClassesWeaponHeldType;
import mods.battleclasses.enumhelper.EnumBattleClassesItemRarity;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battlegear2.api.IUsableItem;

public interface IBattleClassesWeapon extends IBattleClassesHandHeld, IUsableItem {
	public float getBaseWeaponDamage();
	public float getBonusReach();
    public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet();
}
