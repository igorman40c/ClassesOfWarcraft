package mods.battleclasses.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BattleClassesCreativeTabs extends CreativeTabs {

	public BattleClassesCreativeTabs(String label) {
		super(label);
	}

	public Item tabIconItem;
	
	@Override
	public Item getTabIconItem() {
		// TODO Auto-generated method stub
		return this.tabIconItem;
	}

}
