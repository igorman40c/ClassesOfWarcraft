package mods.battleclasses.items.misc;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BattleClassesItemGem extends Item
{
	public static final int ITEM_GEM_TYPES_COUNT = 7;
	
    public BattleClassesItemGem()
    {
           super();
           this.setHasSubtypes(true);
           this.setCreativeTab(CreativeTabs.tabMaterials);
           this.setName("gemstone");
    }
    
    protected void setName(String name) {
    	this.setUnlocalizedName("battleclasses:"+name);
    }
   
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
   
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
           icons = new IIcon[ITEM_GEM_TYPES_COUNT];
           for(int i = 0; i < icons.length; i++)
           {
        	   icons[i] = par1IconRegister.registerIcon((super.getUnlocalizedName().substring(5)) + i);
           }
    }
      
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
    	int i = par1ItemStack.getItemDamage();
    	return this.getUnlocalizedName() +  i;
    }
    
    public IIcon getIconFromDamage(int par1)
    {
    	if (par1 > ITEM_GEM_TYPES_COUNT-1) {
    		par1 = 0;
    	}
            
    	return icons[par1];
    }

	 @SideOnly(Side.CLIENT)
	 public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List)
	 {
		 for (int i = 1; i < ITEM_GEM_TYPES_COUNT; ++i)
	     {
	         par3List.add(new ItemStack(this, 1, i));
	     }
	 }
}