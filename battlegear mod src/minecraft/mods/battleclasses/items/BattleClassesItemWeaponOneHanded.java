package mods.battleclasses.items;

import net.minecraft.item.ItemStack;
import mods.battleclasses.enumhelper.EnumBattleClassesWeaponHeldType;
import mods.battlegear2.api.IAllowItem;

public class BattleClassesItemWeaponOneHanded extends BattleClassesItemWeapon implements IAllowItem {

	public BattleClassesItemWeaponOneHanded(ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.heldType = EnumBattleClassesWeaponHeldType.OFF_HANDED;
	}

}
