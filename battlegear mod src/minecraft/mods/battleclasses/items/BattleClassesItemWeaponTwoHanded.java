package mods.battleclasses.items;

import java.util.EnumSet;

import net.minecraft.item.ItemStack;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.enums.EnumBattleClassesHandHeldType;

public class BattleClassesItemWeaponTwoHanded extends BattleClassesItemWeapon implements IHighDetailWeapon {

	public BattleClassesItemWeaponTwoHanded(String textureName) {
		super();
		
		this.setName(textureName);
		classAccessSet = EnumSet.of(
				EnumBattleClassesPlayerClass.MAGE,
				EnumBattleClassesPlayerClass.PRIEST,
				EnumBattleClassesPlayerClass.WARLOCK,
				EnumBattleClassesPlayerClass.ROGUE,
				EnumBattleClassesPlayerClass.HUNTER,
				EnumBattleClassesPlayerClass.PALADIN,
				EnumBattleClassesPlayerClass.WARRIOR);
		BattleClassesItems.TwoHandedWeaponList.add(this);
		this.handHeldType = EnumBattleClassesHandHeldType.TWO_HANDED;
	}
	
	public BattleClassesItemWeaponTwoHanded(String textureName, float parAnchor, boolean parInvertSheat) {
		this(textureName);
		this.anchor = parAnchor;
		this.inverSheat = parInvertSheat;
	}

	public boolean inverSheat = false;
	public float anchor = 0.25F;
	
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
		return inverSheat;
	}

	@Override
	public float getScalefactor() {
		// TODO Auto-generated method stub
		return 1;
	}
	
}
