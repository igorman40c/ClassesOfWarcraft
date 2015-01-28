package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesItemRarity;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesWeaponHeldType;
import mods.battlegear2.api.IUsableItem;

public interface IBattleClassesWeapon extends IBattleClassesHandHeld, IUsableItem {
	public float getBaseWeaponDamage();
	public float getBonusReach();
    public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet();
}
