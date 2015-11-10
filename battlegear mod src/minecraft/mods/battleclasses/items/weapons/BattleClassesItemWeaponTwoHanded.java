package mods.battleclasses.items.weapons;

import java.util.EnumSet;

import net.minecraft.item.ItemStack;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.items.BattleClassesItem;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;

public class BattleClassesItemWeaponTwoHanded extends BattleClassesItemWeapon implements IHighDetailWeapon {

	
	public BattleClassesItemWeaponTwoHanded() {
		super();
		this.handHeldType = EnumBattleClassesHandHeldType.TWO_HANDED;
		BattleClassesItem.TwoHandedWeaponList.add(this);
	}
	
	public BattleClassesItemWeaponTwoHanded(String textureName) {
		this();		
		this.setName(textureName);
		classAccessSet = EnumSet.of(
				EnumBattleClassesPlayerClass.MAGE,
				EnumBattleClassesPlayerClass.PRIEST,
				EnumBattleClassesPlayerClass.WARLOCK,
				EnumBattleClassesPlayerClass.ROGUE,
				EnumBattleClassesPlayerClass.HUNTER,
				EnumBattleClassesPlayerClass.PALADIN,
				EnumBattleClassesPlayerClass.WARRIOR);
	}
	
	
	public BattleClassesItemWeaponTwoHanded(String textureName, float parAnchor, boolean parInvertSheat) {
		this(textureName);
		this.anchor = parAnchor;
		this.invertSheat = parInvertSheat;
	}
	
	public boolean invertSheat = false;
	public float anchor = 0.25F;
	
	public BattleClassesItemWeaponTwoHanded setAnchorAndInvertSheat(float anchor, boolean invertSheat) {
		this.anchor = anchor;
		this.invertSheat = invertSheat;
		return this;
	}
	
	@Override
	public float getRelativeAnchorPointX() {
		// TODO Auto-generated method stub
		return anchor;
	}

	@Override
	public float getRelativeAnchorPointY() {
		// TODO Auto-generated method stub
		return anchor;
	}
	
	@Override
	public boolean sheatheOnBack(ItemStack item) {
		return true;
	}
	
	@Override
	public boolean invertOnBack(ItemStack item) {
		return invertSheat;
	}

	@Override
	public float getScalefactor() {
		// TODO Auto-generated method stub
		return 1;
	}
	
}
