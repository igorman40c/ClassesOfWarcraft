package mods.battleclasses.items.weapons;

import java.util.EnumSet;

import mods.battleclasses.enums.EnumBattleClassesItemRarity;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;
import mods.battlegear2.api.IUsableItem;

public interface IBattleClassesWeapon extends ISpellBookControllerItem, IControlledSpeedWeapon, IUsableItem, IBattleClassesHandHeld {
	public float getWeaponDamage();
	public float getBonusReach();
}
