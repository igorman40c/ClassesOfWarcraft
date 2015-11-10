package mods.battleclasses.items.misc;

import java.util.EnumSet;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.enums.EnumBattleClassesPlayerClass;
import mods.battleclasses.items.BattleClassesItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BattleClassesItemAmmo extends Item implements AmmoItem
{
	
	protected EnumSet<EnumBattleClassesPlayerClass> classesUsing = EnumSet.noneOf(EnumBattleClassesPlayerClass.class);
	
    public BattleClassesItemAmmo(String name, EnumSet<EnumBattleClassesPlayerClass> classesUsing)
    {
    	super();
    	this.classesUsing = classesUsing;
    	this.setCreativeTab(BattleClassesItem.TabWeapons);
    	this.setName(name);
    }
    
    protected void setName(String name) {
    	this.setUnlocalizedName("battleclasses:"+name);
    	this.setTextureName("battleclasses:"+ name);
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack) {
    	return true;
    }

	@Override
	public EnumSet<EnumBattleClassesPlayerClass> getClassesUsing() {
		return classesUsing;
	}
}
