package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.enumhelper.EnumBattleClassesItemHandRequirement;
import mods.battleclasses.enumhelper.EnumBattleClassesItemRarity;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;

public interface IBattleClassesWeapon extends IBattleClassesWieldable {
	public float getBaseWeaponDamage();
	public float getBonusReach();
    public EnumSet<EnumBattleClassesPlayerClass> getClassAccessSet();
}
