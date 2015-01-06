package mods.battleclasses.items;

import java.util.EnumSet;

import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;

public class BattleClassesItemWeaponTwoHanded extends BattleClassesItemWeapon implements IHighDetailWeapon {

	public BattleClassesItemWeaponTwoHanded(ToolMaterial toolMaterial, String textureName) {
		super(toolMaterial);
		
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
	}

	public static float broadSwordAnchor = 0.25F;
	
	@Override
	public float getRelativeAnchorPointX() {
		// TODO Auto-generated method stub
		return broadSwordAnchor;
	}

	@Override
	public float getRelativeAnchorPointY() {
		// TODO Auto-generated method stub
		return broadSwordAnchor;
	}
	
}
